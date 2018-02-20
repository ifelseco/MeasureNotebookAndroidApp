package com.javaman.olcudefteri.home;

import com.javaman.olcudefteri.model.FirebaseRegIdModel;

/**
 * Created by javaman on 20.02.2018.
 */

public interface HomePresenter {

    void sendFirebaseRegIdToServer(String xAuthToken , FirebaseRegIdModel regIdModel);
    void onDestroy();
}
