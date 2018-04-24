package com.javaman.olcudefteri.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import android.widget.ProgressBar;
import android.widget.Toast;


import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.firebase.messaging.FirebaseMessaging;
import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.base.BasePresenter;
import com.javaman.olcudefteri.base.BasePresenterImpl;
import com.javaman.olcudefteri.base.BaseView;
import com.javaman.olcudefteri.home.model.NotificationDetailModel;
import com.javaman.olcudefteri.home.model.NotificationSummaryModel;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,BaseView,HomeView {

    private static final String TAG = HomeActivity.class.getSimpleName();
    boolean doubleBackToExitPressedOnce = false;
    private HomePresenter mHomePresenter;
    private BasePresenter mBasePresenter;
    private NotificationSummaryModel mNotificationSummaryModel;
    SharedPreferenceHelper sharedPreferenceHelper;
    SweetAlertDialog pDialog;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    public static final String ARG_NOTIFICATIONS = "home-notifications";

    @BindView(R.id.bottom_navigation)
    AHBottomNavigation ahBottomNavigation;

    @BindView(R.id.progress_bar_home)
    ProgressBar progressBarHome;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;
    int notfCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        sharedPreferenceHelper=new SharedPreferenceHelper(getApplicationContext());
        sharedPreferenceHelper.removeKey("orderLineSummaryResponse");
        sharedPreferenceHelper.removeKey("orderDetailResponse");

        notfCount=getNotificationCountFromPref();
        FirebaseMessaging.getInstance().subscribeToTopic(FirebaseUtil.TOPIC_GLOBAL);

        mHomePresenter=new HomePresenterImpl(this);
        mBasePresenter=new BasePresenterImpl(this);
        initBottomNav();
        sendFirebaseRegIdToServer();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(R.id.home);
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void initBottomNav() {
        AHBottomNavigationItem item_search = new AHBottomNavigationItem(R.string.title_search, R.drawable.ic_search_black_24dp, R.color.hintColor);
        AHBottomNavigationItem item_processing = new AHBottomNavigationItem(R.string.title_orders, R.drawable.ic_assignment_black_24dp, R.color.hintColor);
        AHBottomNavigationItem item_processed = new AHBottomNavigationItem(R.string.title_add_order, R.drawable.ic_add_circle_black_24dp, R.color.hintColor);
        AHBottomNavigationItem item_notification = new AHBottomNavigationItem(R.string.title_notifications, R.drawable.ic_notifications_black_24dp, R.color.hintColor);
        ahBottomNavigation.addItem(item_search);
        ahBottomNavigation.addItem(item_processing);
        ahBottomNavigation.addItem(item_processed);
        ahBottomNavigation.addItem(item_notification);
        ahBottomNavigation.setDefaultBackgroundColor(fetchColor(R.color.colorAccentText));
        ahBottomNavigation.setAccentColor(fetchColor(R.color.yello));
        ahBottomNavigation.setInactiveColor(fetchColor(R.color.hintColor));
        ahBottomNavigation.setCurrentItem(0);
        ahBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ahBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        if(notfCount>0){
            ahBottomNavigation.setNotification(""+notfCount,3);
        }
        ahBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if(position==0){
                    //getSearchFragment();
                    return true;
                }else if(position==1){
                    Intent orders= new Intent(HomeActivity.this,OrdersActivity.class);
                    startActivity(orders);
                    return true;
                }else if(position==2){
                    Intent measure= new Intent(HomeActivity.this,AddOrderActivity.class);
                    startActivity(measure);
                    return true;
                }else if(position==3){
                    getNotificationFragment();
                    return true;
                }
                return true;
            }
        });

    }

    public void getNotificationFragment() {
        getNotificationsFromServer();
    }

    private void initFragment(FragmentManager mFragmentManager, FragmentTransaction mFragmentTransaction, Fragment fragment, Parcelable parcelable, String key, String tag) {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        Bundle bundle=new Bundle();
        bundle.putParcelable(key, parcelable);
        fragment.setArguments(bundle);
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.home_container, fragment, tag);
        mFragmentTransaction.commit();
    }

    private int fetchColor(@ColorRes int color) {
        return ContextCompat.getColor(this, color);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
                Bundle bundle=new Bundle();
                measure.putExtra("init-key","first-init-add-order");
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
    public void getNotificationsFromServer() {
        String xAuthToken=getSessionIdFromPref();
        mHomePresenter.getNotificationsFromServer(xAuthToken);
    }

    @Override
    public int getNotificationCountFromPref() {
        if(sharedPreferenceHelper.containKey("notf-count")){
            return sharedPreferenceHelper.getIntegerPreference("notf-count",-1);
        }else{
            return -1;
        }

    }

    @Override
    @Subscribe
    public void deleteNotification(NotificationDetailModel notificationDetailModel) {
        String sessionId=getSessionIdFromPref();
        mHomePresenter.deleteNotification(sessionId,notificationDetailModel);
    }

    @Override
    public void deleteAllNotification() {
        String sessionId=getSessionIdFromPref();
        mHomePresenter.deleteAllNotification(sessionId);
    }

    @Override
    public void getNotifications(NotificationSummaryModel notificationSummaryModel) {
        mNotificationSummaryModel=notificationSummaryModel;
        initFragment(fragmentManager,fragmentTransaction,new HomeNotificationFragment(),mNotificationSummaryModel,ARG_NOTIFICATIONS,ARG_NOTIFICATIONS);
        updateNotificationCount(mNotificationSummaryModel);
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
    public void showAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }




    @Override
    public void navigateLogin() {
        startActivity(new Intent(HomeActivity.this,LoginActivity.class));
    }

    @Override
    public void showProgress(boolean isBaseView) {
        progressBarHome.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress(boolean isBaseView) {
        progressBarHome.setVisibility(View.GONE);
    }

    @Override
    public void updateNotifications(NotificationDetailModel notificationDetailModel,boolean isDeleteAll) {
        HomeNotificationFragment homeNotificationFragment= (HomeNotificationFragment) getSupportFragmentManager().findFragmentByTag(ARG_NOTIFICATIONS);
        if(homeNotificationFragment!=null){
            if(isDeleteAll){
                homeNotificationFragment.removeAllItemFromAdapter();
                mNotificationSummaryModel.getNotificationDetailModelList().clear();
                updateNotificationCount(mNotificationSummaryModel);
            }else{
                homeNotificationFragment.removeItemFromAdapter(notificationDetailModel);
                mNotificationSummaryModel.getNotificationDetailModelList().remove(notificationDetailModel);
                updateNotificationCount(mNotificationSummaryModel);
            }
        }
    }

    private void updateNotificationCount(NotificationSummaryModel notificationSummaryModel) {
        int count=notificationSummaryModel.getNotificationDetailModelList().size();
        HomeNotificationFragment homeNotificationFragment= (HomeNotificationFragment) getSupportFragmentManager().findFragmentByTag(ARG_NOTIFICATIONS);
        if(count>0){
            String notificationCount=String.valueOf(count);
            ahBottomNavigation.setNotification(notificationCount,3);
        }else{
            ahBottomNavigation.setNotification("",3);
            if(homeNotificationFragment!=null){
                homeNotificationFragment.setEmptyBackground();
            }

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
}
