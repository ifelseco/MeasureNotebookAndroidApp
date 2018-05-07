package com.javaman.olcudefteri.intractor;

import com.javaman.olcudefteri.model.NotificationDetailModel;
import com.javaman.olcudefteri.model.NotificationSummaryModel;
import com.javaman.olcudefteri.model.FirebaseRegIdModel;

/**
 * Created by javaman on 20.02.2018.
 */

public interface HomeIntractor {



    interface onNotificationProcessListener{
        void onSuccessGetNotification(NotificationSummaryModel notificationSummaryModel);
        void onFailureGetNotification(String message);
        void navigateToLogin();
        void onSuccessDelete(NotificationDetailModel notificationDetailModel);
        void onFailureDelete(String message);
        void onSuccessDeleteAll(String message);
        void onFailureDeleteAll(String message);
    }

    void sendFirebaseRegIdToServer(String xAuthToken , FirebaseRegIdModel regIdModel);

    void getNotifiationsFromServer(String xAuthToken,onNotificationProcessListener listener);

    void deleteNotification(String xAuthToken,NotificationDetailModel notificationDetailModel,onNotificationProcessListener listener);
    void deleteAllNotification(String xAuthToken,onNotificationProcessListener listener);
}
