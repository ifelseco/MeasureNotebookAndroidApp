package com.javaman.olcudefteri.login.model.response;

import com.javaman.olcudefteri.api.model.response.BaseModel;

import lombok.Data;

@Data
public class LoginUserModel {

    private BaseModel baseModel;
    private UserDetailModel userDetailModel;
    //sessionId
    private String token;





}
