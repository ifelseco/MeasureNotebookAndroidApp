package com.javaman.olcudefteri.tailor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.vision.text.Text;
import com.google.gson.Gson;
import com.javaman.olcudefteri.R;

import com.javaman.olcudefteri.orders.model.response.OrderLineSummaryResponseModel;
import com.javaman.olcudefteri.orders.presenter.OrderLinePresenter;
import com.javaman.olcudefteri.orders.presenter.OrderLinePresenterImpl;
import com.javaman.olcudefteri.orders.view.OrderDetailVew;

import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TailorOrderDetailActivity extends AppCompatActivity implements OrderDetailVew{



    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progress_bar_order_line)
    ProgressBar progressBarOrderLine;

    @BindView(R.id.recycler_tailor_line)
    RecyclerView recyclerView;

    RecyclerView.Adapter adapter;



    private OrderLineSummaryResponseModel orderLineSummaryResponseModel;
    private Long orderId;
    OrderLinePresenter mOrderLinePresenter;
    SharedPreferenceHelper sharedPreferenceHelper;
    public static final String ARG_CURRENT_ORDER = "current_order";
    public static final String ARG_SAVED_ORDER = "saved_order";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tailor_order_detail);
        ButterKnife.bind(this);
        sharedPreferenceHelper=new SharedPreferenceHelper(getApplicationContext());
        setSupportActionBar(toolbar);

        mOrderLinePresenter = new OrderLinePresenterImpl(this);


        Bundle bundle= getIntent().getExtras();

        if(bundle!=null){
            if (bundle.containsKey(TailorOrderDetailActivity.ARG_CURRENT_ORDER)) {
                orderId = getIntent().getExtras().getLong(TailorOrderDetailActivity.ARG_CURRENT_ORDER);
                if (orderId != null && orderId > 0) {
                    sendGetOrderLineRequest(orderId);
                }
            }
        }else{
            Intent intent = new Intent(this, TailorHomeActivity.class);
            startActivity(intent);
        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


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
            Intent intent = new Intent(getApplicationContext(), TailorHomeActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(), TailorHomeActivity.class);
        startActivity(intent);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
    public void getOrderLines(OrderLineSummaryResponseModel orderLineSummaryResponseModel) {
        this.orderLineSummaryResponseModel = orderLineSummaryResponseModel;
        setRecyclerView();

    }

    private void setRecyclerView() {
        adapter = new TailorOrderLineAdapter(this,this.orderLineSummaryResponseModel.getOrderLineDetailList());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPreferenceHelper.setStringPreference("lastActivity", getClass().getName());


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOrderLinePresenter.onDestroy();
    }



}
