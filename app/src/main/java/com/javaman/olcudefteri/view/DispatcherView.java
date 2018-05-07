package com.javaman.olcudefteri.view;

import com.javaman.olcudefteri.view.BaseView;

public interface DispatcherView extends BaseView{
    void getNotificationCountFromServer();
    void saveNotfCountToPref(int count);
    void redirect();
}
