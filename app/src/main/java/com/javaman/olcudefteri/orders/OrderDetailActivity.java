package com.javaman.olcudefteri.orders;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.orders.event.OrderDeleteEvent;
import com.javaman.olcudefteri.orders.event.OrderUpdateEvent;
import com.javaman.olcudefteri.orders.model.CustomerDetailModel;
import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;
import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;
import com.javaman.olcudefteri.orders.model.response.OrderLineSummaryResponseModel;
import com.javaman.olcudefteri.orders.presenter.OrderLinePresenter;
import com.javaman.olcudefteri.orders.presenter.OrderLinePresenterImpl;
import com.javaman.olcudefteri.orders.view.OrderDetailVew;
import com.javaman.olcudefteri.utill.MyUtil;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;

public class OrderDetailActivity extends AppCompatActivity implements FloatingActionMenu.OnMenuToggleListener, View.OnClickListener, OrderDetailVew, View.OnTouchListener {

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


    @BindView(R.id.fab_order_status)
    com.github.clans.fab.FloatingActionButton fabOrderStatus;

    @BindView(R.id.fab_customer_edit)
    com.github.clans.fab.FloatingActionButton fabCustomerEdit;

    @BindView(R.id.fab_customer_delete)
    com.github.clans.fab.FloatingActionButton fabCsutomerDelete;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab_container)
    RelativeLayout relativeLayoutFabContainer;



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
    public static final String ARG_GOTO_UPDATE_ORDER_FROM_ORDER_DETAIL = "update_order";

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

                Bundle bundle= getIntent().getExtras();

                if(bundle!=null){
                    if (bundle.containsKey(OrderDetailActivity.ARG_CURRENT_ORDER)) {
                        orderId = getIntent().getExtras().getLong(OrderDetailActivity.ARG_CURRENT_ORDER);
                        if (orderId != null && orderId > 0) {
                            sendGetOrderLineRequest(orderId);
                        }
                    }else if(bundle.containsKey(AddOrderLineFragment.ARG_GOTO_ORDERLINE)){
                        orderId = getIntent().getExtras().getLong(AddOrderLineFragment.ARG_GOTO_ORDERLINE);
                        if (orderId != null && orderId > 0) {
                            sendGetOrderLineRequest(orderId);
                        }
                    }
                }else{
                    Intent intent = new Intent(this, OrdersActivity.class);
                    startActivity(intent);
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
                        clearMenu();
                        initCustomerFabMenu();
                    } else if (position == 2) {

                        hideFab();
                        getAddLineMenu();
                    } else {
                        initOrderFabMenu();
                        clearMenu();
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

    private void clearMenu() {
        toolbar.getMenu().clear();
    }

    private void getAddLineMenu() {
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_add_order_line);
        MenuItem menuItemAddLine = toolbar.getMenu().findItem(R.id.item_add);

        if (menuItemAddLine != null) {
            MyUtil.tintMenuIcon(this, menuItemAddLine, android.R.color.white);
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



    public void initOrderFabMenu() {
        fabMenu.setVisibility(View.VISIBLE);

        fabCsutomerDelete.setVisibility(View.GONE);
        fabCustomerEdit.setVisibility(View.GONE);
        fabOrderDelete.setVisibility(View.VISIBLE);
        fabOrderEdit.setVisibility(View.VISIBLE);
        fabOrderStatus.setVisibility(View.VISIBLE);

        fabMenu.setOnMenuToggleListener(this);

        fabOrderDelete.setOnClickListener(this);
        fabOrderEdit.setOnClickListener(this);
        fabOrderStatus.setOnClickListener(this);
        fabMenu.close(true);
    }

    public void initCustomerFabMenu() {

        fabMenu.setVisibility(View.VISIBLE);

        fabOrderDelete.setVisibility(View.GONE);
        fabOrderEdit.setVisibility(View.GONE);
        fabOrderStatus.setVisibility(View.GONE);
        fabCsutomerDelete.setVisibility(View.VISIBLE);
        fabCustomerEdit.setVisibility(View.VISIBLE);

        fabCustomerEdit.setOnClickListener(this);
        fabCsutomerDelete.setOnClickListener(this);
        fabMenu.close(true);
    }

    public void hideFab() {
        fabMenu.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.menu_order_detail_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), OrdersActivity.class);
            startActivity(intent);
            return true;
        }else if(id==R.id.item_add){
            gotoAddOrderLine();
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
            openOrderUpdate();
            fabMenu.close(true);
        } else if (v.getId() == R.id.fab_order_delete) {
            OrderDeleteEvent orderDeleteEvent=new OrderDeleteEvent();
            orderDeleteEvent.setOrderId(orderDetailResponseModel.getId());
            EventBus.getDefault().post(orderDeleteEvent);
            fabMenu.close(true);
        } else if (v.getId() == R.id.fab_order_status) {
            fabMenu.close(true);
        } else if (v.getId() == R.id.fab_customer_delete) {
            fabMenu.close(true);
        } else if (v.getId() == R.id.fab_customer_edit) {
            fabMenu.close(true);
        }

    }

    private void gotoAddOrderLine() {
        //Go to AddOrderActivity -> AddOrderLineFragment
        //Send orderDetailModel
        Intent intent = new Intent(this, AddOrderActivity.class);
        intent.putExtra(OrderDetailActivity.ARG_CURRENT_ORDER , orderDetailResponseModel);
        startActivity(intent);
    }

    private void openOrderUpdate() {
        OrderUpdateDialog orderUpdateDialog=new OrderUpdateDialog();
        Bundle bundle=new Bundle();
        bundle.putParcelable(ARG_GOTO_UPDATE_ORDER_FROM_ORDER_DETAIL,orderDetailResponseModel);
        orderUpdateDialog.setArguments(bundle);
        showDialog(orderUpdateDialog,"order-update-dialog");
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
        if(orderLineSummaryResponseModel!=null){
            String json = gson.toJson(orderLineSummaryResponseModel);
            sharedPreferenceHelper.setStringPreference("orderLineSummaryResponse", json);
            sharedPreferenceHelper.setStringPreference("lastActivity", getClass().getName());
        }else{
            sharedPreferenceHelper.setStringPreference("lastActivity", getClass().getName());
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOrderLinePresenter.onDestroy();
    }

    @Override
    @OnTouch(R.id.fab_container)
    public boolean onTouch(View v, MotionEvent event) {
        if(v.getId()==R.id.fab_container){
            if(fabMenu.isOpened()){
                fabMenu.close(true);
                return true;
            }
        }
       return false;
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


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void updatedOrder(OrderUpdateEvent event){
        orderDetailResponseModel.setTotalAmount(event.getOrderUpdateModel().getTotalAmount());
        orderDetailResponseModel.setDepositeAmount(event.getOrderUpdateModel().getDepositeAmount());
        orderDetailResponseModel.setDeliveryDate(event.getOrderUpdateModel().getDeliveryDate());
        orderDetailResponseModel.setMeasureDate(event.getOrderUpdateModel().getMeasureDate());
        orderDetailResponseModel.setMountExist(event.getOrderUpdateModel().isMountExist());
        orderDetailResponseModel.setOrderStatus(event.getOrderUpdateModel().getOrderStatus());
    }
}
