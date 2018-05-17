package com.javaman.olcudefteri.ui.orders;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.gson.Gson;
import com.javaman.olcudefteri.presenter.BasePresenter;
import com.javaman.olcudefteri.presenter.impl.BasePresenterImpl;
import com.javaman.olcudefteri.view.BaseView;
import com.javaman.olcudefteri.ui.home.HomeActivity;
import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.login.LoginActivity;
import com.javaman.olcudefteri.model.CustomerDetailModel;
import com.javaman.olcudefteri.model.AddCustomerResponse;
import com.javaman.olcudefteri.model.OrderDetailResponseModel;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddOrderActivity extends AppCompatActivity
        implements FragmentManager.OnBackStackChangedListener,BaseView {


    public static final String ARG_ADD_ORDER = "arg_add_order";
    public static final String ARG_REGISTER_CUSTOMER_FRAGMENT_TAG = "register-customer-fragment";
    public static final String ARG_ADD_ORDER_LINE_FRAGMENT_TAG = "add-order-line-fragment";

    private AddCustomerResponse addCustomerResponse;
    private OrderDetailResponseModel orderDetailResponseModel;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ActionBarDrawerToggle toggle;
    SharedPreferenceHelper sharedPreferenceHelper;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.bottom_navigation)
    AHBottomNavigation ahBottomNavigation;

    private BasePresenter mBasePresenter;
    SweetAlertDialog pDialog;
    int notfCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        sharedPreferenceHelper=new SharedPreferenceHelper(getApplicationContext());
        sharedPreferenceHelper.removeKey("orderLineSummaryResponse");
        mBasePresenter=new BasePresenterImpl(this);
        ButterKnife.bind(this);
        notfCount=getNotificationCountFromPref();
        initBottomNav();
        setSupportActionBar(toolbar);


        Bundle bundle=getIntent().getExtras();

        if(bundle!=null){

            if(bundle.containsKey(OrderDetailActivity.ARG_CURRENT_ORDER)){
                orderDetailResponseModel=getIntent().getParcelableExtra(OrderDetailActivity.ARG_CURRENT_ORDER);
                if(orderDetailResponseModel!=null){
                    addAddOrderLineFragment(orderDetailResponseModel);
                }else{
                    addFragmentRegisterCustomer();
                }
            }else if(bundle.containsKey(ARG_ADD_ORDER)){
                addCustomerResponse=getIntent().getParcelableExtra(ARG_ADD_ORDER);
                orderDetailResponseModel=new OrderDetailResponseModel();
                CustomerDetailModel customerDetailModel=new CustomerDetailModel();
                customerDetailModel.setNameSurname(addCustomerResponse.getCustomerNameSurname());
                customerDetailModel.setId(addCustomerResponse.getCustomerId());
                orderDetailResponseModel.setCustomer(customerDetailModel);
                orderDetailResponseModel.setOrderDate(addCustomerResponse.getOrderDate());
                orderDetailResponseModel.setId(addCustomerResponse.getId());
                orderDetailResponseModel.setOrderNumber(addCustomerResponse.getOrderNumber());
                if(orderDetailResponseModel!=null){
                    addAddOrderLineFragment(orderDetailResponseModel);
                }else{
                    addFragmentRegisterCustomer();
                }
            }else if(bundle.containsKey("init-key")){

                sharedPreferenceHelper.removeKey("orderDetailResponse");
                addFragmentRegisterCustomer();
            }


        }else if(sharedPreferenceHelper.containKey("orderDetailResponse")){
            String data=sharedPreferenceHelper.getStringPreference("orderDetailResponse","");
            if(!data.equals("")){
                Gson gson = new Gson();
                orderDetailResponseModel= gson.fromJson(data, OrderDetailResponseModel.class);

                if(orderDetailResponseModel!=null){
                    addAddOrderLineFragment(orderDetailResponseModel);
                }else{
                    addFragmentRegisterCustomer();
                }
            }

        }else {
            addFragmentRegisterCustomer();
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
        ahBottomNavigation.setDefaultBackgroundColor(fetchColor(R.color.colorAccentText));
        ahBottomNavigation.setAccentColor(fetchColor(R.color.yello));
        ahBottomNavigation.setInactiveColor(fetchColor(R.color.hintColor));
        ahBottomNavigation.setCurrentItem(2);
        ahBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ahBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        if(notfCount>0){
            ahBottomNavigation.setNotification(""+notfCount,3);
        }
        ahBottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {
            if(position==0){
                Intent home = new Intent(AddOrderActivity.this, HomeActivity.class);
                startActivity(home);
                return true;
            }else if(position==1){
                Intent orders = new Intent(AddOrderActivity.this, OrdersActivity.class);
                startActivity(orders);
                return true;
            }else if(position==2){

                return true;
            }else if(position==3){
                Intent home = new Intent(AddOrderActivity.this, HomeActivity.class);
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

    public void addFragmentRegisterCustomer() {

        checkSession();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);
        fragmentTransaction = fragmentManager.beginTransaction();
        RegisterCustomerFragment registerCustomerFragment = new RegisterCustomerFragment();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.add(R.id.order_fragment_container, registerCustomerFragment, ARG_REGISTER_CUSTOMER_FRAGMENT_TAG)
                .addToBackStack("register-customer-fragment");

        fragmentTransaction.commit();


    }

    public void addAddOrderLineFragment(OrderDetailResponseModel orderDetailResponseModel) {
        hideBottomNav();
        checkSession();
        AddOrderLineFragment addOrderLineFragment = new AddOrderLineFragment();
        Bundle bundle=new Bundle();
        bundle.putParcelable(OrderDetailActivity.ARG_CURRENT_ORDER,orderDetailResponseModel);
        addOrderLineFragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.order_fragment_container, addOrderLineFragment, ARG_ADD_ORDER_LINE_FRAGMENT_TAG)
                .addToBackStack("add-order-fragment");
        fragmentTransaction.commit();


    }

    private int getNotificationCountFromPref() {
        if(sharedPreferenceHelper.containKey("notf-count")){
            return sharedPreferenceHelper.getIntegerPreference("notf-count",-1);
        }else{
            return -1;
        }
    }

    @Override
    public void onBackPressed() {
        Log.d("Count : ", "" + fragmentManager.getBackStackEntryCount());

       if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
        } else {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onPause() {
        super.onPause();
        Fragment currentFragment=getSupportFragmentManager().findFragmentById(R.id.order_fragment_container);
        if(currentFragment.getTag().equals(ARG_ADD_ORDER_LINE_FRAGMENT_TAG)){
            Gson gson = new Gson();
            String jsonOrderDetailResponse=gson.toJson(orderDetailResponseModel);
            sharedPreferenceHelper.setStringPreference("orderDetailResponse",jsonOrderDetailResponse);
        }
        sharedPreferenceHelper.setStringPreference("lastActivity",getClass().getName());
    }


    @Override
    public void onBackStackChanged() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);// show back button
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            //show hamburger

            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void hideBottomNav(){
        ahBottomNavigation.hideBottomNavigation(true);
    }


    @Override
    public void checkSession() {
        String sessionId=getSessionIdFromPref();
        mBasePresenter.checkSession(sessionId);
    }



    @Override
    public String getSessionIdFromPref() {
        String xAuthToken=sharedPreferenceHelper.getStringPreference("sessionId",null);
        return xAuthToken;
    }

    @Override
    public void removeKeyFromPref(String key) {
        sharedPreferenceHelper.removeKey(key);

    }



    @Override
    public void showAlert(String message,boolean isToast) {
        if(isToast){
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }else{
            pDialog=new SweetAlertDialog(this,SweetAlertDialog.NORMAL_TYPE);
            pDialog.setTitleText(message);
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    @Override
    public void showProgress(String message) {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(message);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    public void hideProgress() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }


    @Override
    public void logout() {
        String sessionId=getSessionIdFromPref();
        mBasePresenter.logout(sessionId);
    }

    @Override
    public void navigateToLogin() {
        startActivity(new Intent(AddOrderActivity.this , LoginActivity.class));

    }
}


