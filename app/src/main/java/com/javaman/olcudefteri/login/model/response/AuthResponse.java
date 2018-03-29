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
