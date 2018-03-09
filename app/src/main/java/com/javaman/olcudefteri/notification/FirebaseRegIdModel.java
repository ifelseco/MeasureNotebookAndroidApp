package com.javaman.olcudefteri.notification;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by javaman on 19.02.2018.
 */

public class FirebaseRegIdModel {

    @Getter
    @Setter
    private String registrationId;

    public FirebaseRegIdModel(String registrationId){
        this.registrationId=registrationId;
    }


}
