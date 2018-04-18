package com.javaman.olcudefteri.login.model.response;

import com.javaman.olcudefteri.api.model.response.BaseModel;

import lombok.Data;

@Data
public class LoginUserModel {

    private BaseModel baseModel;
    private String token;
    private String userName;
    private String role;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String companyName;
    private String companyPhone;
    private String imageUrl;


}
