package com.javaman.olcudefteri.dispatcher.view;

import com.javaman.olcudefteri.base.BaseView;

public interface DispatcherView extends BaseView{
    void getNotificationCountFromServer();
    void saveNotfCountToPref(int count);
    void redirect();
}
