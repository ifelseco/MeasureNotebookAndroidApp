package com.javaman.olcudefteri.view;

import com.javaman.olcudefteri.model.NotificationDetailModel;
import com.javaman.olcudefteri.model.NotificationSummaryModel;

/**
 * Created by javaman on 20.02.2018.
 */

public interface HomeView {

    void sendFirebaseRegIdToServer();
    void getNotificationsFromServer();
    void getAppUtilInfoFromPref();
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
