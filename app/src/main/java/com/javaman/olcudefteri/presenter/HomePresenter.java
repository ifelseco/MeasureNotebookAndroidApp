package com.javaman.olcudefteri.presenter;

import com.javaman.olcudefteri.model.NotificationDetailModel;
import com.javaman.olcudefteri.model.FirebaseRegIdModel;

/**
 * Created by javaman on 20.02.2018.
 */

public interface HomePresenter {

    void sendFirebaseRegIdToServer(String xAuthToken , FirebaseRegIdModel regIdModel);
    void onDestroy();
    void getNotificationsFromServer(String xAuthToken);
    void deleteNotification(String xAuthToken , NotificationDetailModel notificationDetailModel);
    void deleteAllNotification(String xAuthToken);
    void getAppUtilInfo(String headerData);
}
