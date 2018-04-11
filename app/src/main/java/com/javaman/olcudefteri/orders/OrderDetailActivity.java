package com.javaman.olcudefteri.orders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.orders.model.CustomerDetailModel;
import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;
import com.javaman.olcudefteri.orders.model.PageModel;
import com.javaman.olcudefteri.orders.model.response.AddCustomerResponse;
import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;
import com.javaman.olcudefteri.orders.model.response.OrderLineSummaryResponseModel;
import com.javaman.olcudefteri.orders.presenter.OrderLinePresenter;
import com.javaman.olcudefteri.orders.presenter.OrderLinePresenterImpl;
import com.javaman.olcudefteri.orders.presenter.OrdersPresenter;
import com.javaman.olcudefteri.orders.view.OrderDetailVew;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailActivity extends AppCompatActivity implements FloatingActionMenu.OnMenuToggleListener, View.OnClickListener, OrderDetailVew {

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.container)
    ViewPager mViewPager;

    @BindView(R.id.fab_menu)
    FloatingActionMenu fabMenu;

    @BindView(R.id.fab_order_edit)
    com.github.clans.fab.FloatingActionButton fabOrderEdit;

    @BindView(R.id.fab_order_delete)
    com.github.clans.fab.FloatingActionButton fabOrderDelete;

    @BindView(R.id.fab_order_line_add)
    com.github.clans.fab.FloatingActionButton fabOrderLineAdd;


    @BindView(R.id.fab_order_status)
    com.github.clans.fab.FloatingActionButton fabOrderStatus;

    @BindView(R.id.fab_customer_edit)
    com.github.clans.fab.FloatingActionButton fabCustomerEdit;

    @BindView(R.id.fab_customer_delete)
    com.github.clans.fab.FloatingActionButton fabCsutomerDelete;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @BindView(R.id.progress_bar_order_line)
    ProgressBar progressBarOrderLine;

    private OrderDetailResponseModel orderDetailResponseModel;
    private CustomerDetailModel customerDetailModel;
    private OrderLineSummaryResponseModel orderLineSummaryResponseModel;
    private List<OrderLineDetailModel> orderLines = new ArrayList<>();
    private Long orderId;
    private Long orderIdFromNotification;
    Bundle arguments;
    OrderLinePresenter mOrderLinePresenter;
    SharedPreferenceHelper sharedPreferenceHelper;
    public static final String ARG_CURRENT_ORDER = "current_order";
    public static final String ARG_NOTIFICATION_ORDER = "notification_order";
    public static final String ARG_CURRENT_CUSTOMER = "current_customer_detail";
    public static final String ARG_ORDER_LINES = "current_order_lines";
    public static final String ARG_SAVED_ORDER = "saved_order";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        sharedPreferenceHelper=new SharedPreferenceHelper(getApplicationContext());
        setSupportActionBar(toolbar);
        initOrderFabMenu();
        mOrderLinePresenter = new OrderLinePresenterImpl(this);

        if (sharedPreferenceHelper.containKey("orderLineSummaryResponse")) {
            String data = sharedPreferenceHelper.getStringPreference("orderLineSummaryResponse", "");
            if (!data.equals("")) {
                Gson gson = new Gson();
                OrderLineSummaryResponseModel orderLineSummaryResponseModel = gson.fromJson(data, OrderLineSummaryResponseModel.class);
                this.orderLineSummaryResponseModel = orderLineSummaryResponseModel;
                setArgumentForFragments();
                initTab();
            }

        } else {
            if (savedInstanceState == null) {
                if (getIntent().getExtras().containsKey(ARG_NOTIFICATION_ORDER)) {
                    orderIdFromNotification = getIntent().getExtras().getLong(ARG_NOTIFICATION_ORDER);
                    if (orderIdFromNotification != null && orderIdFromNotification > 0) {
                        sendGetOrderLineRequest(orderIdFromNotification);
                    }
                } else if (getIntent().getExtras().containsKey(OrderDetailActivity.ARG_CURRENT_ORDER)) {
                    orderId = getIntent().getExtras().getLong(OrderDetailActivity.ARG_CURRENT_ORDER);
                    if (orderId != null && orderId > 0) {
                        sendGetOrderLineRequest(orderId);
                    }
                }else if(getIntent().getExtras().containsKey(AddOrderLineFragment.ARG_GOTO_ORDERLINE)){
                    orderId = getIntent().getExtras().getLong(AddOrderLineFragment.ARG_GOTO_ORDERLINE);
                    if (orderId != null && orderId > 0) {
                        sendGetOrderLineRequest(orderId);
                    }
                }
            } else {
                this.orderLineSummaryResponseModel = savedInstanceState.getParcelable(ARG_SAVED_ORDER);
                setArgumentForFragments();
                initTab();
            }
        }




        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    initCustomerFabMenu();
                } else if (position == 2) {
                    showOrderLineFabMenu();
                } else {
                    initOrderFabMenu();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }


    private void initTab() {
        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);
    }


    public void setArgumentForFragments() {
        arguments = new Bundle();


        orderDetailResponseModel = orderLineSummaryResponseModel.getOrder();
        customerDetailModel = orderDetailResponseModel.getCustomer();
        orderLines = orderLineSummaryResponseModel.getOrderLineDetailList();

        arguments.putParcelable(OrderDetailActivity.ARG_CURRENT_ORDER, orderDetailResponseModel);
        arguments.putParcelable(OrderDetailActivity.ARG_CURRENT_CUSTOMER, customerDetailModel);
        arguments.putParcelableArrayList(ARG_ORDER_LINES, (ArrayList<? extends Parcelable>) orderLines);
    }

    private void showOrderLineFabMenu() {
        fabMenu.setVisibility(View.VISIBLE);
        fabMenu.close(true);
        fabOrderDelete.setVisibility(View.GONE);
        fabOrderEdit.setVisibility(View.GONE);
        fabOrderStatus.setVisibility(View.GONE);
        fabCsutomerDelete.setVisibility(View.GONE);
        fabCustomerEdit.setVisibility(View.GONE);
        fabOrderLineAdd.setVisibility(View.VISIBLE);

        fabOrderLineAdd.setOnClickListener(this);
    }

    public void initOrderFabMenu() {
        fabMenu.setVisibility(View.VISIBLE);
        fabMenu.close(true);
        fabCsutomerDelete.setVisibility(View.GONE);
        fabCustomerEdit.setVisibility(View.GONE);
        fabOrderLineAdd.setVisibility(View.GONE);
        fabOrderDelete.setVisibility(View.VISIBLE);
        fabOrderEdit.setVisibility(View.VISIBLE);
        fabOrderStatus.setVisibility(View.VISIBLE);

        fabMenu.setOnMenuToggleListener(this);

        fabOrderDelete.setOnClickListener(this);
        fabOrderEdit.setOnClickListener(this);
        fabOrderStatus.setOnClickListener(this);
    }

    public void initCustomerFabMenu() {

        fabMenu.setVisibility(View.VISIBLE);
        fabMenu.close(true);
        fabOrderDelete.setVisibility(View.GONE);
        fabOrderEdit.setVisibility(View.GONE);
        fabOrderLineAdd.setVisibility(View.GONE);
        fabOrderStatus.setVisibility(View.GONE);
        fabCsutomerDelete.setVisibility(View.VISIBLE);
        fabCustomerEdit.setVisibility(View.VISIBLE);

        fabCustomerEdit.setOnClickListener(this);
        fabCsutomerDelete.setOnClickListener(this);
    }

    public void hideFab() {
        fabMenu.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_order_detail_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            //NavUtils.navigateUpTo(this, new Intent(this, OrdersActivity.class));
            Intent intent = new Intent(getApplicationContext(), OrdersActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //NavUtils.navigateUpTo(this, new Intent(this, OrdersActivity.class));
        Intent intent = new Intent(getApplicationContext(), OrdersActivity.class);
        startActivity(intent);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showDialog(DialogFragment dialogFragment , String fragmentTag){
        dialogFragment.show(getSupportFragmentManager(),fragmentTag);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_order_edit) {
            OrderUpdateDialog orderUpdateDialog=new OrderUpdateDialog();
            Bundle bundle=new Bundle();
            bundle.putParcelable(AddOrderLineFragment.ARG_GOTO_ORDER_UPDATE,orderDetailResponseModel);
            orderUpdateDialog.setArguments(bundle);
            showDialog(orderUpdateDialog,"order-update-dialog");
            fabMenu.close(true);
        } else if (v.getId() == R.id.fab_order_delete) {
            showToast("Sipariş silme diyalogu");
            fabMenu.close(true);
        } else if (v.getId() == R.id.fab_order_status) {
            showToast("Sipariş durumu değiştir");
            fabMenu.close(true);
        } else if (v.getId() == R.id.fab_customer_delete) {
            showToast("Müşteri silme");
            fabMenu.close(true);
        } else if (v.getId() == R.id.fab_customer_edit) {
            showToast("Müşteri edit");
            fabMenu.close(true);
        }else if(v.getId()==R.id.fab_order_line_add){
            //Go to AddOrderActivity -> AddOrderLineFragment
            //Send orderDetailModel
            Intent intent = new Intent(this, AddOrderActivity.class);
            intent.putExtra(OrderDetailActivity.ARG_CURRENT_ORDER , orderDetailResponseModel);
            startActivity(intent);
            fabMenu.close(true);
        }

    }

    @Override
    public void onMenuToggle(boolean opened) {
        if (opened) {

        } else {

        }
    }

    private void setupViewPager(ViewPager viewPager) {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        OrderDetailFragment orderDetailFragment = new OrderDetailFragment();
        orderDetailFragment.setArguments(arguments);

        CustomerDetailFragment customerDetailFragment = new CustomerDetailFragment();
        customerDetailFragment.setArguments(arguments);

        OrderLineFragment orderLineFragment = new OrderLineFragment();
        orderLineFragment.setArguments(arguments);

        adapter.addFragment(orderDetailFragment, "Sipariş Bilgileri");
        adapter.addFragment(customerDetailFragment, "Müşteri Bilgileri");
        adapter.addFragment(orderLineFragment, "Sipariş Ölçüleri");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void sendGetOrderLineRequest(Long orderId) {
        String xAuthToken = getSessionIdFromPref();
        mOrderLinePresenter.sendGetOrderLineRequest(xAuthToken, orderId);
    }

    @Override
    public void showProgress() {
        progressBarOrderLine.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBarOrderLine.setVisibility(View.GONE);
    }

    @Override
    public void deleteOrder(Long orderId) {

    }

    @Override
    public void updateOrder(OrderDetailResponseModel orderDetailResponseModel) {

    }

    @Override
    public String getSessionIdFromPref() {
        String xAuthToken=sharedPreferenceHelper.getStringPreference("sessionId",null);
        return xAuthToken;
    }
    @Override
    public void navigateToLogin() {

    }

    @Override
    public void checkSession() {

    }

    @Override
    public void showAlert(String message) {
        showToast(message);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_SAVED_ORDER, this.orderLineSummaryResponseModel);
    }

    @Override
    public void getOrderLines(OrderLineSummaryResponseModel orderLineSummaryResponseModel) {
        this.orderLineSummaryResponseModel = orderLineSummaryResponseModel;
        setArgumentForFragments();
        initTab();
        if(getIntent().getExtras().containsKey(AddOrderLineFragment.ARG_GOTO_ORDERLINE)){
            mViewPager.setCurrentItem(3,true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Gson gson = new Gson();
        String json = gson.toJson(orderLineSummaryResponseModel);
        sharedPreferenceHelper.setStringPreference("orderLineSummaryResponse", json);
        sharedPreferenceHelper.setStringPreference("lastActivity", getClass().getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOrderLinePresenter.onDestroy();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
