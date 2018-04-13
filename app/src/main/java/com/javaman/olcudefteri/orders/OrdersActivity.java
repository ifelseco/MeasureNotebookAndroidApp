package com.javaman.olcudefteri.orders;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.javaman.olcudefteri.home.HomeActivity;
import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.login.LoginActivity;
import com.javaman.olcudefteri.orders.model.PageModel;
import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;
import com.javaman.olcudefteri.orders.model.response.OrderSummaryReponseModel;
import com.javaman.olcudefteri.orders.presenter.OrdersPresenter;
import com.javaman.olcudefteri.orders.presenter.OrdersPresenterImpl;
import com.javaman.olcudefteri.orders.view.OrdersView;
import com.javaman.olcudefteri.reports.ReportsActivity;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class OrdersActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OrdersView, View.OnLongClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recycle_orders)
    RecyclerView recyclerView;

    @BindView(R.id.tv_order_select_counter)
    TextView tvOrderSelectCount;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    SharedPreferenceHelper sharedPreferenceHelper;
    ActionBarDrawerToggle toggle;
    RecyclerView.Adapter adapter;
    LinearLayoutManager linearLayoutManager;
    OrdersPresenter mOrdersPresenter;
    List<OrderDetailResponseModel> orderList=new ArrayList<>();
    ArrayList<OrderDetailResponseModel> selectedOrderList = new ArrayList<>();
    public static final String ARG_SAVED_ORDERS = "last-saved-orders";

    private int first = 0;
    private int rows = 10;

    boolean isActionModeActive = false;
    boolean isScrooling=false;
    int countSelectedOrders = 0;
    int totalOrder;
    private int currentItems,totalItems,scrollOutItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        sharedPreferenceHelper=new SharedPreferenceHelper(getApplicationContext());
        sharedPreferenceHelper.removeKey("orderLineSummaryResponse");
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (savedInstanceState!=null){
            this.orderList=savedInstanceState.getParcelableArrayList(ARG_SAVED_ORDERS);
        }

        initView();
        initRcyclerView();

        if(savedInstanceState==null){
            sendPageRequest(first, rows);
        }




    }


    public void initView() {


        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(R.id.orders);
        navigationView.setNavigationItemSelectedListener(this);
        mOrdersPresenter = new OrdersPresenterImpl(this);
        tvOrderSelectCount.setVisibility(View.GONE);
        swipeRefreshLayout.setOnRefreshListener(this);


    }

    public void initRcyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        setRecyclerViewAdapter();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrooling=true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems=linearLayoutManager.getChildCount();
                totalItems=linearLayoutManager.getItemCount();
                scrollOutItems=linearLayoutManager.findFirstVisibleItemPosition();

                if(isScrooling && (currentItems+scrollOutItems==totalItems)){
                    isScrooling=false;
                    first+=10;
                    sendPageRequest(first,rows);
                }else{

                }
            }
        });
    }

    public void setRecyclerViewAdapter() {
        adapter = new OrderAdapter(this, this.orderList!=null?this.orderList:new ArrayList<OrderDetailResponseModel>());
        recyclerView.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_orders, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.item_delete) {
            if (this.selectedOrderList.size() > 0) {
                showConfirmDialog(this.selectedOrderList);

            } else {
                Toast.makeText(this, "Sipariş seçmediniz.", Toast.LENGTH_SHORT).show();
                clearActionMode();
            }


        }

        return super.onOptionsItemSelected(item);
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
        isActionModeActive = false;
        adapter.notifyDataSetChanged();
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.home);
        showHamburgerButton();
        tvOrderSelectCount.setVisibility(View.GONE);
        tvOrderSelectCount.setText("0 sipariş seçildi");
        countSelectedOrders = 0;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {

            case R.id.home:
                Intent home = new Intent(OrdersActivity.this, HomeActivity.class);
                startActivity(home);
                break;
            case R.id.orders:
                Intent orders = new Intent(OrdersActivity.this, OrdersActivity.class);
                startActivity(orders);
                break;
            case R.id.measure:
                Intent measure = new Intent(OrdersActivity.this, AddOrderActivity.class);
                Bundle bundle=new Bundle();
                measure.putExtra("init-key","first-init-add-order");
                startActivity(measure);
                break;
            case R.id.report:
                Intent report = new Intent(OrdersActivity.this, ReportsActivity.class);
                startActivity(report);
                break;
            // this is done, now let us go and intialise the home page.
            // after this lets start copying the above.
            // FOLLOW MEEEEE>>>
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    public void updateOrderFromAdapter(List<OrderDetailResponseModel> orders) {
        OrderAdapter orderAdapter = (OrderAdapter) adapter;
        orderAdapter.updateList(orders);
    }

    @Override
    public void navigateToLogin() {
        startActivity(new Intent(OrdersActivity.this , LoginActivity.class));
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public String getSessionIdFromPref() {
        String xAuthToken=sharedPreferenceHelper.getStringPreference("sessionId",null);
        return xAuthToken;
    }

    @Override
    public void checkSession() {

    }

    @Override
    public void showAlert(String message,boolean isError,boolean isOnlyToast) {
        if(isError){

            if(isOnlyToast){
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }else{
                SweetAlertDialog pDialog= new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
                pDialog.setTitleText("Hata...");
                pDialog.setContentText(message);
                pDialog.setConfirmText("Kapat");
                pDialog.setCancelable(true);
                pDialog.show();
            }

        }else{
            if(isOnlyToast){
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }else{
                SweetAlertDialog pDialog=new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
                pDialog.setTitleText(message);
                pDialog.setConfirmText("Kapat");
                pDialog.setCancelable(true);
                pDialog.show();
            }
        }
    }

    @Override
    public void getOrders(OrderSummaryReponseModel orderSummaryReponseModel) {

        this.orderList.addAll(orderSummaryReponseModel.getOrderDetailPage().getContent());
        this.totalOrder = orderSummaryReponseModel.getOrderDetailPage().getTotalElements();
        updateOrderFromAdapter(this.orderList);


    }


    @Override
    public boolean onLongClick(View view) {
        Log.d("Selecetde order :",""+selectedOrderList.size());
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_action);
        tvOrderSelectCount.setVisibility(View.VISIBLE);
        isActionModeActive = true;
        adapter.notifyDataSetChanged();
        showArrowButton();
        return true;
    }

    public void showHamburgerButton() {
        toggle.setDrawerIndicatorEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle.syncState();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);

            }
        });
    }

    public void showArrowButton() {
        toggle.setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// show back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void prepareSelection(View view, int position) {
        Log.d("Selecetde order :",""+selectedOrderList.size());
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (isActionModeActive) {
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

    public void refreshOrder(){
        first=0;
        OrderAdapter orderAdapter = (OrderAdapter) adapter;
        orderAdapter.clearList();
        sendPageRequest(first,rows);
        swipeRefreshLayout.setRefreshing(false);
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
}
