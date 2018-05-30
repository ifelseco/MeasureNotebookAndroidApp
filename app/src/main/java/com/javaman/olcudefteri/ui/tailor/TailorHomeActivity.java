package com.javaman.olcudefteri.ui.tailor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
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
import com.javaman.olcudefteri.model.AppUtilInfoModel;
import com.javaman.olcudefteri.presenter.BasePresenter;
import com.javaman.olcudefteri.presenter.impl.BasePresenterImpl;
import com.javaman.olcudefteri.view.BaseView;
import com.javaman.olcudefteri.ui.home.HomeNotificationFragment;
import com.javaman.olcudefteri.model.NotificationDetailModel;
import com.javaman.olcudefteri.model.NotificationSummaryModel;
import com.javaman.olcudefteri.presenter.HomePresenter;
import com.javaman.olcudefteri.presenter.impl.HomePresenterImpl;
import com.javaman.olcudefteri.ui.login.LoginActivity;
import com.javaman.olcudefteri.model.FirebaseRegIdModel;
import com.javaman.olcudefteri.utill.FirebaseUtil;
import com.javaman.olcudefteri.model.OrderDetailModel;
import com.javaman.olcudefteri.model.OrderUpdateModel;
import com.javaman.olcudefteri.model.OrderSummaryModel;
import com.javaman.olcudefteri.presenter.OrdersPresenter;
import com.javaman.olcudefteri.presenter.impl.OrdersPresenterImpl;
import com.javaman.olcudefteri.view.TailorView;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class TailorHomeActivity extends AppCompatActivity implements BaseView,TailorView, SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferenceHelper sharedPreferenceHelper;
    String text;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    boolean doubleBackToExitPressedOnce = false;

    private HomePresenter mHomePresenter;
    private BasePresenter mBasePresenter;
    private OrdersPresenter mOrdersPresenter;
    private NotificationSummaryModel mNotificationSummaryModel;
    SweetAlertDialog pDialog;
    public static final String ARG_NOTIFICATIONS = "tailor-home-notifications";
    public static final String ARG_TAILOR_ORDERS_PROCESSING = "processing_order";
    public static final String ARG_TAILOR_ORDERS_PROCESSED = "processed_order";
    @BindView(R.id.bottom_navigation)
    AHBottomNavigation ahBottomNavigation;

    @BindView(R.id.progress_bar_home)
    ProgressBar progressBarHome;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    OrderSummaryModel processingOrder;
    OrderSummaryModel processedOrder;
    int notfCount=0;
    int processingSize=0;
    int processedSize=0;
    private boolean clickForProcessesFragment;
    private String companyName;
    private String nameSurname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initview();
    }

    private void initview() {
        setContentView(R.layout.activity_tailor_home);
        ButterKnife.bind(this);
        FirebaseMessaging.getInstance().subscribeToTopic(FirebaseUtil.TOPIC_GLOBAL);
        sharedPreferenceHelper=new SharedPreferenceHelper(getApplicationContext());
        sharedPreferenceHelper.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        setSupportActionBar(toolbar);
        mHomePresenter=new HomePresenterImpl(this);
        mOrdersPresenter=new OrdersPresenterImpl(this);
        mBasePresenter=new BasePresenterImpl(this);
        getAppUtilInfoFromPref();
        initBottomNav();
        sendFirebaseRegIdToServer();
        getProcessingOrderFragment();
        getProcessedOrderFromServer();
        getAppUtilInfoFromServer();

    }

    private void setAppInfo() {
        toolbar.setTitle(companyName);
        toolbar.setSubtitle("Terzi: "+nameSurname);
    }

    public void getNotificationFragment() {
        getNotificationsFromServer();

    }

    public void getProcessingOrderFragment() {
        getProcessingOrderFromServer();

    }

    private void initFragment(FragmentManager mFragmentManager, FragmentTransaction mFragmentTransaction, Fragment fragment, Parcelable parcelable,String key, String tag) {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        Bundle bundle=new Bundle();
        bundle.putParcelable(key, parcelable);
        fragment.setArguments(bundle);
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.home_container, fragment, tag);
        mFragmentTransaction.commitAllowingStateLoss();
    }

    public void getProcessedOrderFragment() {
        clickForProcessesFragment=true;
        getProcessedOrderFromServer();
    }


    @Override
    public void getProcessedOrderFromServer() {
        String xAuthToken=getSessionIdFromPref();
        mOrdersPresenter.getTailorOrderWithFilter(xAuthToken,4);

    }

    @Override
    public void getProcessingOrderFromServer() {
        String xAuthToken=getSessionIdFromPref();
        mOrdersPresenter.getTailorOrderWithFilter(xAuthToken,3);
    }

    @Override
    public void getOrdersProcessing(OrderSummaryModel orderSummaryModel) {
        processingOrder= orderSummaryModel;
        processingSize=processingOrder.getOrders().size();
        updateOrderBadge(processingOrder.getOrders().size(),0);
        initFragment(fragmentManager,fragmentTransaction,new TailorOrderFragment(),processingOrder,ARG_TAILOR_ORDERS_PROCESSING,ARG_TAILOR_ORDERS_PROCESSING);

    }



    @Override
    public void getOrdersProcessed(OrderSummaryModel orderSummaryModel) {
        processedOrder= orderSummaryModel;
        processedSize=processedOrder.getOrders().size();
        updateOrderBadge(processedOrder.getOrders().size(),1);
        if(clickForProcessesFragment){
            initFragment(fragmentManager,fragmentTransaction,new TailorOrderFragment(), processedOrder, ARG_TAILOR_ORDERS_PROCESSED, ARG_TAILOR_ORDERS_PROCESSED);
            clickForProcessesFragment =false;
        }

    }

    @Override
    @Subscribe
    public void sendOrderUpdateRequest(OrderDetailModel orderDetailModel) {

        if(orderDetailModel.getId()!=null){

            OrderUpdateModel orderUpdateModel=new OrderUpdateModel();
            orderUpdateModel.setId(orderDetailModel.getId());
            orderUpdateModel.setOrderStatus(orderDetailModel.getOrderStatus());
            String headerData=getSessionIdFromPref();
            mOrdersPresenter.orderUpdate(orderUpdateModel,headerData);
        }

    }

    @Override
    public void updateList(final OrderUpdateModel orderUpdateModel) {



        if(orderUpdateModel.getOrderStatus()==4){
            processingSize--;
            processedSize++;
            updateOrderBadge(processingSize,0);
            updateOrderBadge(processedSize,1);
        }else if(orderUpdateModel.getOrderStatus()==3){
            processingSize++;
            processedSize--;
            updateOrderBadge(processingSize,0);
            updateOrderBadge(processedSize,1);
        }



        EventBus.getDefault().post(orderUpdateModel);

    }


    private void updateOrderBadge(int size, int index) {
        if(size>0){
            ahBottomNavigation.setNotification(""+size,index);
        }else{
            ahBottomNavigation.setNotification("",index);
        }
    }





    private void initBottomNav() {
        AHBottomNavigationItem item_processing = new AHBottomNavigationItem(R.string.title_processing, R.drawable.ic_tailor, R.color.hintColor);
        AHBottomNavigationItem item_processed = new AHBottomNavigationItem(R.string.title_processed, R.drawable.ic_check_circle_black_24dp, R.color.hintColor);
        AHBottomNavigationItem item_notification = new AHBottomNavigationItem(R.string.title_notifications, R.drawable.ic_notifications_black_24dp, R.color.hintColor);
        ahBottomNavigation.addItem(item_processing);
        ahBottomNavigation.addItem(item_processed);
        ahBottomNavigation.addItem(item_notification);
        ahBottomNavigation.setDefaultBackgroundColor(fetchColor(R.color.colorBottomNavBackground));
        ahBottomNavigation.setAccentColor(fetchColor(R.color.colorBottomNavActive));
        ahBottomNavigation.setInactiveColor(fetchColor(R.color.colorBottomNavInactive));
        ahBottomNavigation.setNotificationBackgroundColor(fetchColor(R.color.colorBottomNavNotification));
        ahBottomNavigation.setCurrentItem(0);
        ahBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        if(notfCount>0){
            ahBottomNavigation.setNotification(""+notfCount,2);
        }
        ahBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if(position==0){
                    getProcessingOrderFragment();
                    return true;
                }else if(position==1){
                    text="Biten işler";
                    getProcessedOrderFragment();
                    return true;
                }else if(position==2){
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
        if(!isFinishing()){
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText(message);
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    @Override
    public void hideProgress() {
        if(!isFinishing()){
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }

    @Override
    public void showAlert(String message) {
        StyleableToast.makeText(this,message,R.style.info_toast_style).show();
    }


    @Override
    public void navigateLogin() {
        if(!isFinishing()){
            startActivity(new Intent(TailorHomeActivity.this,LoginActivity.class));
        }
    }

    @Override
    public void showProgress(boolean isBaseView) {
        if(!isFinishing()){
            progressBarHome.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress(boolean isBaseView) {
        if(!isFinishing()){
            progressBarHome.setVisibility(View.GONE);
        }
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
        if(!isFinishing()){
            startActivity(new Intent(TailorHomeActivity.this,LoginActivity.class));
        }
    }

    @Override
    public void checkSession() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHomePresenter.onDestroy();
        mBasePresenter.onDestroy();
        if(pDialog!=null){
            hideProgress();
        }

        if(progressBarHome!=null){
            hideProgress(true);
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


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key=="notf-count"){
            notfCount=sharedPreferenceHelper.getIntegerPreference("notf-count",-1);
            if(notfCount>0){
                ahBottomNavigation.setNotification(""+notfCount,2);
            }
        }
    }
}
