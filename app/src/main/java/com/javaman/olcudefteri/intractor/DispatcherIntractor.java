package com.javaman.olcudefteri.intractor;

import com.javaman.olcudefteri.model.AppUtilInfoModel;

public interface DispatcherIntractor {
    interface onNotificationProcessListener{
        void onSuccess(AppUtilInfoModel appUtilInfoModel);
        void onFailure(String message);
        void navigateToLogin();
    }

    void getAppUtilInfo(String headerData , onNotificationProcessListener listener);
}
