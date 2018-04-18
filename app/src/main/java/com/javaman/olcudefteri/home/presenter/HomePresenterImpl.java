package com.javaman.olcudefteri.home.presenter;

import com.javaman.olcudefteri.home.intractor.HomeIntractor;
import com.javaman.olcudefteri.home.intractor.HomeIntractorImpl;
import com.javaman.olcudefteri.home.model.NotificationDetailModel;
import com.javaman.olcudefteri.home.model.NotificationSummaryModel;
import com.javaman.olcudefteri.home.view.HomeView;
import com.javaman.olcudefteri.notification.FirebaseRegIdModel;

/**
 * Created by javaman on 20.02.2018.
 */

public class HomePresenterImpl implements HomePresenter,HomeIntractor.onNotificationProcessListener {

    HomeView mHomeViev;
    HomeIntractor mHomeIntractor;

    public HomePresenterImpl(HomeView mHomeViev ){
        this.mHomeViev=mHomeViev;
        mHomeIntractor=new HomeIntractorImpl();
    }

    @Override
    public void sendFirebaseRegIdToServer(String xAuthToken , FirebaseRegIdModel regIdModel) {
        if(mHomeViev!=null){
            mHomeIntractor.sendFirebaseRegIdToServer(xAuthToken,regIdModel);
        }
    }

    @Override
    public void onDestroy() {
        if(mHomeViev!=null){
            mHomeViev=null;
        }
    }

    @Override
    public void getNotificationsFromServer(String xAuthToken) {
        if(mHomeViev!=null){
            mHomeViev.showProgress(true);
            mHomeIntractor.getNotifiationsFromServer(xAuthToken,this);
        }
    }

    @Override
    public void deleteNotification(String xAuthToken, NotificationDetailModel notificationDetailModel) {
        if(mHomeViev!=null){
            mHomeIntractor.deleteNotification(xAuthToken,notificationDetailModel,this);
        }
    }

    @Override
    public void deleteAllNotification(String xAuthToken) {
        if(mHomeViev!=null){
            mHomeViev.showProgress(true);
            mHomeIntractor.deleteAllNotification(xAuthToken,this);
        }
    }


    @Override
    public void onSuccessGetNotification(NotificationSummaryModel notificationSummaryModel) {
        if(mHomeViev!=null){
            mHomeViev.hideProgress(true);
            mHomeViev.getNotifications(notificationSummaryModel);
        }
    }

    @Override
    public void onFailureGetNotification(String message) {
        if(mHomeViev!=null){
            mHomeViev.hideProgress(true);
            mHomeViev.showAlert(message);
        }
    }

    @Override
    public void navigateToLogin() {
        if(mHomeViev!=null){
            mHomeViev.navigateLogin();
        }
    }

    @Override
    public void onSuccessDelete(NotificationDetailModel notificationDetailModel) {
        if(mHomeViev!=null){
            mHomeViev.updateNotifications(notificationDetailModel,false);
        }
    }

    @Override
    public void onFailureDelete(String message) {
        if(mHomeViev!=null){
            mHomeViev.hideProgress(true);
            mHomeViev.showAlert(message);
        }
    }

    @Override
    public void onSuccessDeleteAll(String message) {
        if(mHomeViev!=null){
            mHomeViev.hideProgress(true);
            mHomeViev.updateNotifications(null,true);
        }
    }

    @Override
    public void onFailureDeleteAll(String message) {
        if(mHomeViev!=null){
            mHomeViev.hideProgress(true);
            mHomeViev.showAlert(message);
        }
    }
}
