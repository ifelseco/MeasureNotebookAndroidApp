package com.javaman.olcudefteri.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;


import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.firebase.messaging.FirebaseMessaging;
import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.model.AppUtilInfoModel;
import com.javaman.olcudefteri.presenter.BasePresenter;
import com.javaman.olcudefteri.presenter.impl.BasePresenterImpl;
import com.javaman.olcudefteri.view.BaseView;
import com.javaman.olcudefteri.model.NotificationDetailModel;
import com.javaman.olcudefteri.model.NotificationSummaryModel;
import com.javaman.olcudefteri.presenter.HomePresenter;
import com.javaman.olcudefteri.presenter.impl.HomePresenterImpl;
import com.javaman.olcudefteri.view.HomeView;
import com.javaman.olcudefteri.ui.login.LoginActivity;
import com.javaman.olcudefteri.ui.orders.AddOrderActivity;
import com.javaman.olcudefteri.model.FirebaseRegIdModel;
import com.javaman.olcudefteri.utill.FirebaseUtil;
import com.javaman.olcudefteri.ui.orders.OrdersActivity;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,BaseView,HomeView, SharedPreferences.OnSharedPreferenceChangeListener {

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
    public static final String ARG_DASHBOARD = "dashboard";

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
    private String companyName;
    private String nameSurname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        sharedPreferenceHelper=new SharedPreferenceHelper(getApplicationContext());
        sharedPreferenceHelper.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        sharedPreferenceHelper.removeKey("orderLineSummaryResponse");
        sharedPreferenceHelper.removeKey("orderDetailResponse");


        FirebaseMessaging.getInstance().subscribeToTopic(FirebaseUtil.TOPIC_GLOBAL);

        mHomePresenter=new HomePresenterImpl(this);
        mBasePresenter=new BasePresenterImpl(this);
        getAppUtilInfoFromPref();
        initBottomNav();
        sendFirebaseRegIdToServer();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(R.id.home);
        navigationView.setNavigationItemSelectedListener(this);
        getAppUtilInfoFromServer();
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            if(bundle.containsKey("init-key")){
                getNotificationFragment();
                ahBottomNavigation.setCurrentItem(3);
            }else{
                getDashboardFragment();
            }

        }else{
            getDashboardFragment();
        }

    }

    private void setAppInfo() {
        View navHeader=navigationView.getHeaderView(0);
        TextView tvCompanyName=navHeader.findViewById(R.id.tv_company_name);
        TextView tvNameSurname=navHeader.findViewById(R.id.tv_name_surname);
        tvCompanyName.setText(companyName);
        tvNameSurname.setText(nameSurname);
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
        ahBottomNavigation.setAccentColor(fetchColor(R.color.secondaryDarkColor));
        ahBottomNavigation.setInactiveColor(fetchColor(R.color.primaryDarkColor));
        ahBottomNavigation.setNotificationBackgroundColor(fetchColor(R.color.secondaryDarkColor));
        ahBottomNavigation.setCurrentItem(0);
        ahBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ahBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        if(notfCount>0){
            ahBottomNavigation.setNotification(""+notfCount,3);
        }
        ahBottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {
            if(position==0){
                getDashboardFragment();
                return true;
            }else if(position==1){
                Intent orders= new Intent(HomeActivity.this,OrdersActivity.class);
                startActivity(orders);
                return true;
            }else if(position==2){
                Intent measure= new Intent(HomeActivity.this,AddOrderActivity.class);
                measure.putExtra("init-key","first-init-add-order");
                startActivity(measure);
                return true;
            }else if(position==3){
                getNotificationFragment();
                return true;
            }
            return true;
        });

    }

    private void getDashboardFragment() {
        initFragment(fragmentManager,fragmentTransaction,new DashboardFragment(),null,ARG_DASHBOARD,ARG_DASHBOARD);
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
        StyleableToast.makeText(this,"Çıkmak için tekrar basın.",R.style.info_toast_style).show();

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){

            case R.id.settings:

                Toast.makeText(this, "Ayarlar", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                logout();
                break;

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
    public void getAppUtilInfoFromServer() {
        String headerData=getSessionIdFromPref();
        mHomePresenter.getAppUtilInfo(headerData);
    }

    @Override
    public void saveAppUtilInfoToPref(AppUtilInfoModel appUtilInfoModel) {
        notfCount=appUtilInfoModel.getCount();
        companyName=appUtilInfoModel.getComapanyName();
        nameSurname=appUtilInfoModel.getUserNameSurname();
        setAppInfo();
        sharedPreferenceHelper.setIntegerPreference("notf-count", appUtilInfoModel.getCount());
        sharedPreferenceHelper.setStringPreference("company-name",appUtilInfoModel.getComapanyName());
        sharedPreferenceHelper.setStringPreference("name-surname",appUtilInfoModel.getUserNameSurname());

    }

    @Override
    public void getAppUtilInfoFromPref() {
        if(sharedPreferenceHelper.containKey("notf-count")){
            notfCount= sharedPreferenceHelper.getIntegerPreference("notf-count",-1);
        }else{
            notfCount= -1;
        }


        if(sharedPreferenceHelper.containKey("company-name")){
            companyName=sharedPreferenceHelper.getStringPreference("company-name","");
        }

        if(sharedPreferenceHelper.containKey("name-surname")){
            nameSurname=sharedPreferenceHelper.getStringPreference("name-surname","");
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
        StyleableToast.makeText(this,message,R.style.info_toast_style).show();

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
        StyleableToast.makeText(this,message,R.style.info_toast_style).show();
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
                sharedPreferenceHelper.setIntegerPreference("notf-count",0);
            }else{
                homeNotificationFragment.removeItemFromAdapter(notificationDetailModel);
                mNotificationSummaryModel.getNotificationDetailModelList().remove(notificationDetailModel);
                updateNotificationCount(mNotificationSummaryModel);

                int notf_count=sharedPreferenceHelper.getIntegerPreference("notf-count",-1);
                if(notf_count!=-1){
                    notf_count--;
                    sharedPreferenceHelper.setIntegerPreference("notf-count",notf_count);
                }
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
