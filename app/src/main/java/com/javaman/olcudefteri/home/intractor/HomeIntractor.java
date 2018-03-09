package com.javaman.olcudefteri.home.intractor;

import com.javaman.olcudefteri.notification.FirebaseRegIdModel;

/**
 * Created by javaman on 20.02.2018.
 */

public interface HomeIntractor {

    void sendFirebaseRegIdToServer(String xAuthToken , FirebaseRegIdModel regIdModel);
}
