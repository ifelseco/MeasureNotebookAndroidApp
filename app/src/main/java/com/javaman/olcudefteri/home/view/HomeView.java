package com.javaman.olcudefteri.home.view;

import com.javaman.olcudefteri.home.model.NotificationDetailModel;
import com.javaman.olcudefteri.home.model.NotificationSummaryModel;

/**
 * Created by javaman on 20.02.2018.
 */

public interface HomeView {

    void sendFirebaseRegIdToServer();
    void getNotificationsFromServer();
    void deleteNotification(NotificationDetailModel notificationDetailModel);
    void deleteAllNotification();
    void getNotifications(NotificationSummaryModel notificationSummaryModel);
    String getFirebaseIdFromPref();
    void showAlert(String message);
    void navigateLogin();
    void showProgress(boolean isBaseView);
    void hideProgress(boolean isBaseView);
    void updateNotifications(NotificationDetailModel notificationDetailModel,boolean isDeleteAll);
}
