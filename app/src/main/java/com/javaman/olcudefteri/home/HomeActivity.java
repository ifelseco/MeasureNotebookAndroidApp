package com.javaman.olcudefteri.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;


import com.google.firebase.messaging.FirebaseMessaging;
import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.base.BasePresenter;
import com.javaman.olcudefteri.base.BasePresenterImpl;
import com.javaman.olcudefteri.base.BaseView;
import com.javaman.olcudefteri.home.presenter.HomePresenter;
import com.javaman.olcudefteri.home.presenter.HomePresenterImpl;
import com.javaman.olcudefteri.home.view.HomeView;
import com.javaman.olcudefteri.login.LoginActivity;
import com.javaman.olcudefteri.orders.AddOrderActivity;
import com.javaman.olcudefteri.notification.FirebaseRegIdModel;
import com.javaman.olcudefteri.notification.FirebaseUtil;
import com.javaman.olcudefteri.orders.OrdersActivity;
import com.javaman.olcudefteri.reports.ReportsActivity;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,BaseView,HomeView {

    private static final String TAG = HomeActivity.class.getSimpleName();

    boolean doubleBackToExitPressedOnce = false;
    private HomePresenter mHomePresenter;
    private BasePresenter mBasePresenter;
    SharedPreferenceHelper sharedPreferenceHelper;
    SweetAlertDialog pDialog;
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferenceHelper=new SharedPreferenceHelper(getApplicationContext());
        sharedPreferenceHelper.removeKey("orderLineSummaryResponse");
        sharedPreferenceHelper.removeKey("orderDetailResponse");
        Log.d("mesaj","Home activity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHomePresenter=new HomePresenterImpl(this);
        mBasePresenter=new BasePresenterImpl(this);


        sendFirebaseRegIdToServer();

        FirebaseMessaging.getInstance().subscribeToTopic(FirebaseUtil.TOPIC_GLOBAL);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent measure= new Intent(HomeActivity.this,AddOrderActivity.class);
                startActivity(measure);
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.home);
        navigationView.setNavigationItemSelectedListener(this);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(doubleBackToExitPressedOnce){
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Çıkmak için tekrar basın.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
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
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){

            case R.id.home:
                Intent home= new Intent(HomeActivity.this,HomeActivity.class);
               startActivity(home);
                break;
            case R.id.orders:
                Intent orders= new Intent(HomeActivity.this,OrdersActivity.class);
                startActivity(orders);
                break;
            case R.id.measure:
                Intent measure= new Intent(HomeActivity.this,AddOrderActivity.class);
                startActivity(measure);
                break;
            case R.id.report:
                Intent report= new Intent(HomeActivity.this,ReportsActivity.class);
                startActivity(report);
                break;
            case R.id.logout:
                logout();
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
        Log.i(TAG, "onPause()");
        super.onPause();
        sharedPreferenceHelper.setStringPreference("lastActivity", getClass().getName());
    }

    @Override
    public void sendFirebaseRegIdToServer() {


        String xAuthToken=getSessionIdFromPref();
        String firebaseRegId=getFirebaseIdFromPref();

        FirebaseRegIdModel firebaseRegIdModel=new FirebaseRegIdModel(firebaseRegId);
        mHomePresenter.sendFirebaseRegIdToServer(xAuthToken,firebaseRegIdModel);


    }

    @Override
    public String getFirebaseIdFromPref() {
        String firebaseRegId=sharedPreferenceHelper.getStringPreference("firebase_reg_id",null);
        return firebaseRegId;
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
    public void showAlert(String message) {
        pDialog=new SweetAlertDialog(this,SweetAlertDialog.NORMAL_TYPE);
        pDialog.setTitleText(message);
        pDialog.setCancelable(false);
        pDialog.show();
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
        startActivity(new Intent(HomeActivity.this , LoginActivity.class));

    }

    @Override
    public void checkSession() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHomePresenter.onDestroy();
        mBasePresenter.onDestroy();
        hideProgress();
    }
}
