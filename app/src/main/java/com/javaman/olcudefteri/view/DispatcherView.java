package com.javaman.olcudefteri.view;

import com.javaman.olcudefteri.model.AppUtilInfoModel;

public interface DispatcherView extends BaseView{
    void getAppUtilInfoFromServer();
    void saveAppUtilInfoToPref(AppUtilInfoModel appUtilInfoModel);
    void redirect();
}
