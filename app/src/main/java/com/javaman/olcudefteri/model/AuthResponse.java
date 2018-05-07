package com.javaman.olcudefteri.model;

import lombok.Data;

/**
 * Created by javaman on 08.02.2018.
 */

@Data
public class AuthResponse {

    private BaseModel baseModel;
    private String token;
    private UserDetailModel userDetailModel;


}
