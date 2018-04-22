package com.javaman.olcudefteri.login.model.response;

import com.javaman.olcudefteri.api.model.response.BaseModel;

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
