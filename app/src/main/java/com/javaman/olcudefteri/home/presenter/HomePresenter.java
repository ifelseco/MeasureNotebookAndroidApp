package com.javaman.olcudefteri.home.presenter;

import com.javaman.olcudefteri.home.model.NotificationDetailModel;
import com.javaman.olcudefteri.notification.FirebaseRegIdModel;

/**
 * Created by javaman on 20.02.2018.
 */

public interface HomePresenter {

    void sendFirebaseRegIdToServer(String xAuthToken , FirebaseRegIdModel regIdModel);
    void onDestroy();
    void getNotificationsFromServer(String xAuthToken);
    void deleteNotification(String xAuthToken , NotificationDetailModel notificationDetailModel);
    void deleteAllNotification(String xAuthToken);
}
