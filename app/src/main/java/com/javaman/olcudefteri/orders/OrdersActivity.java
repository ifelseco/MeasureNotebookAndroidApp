package com.javaman.olcudefteri.orders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.javaman.olcudefteri.home.HomeActivity;
import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.login.LoginActivity;
import com.javaman.olcudefteri.orders.model.PageModel;
import com.javaman.olcudefteri.orders.model.response.OrderDetailPage;
import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;
import com.javaman.olcudefteri.orders.model.response.OrderSummaryReponseModel;
import com.javaman.olcudefteri.orders.model.Paginator;
import com.javaman.olcudefteri.orders.presenter.OrdersPresenter;
import com.javaman.olcudefteri.orders.presenter.OrdersPresenterImpl;
import com.javaman.olcudefteri.orders.view.OrdersView;
import com.javaman.olcudefteri.reports.ReportsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrdersActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OrdersView, View.OnLongClickListener{

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

    @BindView(R.id.tv_total_orders)
    TextView tvTotalOrders;

    @BindView(R.id.tv_current_page)
    TextView tvCurrentPage;

    ActionBarDrawerToggle toggle;
    RecyclerView.Adapter adapter;
    LinearLayoutManager linearLayoutManager;
    OrdersPresenter mOrdersPresenter;
    List<OrderDetailResponseModel> orderList;
    ArrayList<OrderDetailResponseModel> selectedOrderList = new ArrayList<>();

    private int first = 0;
    private int rows = 10;

    boolean isActionModeActive = false;
    int countSelectedOrders = 0;
    int totalOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        initView();
        initRcyclerView();
        sendPageRequest(first, rows);

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
        tvCurrentPage.setText("");


    }

    public void initRcyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        setRecyclerViewAdapter();
    }

    public void setRecyclerViewAdapter() {
        adapter = new OrderAdapter(this, this.orderList!=null?this.orderList:new ArrayList<OrderDetailResponseModel>());
        recyclerView.setAdapter(adapter);

    }









    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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
                sendDeleteOrderListRequest(this.selectedOrderList);
                clearActionMode();
            } else {
                Toast.makeText(this, "Sipariş seçmediniz.", Toast.LENGTH_SHORT).show();
                clearActionMode();
            }


        }

        return super.onOptionsItemSelected(item);
    }

    public void clearActionMode() {
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastActivity", getClass().getName());
        editor.commit();
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
    public String getSessionIdFromPref() {
        SharedPreferences prefSession = getSharedPreferences("Session", Context.MODE_PRIVATE);
        String xAuthToken = prefSession.getString("sessionId", null);
        return xAuthToken;
    }

    @Override
    public void checkSession() {

    }

    @Override
    public void showAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getOrders(OrderSummaryReponseModel orderSummaryReponseModel) {

        this.orderList = orderSummaryReponseModel.getOrderDetailPage().getContent();
        this.totalOrder = orderSummaryReponseModel.getOrderDetailPage().getTotalElements();
        updateOrderFromAdapter(this.orderList);


        if (this.totalOrder > 0) {
            tvTotalOrders.setText("Toplam " + this.totalOrder + " sipariş bulundu.");
        } else {
            tvTotalOrders.setText("Sipaiş kaydınız bulunmamaktadır.");

        }




    }



    @Override
    public boolean onLongClick(View view) {
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
        if (((CheckBox) view).isChecked()) {
            selectedOrderList.add(this.orderList.get(position));
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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


}
