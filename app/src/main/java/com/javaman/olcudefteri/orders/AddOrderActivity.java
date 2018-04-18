package com.javaman.olcudefteri.orders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.google.gson.Gson;
import com.javaman.olcudefteri.base.BasePresenter;
import com.javaman.olcudefteri.base.BasePresenterImpl;
import com.javaman.olcudefteri.base.BaseView;
import com.javaman.olcudefteri.home.HomeActivity;
import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.login.LoginActivity;
import com.javaman.olcudefteri.orders.model.CustomerDetailModel;
import com.javaman.olcudefteri.orders.model.response.AddCustomerResponse;
import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;
import com.javaman.olcudefteri.reports.ReportsActivity;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddOrderActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener,BaseView {


    public static final String MyPREFERENCES = "MyPrefs";
    public static final String ARG_ADD_ORDER = "arg_add_order";
    public static final String ARG_REGISTER_CUSTOMER_FRAGMENT_TAG = "register-customer-fragment";
    public static final String ARG_ADD_ORDER_LINE_FRAGMENT_TAG = "add-order-line-fragment";
    private Bundle customerFormData = new Bundle();
    SharedPreferences sharedPref;
    private boolean isCustomerRegister = false;
    private AddCustomerResponse addCustomerResponse;
    private OrderDetailResponseModel orderDetailResponseModel;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ActionBarDrawerToggle toggle;
    SharedPreferenceHelper sharedPreferenceHelper;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private BasePresenter mBasePresenter;
    SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        sharedPreferenceHelper=new SharedPreferenceHelper(getApplicationContext());
        sharedPreferenceHelper.removeKey("orderLineSummaryResponse");
        mBasePresenter=new BasePresenterImpl(this);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        navigationView.setCheckedItem(R.id.measure);
        navigationView.setNavigationItemSelectedListener(this);

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



    @Override
    public void onBackPressed() {
        Log.d("Count : ", "" + fragmentManager.getBackStackEntryCount());

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
        } else {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {

            case R.id.home:
                Intent home = new Intent(AddOrderActivity.this, HomeActivity.class);
                startActivity(home);
                break;
            case R.id.orders:
                Intent orders = new Intent(AddOrderActivity.this, OrdersActivity.class);
                startActivity(orders);
                break;
            case R.id.measure:
                Intent measure = new Intent(AddOrderActivity.this, AddOrderActivity.class);
                Bundle bundle=new Bundle();
                measure.putExtra("init-key","first-init-add-order");
                startActivity(measure);
                break;
            case R.id.report:
                Intent report = new Intent(AddOrderActivity.this, ReportsActivity.class);
                startActivity(report);
                break;
            // this is done, now let us go and intialise the home page.
            // after this lets start copying the above.
            // FOLLOW MEEEEE>>>
        }


        return true;
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
            toggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);// show back button
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            //show hamburger
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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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


