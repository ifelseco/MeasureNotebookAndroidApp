package com.javaman.olcudefteri.ui.orders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.javaman.olcudefteri.ui.home.HomeActivity;
import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.ui.login.LoginActivity;
import com.javaman.olcudefteri.model.PageModel;
import com.javaman.olcudefteri.model.OrderDetailPage;
import com.javaman.olcudefteri.model.OrderDetailResponseModel;
import com.javaman.olcudefteri.model.OrderSummaryPageReponseModel;
import com.javaman.olcudefteri.presenter.OrdersPresenter;
import com.javaman.olcudefteri.presenter.impl.OrdersPresenterImpl;
import com.javaman.olcudefteri.view.OrdersView;
import com.javaman.olcudefteri.utill.MyUtil;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class OrdersActivity extends AppCompatActivity
        implements OrdersView,
        View.OnLongClickListener, SwipeRefreshLayout.OnRefreshListener,
        SearchView.OnQueryTextListener, AdapterView.OnItemSelectedListener, View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    @BindView(R.id.recycle_orders)
    RecyclerView recyclerView;

    @BindView(R.id.tv_order_select_counter)
    TextView tvOrderSelectCount;


    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @BindView(R.id.linear_layout_zero_orders)
    LinearLayout linearLayoutZeroOrder;


    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.filter_spinner)
    Spinner spinnerFilter;

    @BindView(R.id.bottom_navigation)
    AHBottomNavigation ahBottomNavigation;

    SearchView searchView;

    SharedPreferenceHelper sharedPreferenceHelper;
    RecyclerView.Adapter adapter;
    LinearLayoutManager linearLayoutManager;
    OrdersPresenter mOrdersPresenter;
    List<OrderDetailResponseModel> orderList = new ArrayList<>();
    OrderDetailPage curOrderDetailPage=new OrderDetailPage();
    ArrayList<OrderDetailResponseModel> selectedOrderList = new ArrayList<>();
    public static final String ARG_SAVED_ORDERS = "last-saved-orders";

    private int first = 0;
    private int rows = 10;

    boolean isActionModeActive = false;
    boolean isScrooling = false;
    int countSelectedOrders = 0;
    private int currentItems, totalItems, scrollOutItems;
    private boolean spinnerFirstLoad = true;
    private boolean isFilterMode = false;
    int notfCount=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());
        sharedPreferenceHelper.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        sharedPreferenceHelper.removeKey("orderLineSummaryResponse");
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        notfCount=getNotificationCountFromPref();
        initBottomNav();
        if (savedInstanceState != null) {
            this.orderList = savedInstanceState.getParcelableArrayList(ARG_SAVED_ORDERS);

        }

        initView();
        initRcyclerView();



        if (savedInstanceState == null) {
            sendPageRequest(first, rows);
        }


    }

    private int getNotificationCountFromPref() {
        if(sharedPreferenceHelper.containKey("notf-count")){
            return sharedPreferenceHelper.getIntegerPreference("notf-count",-1);
        }else{
            return -1;
        }
    }


    private void initBottomNav() {
        AHBottomNavigationItem item_home = new AHBottomNavigationItem(R.string.title_home, R.drawable.ic_home_black_24dp, R.color.hintColor);
        AHBottomNavigationItem item_orders = new AHBottomNavigationItem(R.string.title_orders, R.drawable.ic_assignment_black_24dp, R.color.hintColor);
        AHBottomNavigationItem item_add_order = new AHBottomNavigationItem(R.string.title_add_order, R.drawable.ic_add_circle_black_24dp, R.color.hintColor);
        AHBottomNavigationItem item_notification = new AHBottomNavigationItem(R.string.title_notifications, R.drawable.ic_notifications_black_24dp, R.color.hintColor);
        ahBottomNavigation.addItem(item_home);
        ahBottomNavigation.addItem(item_orders);
        ahBottomNavigation.addItem(item_add_order);
        ahBottomNavigation.addItem(item_notification);
        ahBottomNavigation.setDefaultBackgroundColor(fetchColor(R.color.secondaryTextColor));
        ahBottomNavigation.setAccentColor(fetchColor(R.color.primaryColor));
        ahBottomNavigation.setInactiveColor(fetchColor(R.color.hintColor));
        ahBottomNavigation.setCurrentItem(1);
        ahBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ahBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        if(notfCount>0){
            ahBottomNavigation.setNotification(""+notfCount,3);
        }
        ahBottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {
            if(position==0){
                Intent home = new Intent(OrdersActivity.this, HomeActivity.class);
                startActivity(home);
                return true;
            }else if(position==1){

                return true;
            }else if(position==2){
                Intent measure = new Intent(OrdersActivity.this, AddOrderActivity.class);
                measure.putExtra("init-key", "first-init-add-order");
                startActivity(measure);
                return true;
            }else if(position==3){
                Intent home = new Intent(OrdersActivity.this, HomeActivity.class);
                home.putExtra("init-key", "get-notification-fragment");
                startActivity(home);
                return true;
            }
            return true;
        });

    }

    private int fetchColor(@ColorRes int color) {
        return ContextCompat.getColor(this, color);
    }

    public void initView() {

        fillSpinner();
        linearLayoutZeroOrder.setVisibility(View.GONE);
        mOrdersPresenter = new OrdersPresenterImpl(this);
        tvOrderSelectCount.setVisibility(View.GONE);
        swipeRefreshLayout.setOnRefreshListener(this);
        searchView = findViewById(R.id.searchView);
        searchView.setQueryHint("Sip. no giriniz...");
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnCloseListener(() -> {
            clearActionMode();
            Toast.makeText(this, "" + isFilterMode, Toast.LENGTH_SHORT).show();
            refreshOrder();
            return false;
        });
        searchView.setOnSearchClickListener(this);


    }

    private void fillSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_filter, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter);

    }

    public void initRcyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new OrderAdapter(this, this.orderList != null ? this.orderList : new ArrayList<OrderDetailResponseModel>());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrooling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (isScrooling && (currentItems + scrollOutItems == totalItems) && !curOrderDetailPage.isLast()) {
                    isScrooling = false;
                    first += 10;
                    if(isFilterMode){
                        if(spinnerFilter.getSelectedItemPosition()==0){
                            sendPageRequest(first, rows);
                        }else{
                            sendPageRequestWithFilter(first,rows,spinnerFilter.getSelectedItemPosition()-1);
                        }
                    }else{
                        sendPageRequest(first, rows);
                    }

                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_orders, menu);
        MenuItem menuItemFilter = menu.findItem(R.id.item_filter);
        if (menuItemFilter != null) {
            MyUtil.tintMenuIcon(this, menuItemFilter, android.R.color.white);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.item_delete) {
            if (this.selectedOrderList.size() > 0) {
                showConfirmDialog(this.selectedOrderList);

            } else {
                Toast.makeText(this, "Sipariş seçmediniz.", Toast.LENGTH_SHORT).show();
                clearActionMode();
            }


        } else if (id == R.id.item_filter) {
            isFilterMode = true;
            showFilterSpinner();


        } else if (id == R.id.item_close) {
            clearToolbarAppendNewMenu(R.menu.menu_orders);
            hideFilterSpinner();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFilterSpinner() {
        clearToolbarAppendNewMenu(R.menu.menu_close);
        spinnerFilter.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.GONE);
        tvOrderSelectCount.setVisibility(View.GONE);

    }



    private void hideFilterSpinner() {
        searchView.setVisibility(View.VISIBLE);
        tvOrderSelectCount.setVisibility(View.GONE);
        spinnerFilter.setVisibility(View.GONE);
        isFilterMode = false;
    }

    public void clearToolbarAppendNewMenu(int menuId){
        toolbar.getMenu().clear();
        toolbar.inflateMenu(menuId);
    }


    private void showConfirmDialog(final ArrayList<OrderDetailResponseModel> selectedOrderList) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Onaylıyor musunuz?")
                .setContentText("Siparişler silinecek.")
                .setConfirmText("Evet")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sendDeleteOrderListRequest(selectedOrderList);
                        sDialog.dismissWithAnimation();
                    }
                })
                .showCancelButton(true)
                .setCancelText("Vazgeç!")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }

    public void clearActionMode() {
        this.selectedOrderList.clear();
        hideArrowButton();
        isFilterMode = false;
        isActionModeActive = false;
        adapter.notifyDataSetChanged();
        clearToolbarAppendNewMenu(R.menu.menu_orders);
        tvOrderSelectCount.setVisibility(View.GONE);
        searchView.setVisibility(View.VISIBLE);
        tvOrderSelectCount.setText("0 sipariş seçildi");
        countSelectedOrders = 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPreferenceHelper.setStringPreference("lastActivity", getClass().getName());
    }

    @Override
    public void sendPageRequest(int first, int rows) {

        String xAuthToken = getSessionIdFromPref();
        PageModel pageModel = new PageModel();
        pageModel.setFirst(first);
        pageModel.setRows(rows);
        mOrdersPresenter.sendPageRequest(xAuthToken, pageModel);

    }

    @Override
    public void sendPageRequestWithFilter(int first, int rows, int orderStatus) {
        String xAuthToken = getSessionIdFromPref();
        PageModel pageModel = new PageModel();
        pageModel.setFirst(first);
        pageModel.setRows(rows);
        mOrdersPresenter.sendPageRequestWithFilter(xAuthToken,orderStatus,pageModel);
    }

    @Override
    public void sendDeleteOrderListRequest(ArrayList<OrderDetailResponseModel> orders) {
        String xAuthToken = getSessionIdFromPref();
        mOrdersPresenter.sendDeleteOrderListRequest(xAuthToken, orders);
    }

    @Override
    public void deleteOrdersFromAdapter(ArrayList<OrderDetailResponseModel> orders) {
        OrderAdapter orderAdapter = (OrderAdapter) adapter;
        orderAdapter.deleteSelectedItems(orders);
        clearActionMode();
    }

    @Override
    public void updateOrderAfterSearch(List<OrderDetailResponseModel> orders) {
        OrderAdapter orderAdapter = (OrderAdapter) adapter;
        orderAdapter.updateList(orders);
    }

    @Override
    public void orderSearch(String query) {
        String headerData = getSessionIdFromPref();
        mOrdersPresenter.orderSearch(headerData, query);
    }

    @Override
    public void navigateToLogin() {
        startActivity(new Intent(OrdersActivity.this, LoginActivity.class));
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public String getSessionIdFromPref() {
        String xAuthToken = sharedPreferenceHelper.getStringPreference("sessionId", null);
        return xAuthToken;
    }

    @Override
    public void checkSession() {

    }

    @Override
    public void showAlert(String message, boolean isError, boolean isOnlyToast) {
        if (isError) {

            if (isOnlyToast) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            } else {
                SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
                pDialog.setTitleText("Hata...");
                pDialog.setContentText(message);
                pDialog.setConfirmText("Kapat");
                pDialog.setCancelable(true);
                pDialog.show();
            }

        } else {
            if (isOnlyToast) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            } else {
                SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
                pDialog.setTitleText(message);
                pDialog.setConfirmText("Kapat");
                pDialog.setCancelable(true);
                pDialog.show();
            }
        }
    }

    @Override
    public void setEmptyBacground() {
        linearLayoutZeroOrder.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyBacground() {
        linearLayoutZeroOrder.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void getOrders(OrderSummaryPageReponseModel orderSummaryPageReponseModel) {
        this.orderList=orderSummaryPageReponseModel.getOrderDetailPage().getContent();
        this.curOrderDetailPage=orderSummaryPageReponseModel.getOrderDetailPage();

        OrderAdapter orderAdapter = (OrderAdapter) adapter;
        orderAdapter.updateList(orderSummaryPageReponseModel);

    }


    @Override
    public boolean onLongClick(View view) {
        Log.d("Selecetde order :", "" + selectedOrderList.size());
        clearToolbarAppendNewMenu(R.menu.menu_action_delete);
        hideFilterSpinner();
        searchView.setVisibility(View.GONE);
        tvOrderSelectCount.setVisibility(View.VISIBLE);
        isActionModeActive = true;
        adapter.notifyDataSetChanged();
        showArrowButton();
        return true;
    }


    public void showArrowButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// show back button
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    public void hideArrowButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);// show back button
    }

    public void prepareSelection(View view, int position) {
        Log.d("Selecetde order :", "" + selectedOrderList.size());
        if (((CheckBox) view).isChecked()) {
            selectedOrderList.add(this.orderList.get(position));
            Log.d("SelectedOrder", " " + selectedOrderList.size());
            countSelectedOrders++;
            updateCounter(countSelectedOrders);
        } else {
            selectedOrderList.remove(this.orderList.get(position));
            Log.d("SelectedOrder", " " + selectedOrderList.size());

            countSelectedOrders--;
            updateCounter(countSelectedOrders);
        }

    }

    public void updateCounter(int counter) {
        if (counter == 0) {
            tvOrderSelectCount.setText("0 sipariş seçildi");
        } else {
            tvOrderSelectCount.setText(counter + " sipariş seçildi");
        }
    }

    @Override
    public void onBackPressed() {
        if (isActionModeActive) {
            clearActionMode();
            adapter.notifyDataSetChanged();
        } else {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onRefresh() {
        refreshOrder();
    }

    public void refreshOrder() {

        first = 0;
        OrderAdapter orderAdapter = (OrderAdapter) adapter;
        orderAdapter.clearList();
        if(isFilterMode && spinnerFilter.getSelectedItemPosition()>0){
            sendPageRequestWithFilter(first,rows,spinnerFilter.getSelectedItemPosition()-1);
        }else{
            sendPageRequest(first, rows);
        }



    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ARG_SAVED_ORDERS, (ArrayList<? extends Parcelable>) this.orderList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOrdersPresenter.onDestroy();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query.length() > 0) {
            orderSearch(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    @OnItemSelected(R.id.filter_spinner)
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case 0:
                if (!spinnerFirstLoad) {
                    refreshOrder();
                }
                spinnerFirstLoad = false;
                break;
            case 1:
                sendPageRequestWithFilter(0,rows,0);
                break;
            case 2:
                sendPageRequestWithFilter(0,rows,1);
                break;
            case 3:
                sendPageRequestWithFilter(0,rows,2);
                break;
            case 4:
                sendPageRequestWithFilter(0,rows,3);
                break;
            case 5:
                sendPageRequestWithFilter(0,rows,4);
                break;
            case 6:
                sendPageRequestWithFilter(0,rows,5);
                break;
            case 7:
                sendPageRequestWithFilter(0,rows,6);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        hideFilterSpinner();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.searchView) {
            toolbar.getMenu().clear();
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key=="notf-count"){
            notfCount=sharedPreferenceHelper.getIntegerPreference("notf-count",-1);
            if(notfCount>0){
                ahBottomNavigation.setNotification(""+notfCount,3);
            }
        }
    }
}
