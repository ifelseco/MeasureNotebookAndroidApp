package com.javaman.olcudefteri.intractor;

import com.javaman.olcudefteri.model.CountModel;

public interface DispatcherIntractor {
    interface onNotificationProcessListener{
        void onSuccess(CountModel countModel);
        void onFailure(String message);
        void navigateToLogin();
    }

    void getNotificationCountFromServer(String headerData , onNotificationProcessListener listener);
}
