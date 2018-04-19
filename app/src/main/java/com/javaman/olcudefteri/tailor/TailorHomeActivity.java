package com.javaman.olcudefteri.tailor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.javaman.olcudefteri.home.HomeActivity;
import com.javaman.olcudefteri.home.HomeNotificationFragment;
import com.javaman.olcudefteri.home.model.NotificationDetailModel;
import com.javaman.olcudefteri.home.model.NotificationSummaryModel;
import com.javaman.olcudefteri.home.presenter.HomePresenter;
import com.javaman.olcudefteri.home.presenter.HomePresenterImpl;
import com.javaman.olcudefteri.home.view.HomeView;
import com.javaman.olcudefteri.login.LoginActivity;
import com.javaman.olcudefteri.notification.FirebaseRegIdModel;
import com.javaman.olcudefteri.notification.FirebaseUtil;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class TailorHomeActivity extends AppCompatActivity implements HomeView,BaseView {

    SharedPreferenceHelper sharedPreferenceHelper;
    String text;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    boolean doubleBackToExitPressedOnce = false;
    private HomePresenter mHomePresenter;
    private BasePresenter mBasePresenter;
    private NotificationSummaryModel mNotificationSummaryModel;
    SweetAlertDialog pDialog;
    public static final String ARG_NOTIFICATIONS = "notifications";
    public static final String ARG_NOTIFICATION_FRAGMENT = "notification_fragment";
    @BindView(R.id.bottom_navigation)
    AHBottomNavigation ahBottomNavigation;

    @BindView(R.id.progress_bar_home)
    ProgressBar progressBarHome;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    public void getNotificationFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        HomeNotificationFragment homeNotificationFragment= new HomeNotificationFragment();
        Bundle bundle=new Bundle();
        bundle.putParcelable(ARG_NOTIFICATIONS,mNotificationSummaryModel);
        homeNotificationFragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_container, homeNotificationFragment, ARG_NOTIFICATION_FRAGMENT);
        fragmentTransaction.commit();
    }

    public void getOrderFragment(String text) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        TailorOrderFragment  tailorOrderFragment= new TailorOrderFragment();
        Bundle bundle=new Bundle();
        bundle.putString("text",text);
        tailorOrderFragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_container, tailorOrderFragment, "tailor-order-fragment");
        fragmentTransaction.commit();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initview();
        sendFirebaseRegIdToServer();
        getNotificationsFromServer();
    }

    private void initview() {
        setContentView(R.layout.activity_tailor_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        FirebaseMessaging.getInstance().subscribeToTopic(FirebaseUtil.TOPIC_GLOBAL);
        sharedPreferenceHelper=new SharedPreferenceHelper(getApplicationContext());
        mHomePresenter=new HomePresenterImpl(this);
        mBasePresenter=new BasePresenterImpl(this);
        initBottomNav();
        getOrderFragment("Devam eden işler");

    }

    private void initBottomNav() {
        AHBottomNavigationItem item_processing = new AHBottomNavigationItem(R.string.title_processing, R.drawable.ic_tailor, R.color.hintColor);
        AHBottomNavigationItem item_processed = new AHBottomNavigationItem(R.string.title_processed, R.drawable.ic_check_circle_black_24dp, R.color.hintColor);
        AHBottomNavigationItem item_notification = new AHBottomNavigationItem(R.string.title_notifications, R.drawable.ic_notifications_black_24dp, R.color.hintColor);
        ahBottomNavigation.addItem(item_processing);
        ahBottomNavigation.addItem(item_processed);
        ahBottomNavigation.addItem(item_notification);
        ahBottomNavigation.setDefaultBackgroundColor(fetchColor(R.color.colorAccentText));
        ahBottomNavigation.setAccentColor(fetchColor(R.color.yello));
        ahBottomNavigation.setInactiveColor(fetchColor(R.color.hintColor));
        ahBottomNavigation.setCurrentItem(0);
        ahBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ahBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if(position==0){
                    text="Devam eden işler";
                    getOrderFragment(text);
                    return true;
                }else if(position==1){
                    text="Biten işler";
                    getOrderFragment(text);
                    return true;
                }else if(position==2){
                    text="Bildirimler";
                    getNotificationFragment();
                    return true;
                }
                return true;
            }
        });

    }

    private int fetchColor(@ColorRes int color) {
        return ContextCompat.getColor(this, color);
    }

    @Override
    protected void onPause() {
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
        startActivity(new Intent(TailorHomeActivity.this,LoginActivity.class));
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
        HomeNotificationFragment homeNotificationFragment= (HomeNotificationFragment) getSupportFragmentManager().findFragmentByTag(ARG_NOTIFICATION_FRAGMENT);
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
        HomeNotificationFragment homeNotificationFragment= (HomeNotificationFragment) getSupportFragmentManager().findFragmentByTag(ARG_NOTIFICATION_FRAGMENT);
        if(count>0){
            String notificationCount=String.valueOf(count);
            ahBottomNavigation.setNotification(notificationCount,2);
        }else{
            ahBottomNavigation.setNotification("",2);
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
        startActivity(new Intent(TailorHomeActivity.this , LoginActivity.class));

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
