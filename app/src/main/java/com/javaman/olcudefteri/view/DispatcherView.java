package com.javaman.olcudefteri.view;

import com.javaman.olcudefteri.model.AppUtilInfoModel;

public interface DispatcherView extends BaseView{
    void getNotificationCountFromServer();
    void saveAppUtilInfoToPref(AppUtilInfoModel appUtilInfoModel);
    void redirect();
}
