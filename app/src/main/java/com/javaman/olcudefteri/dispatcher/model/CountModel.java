package com.javaman.olcudefteri.dispatcher.model;

import com.javaman.olcudefteri.api.model.response.BaseModel;

import lombok.Data;

@Data
public class CountModel {
    private BaseModel baseModel;
    private int count;
}
