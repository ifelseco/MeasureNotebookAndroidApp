package com.javaman.olcudefteri.home.view;

/**
 * Created by javaman on 20.02.2018.
 */

public interface HomeView {

    void sendFirebaseRegIdToServer();
    String getSessionIdFromPref();
    String getFirebaseIdFromPref();
}
