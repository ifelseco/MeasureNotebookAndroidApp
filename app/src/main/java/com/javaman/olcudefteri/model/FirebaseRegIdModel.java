package com.javaman.olcudefteri.model;

/**
 * Created by javaman on 19.02.2018.
 */

public class FirebaseRegIdModel {

    private String registrationId;

    public FirebaseRegIdModel(String registrationId){
        this.registrationId=registrationId;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }
}
