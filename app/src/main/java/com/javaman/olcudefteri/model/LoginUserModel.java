package com.javaman.olcudefteri.model;

import lombok.Data;

@Data
public class LoginUserModel {

    private BaseModel baseModel;
    private UserDetailModel userDetailModel;
    //sessionId
    private String token;





}
