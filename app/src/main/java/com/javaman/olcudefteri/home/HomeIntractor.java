package com.javaman.olcudefteri.home;

import com.javaman.olcudefteri.model.FirebaseRegIdModel;

/**
 * Created by javaman on 20.02.2018.
 */

public interface HomeIntractor {

    void sendFirebaseRegIdToServer(String xAuthToken , FirebaseRegIdModel regIdModel);
}
