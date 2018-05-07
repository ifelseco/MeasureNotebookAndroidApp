package com.javaman.olcudefteri.model;

import com.javaman.olcudefteri.model.BaseModel;

import lombok.Data;

@Data
public class UserDetailModel {

    private BaseModel baseModel;
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String role;
    private String name;
    private String surname;
}
