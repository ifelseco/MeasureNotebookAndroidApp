package com.javaman.olcudefteri.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.Data;

/**
 * Created by javaman on 27.02.2018.
 */

@Data
public class OrdersDeleteModel {

    @SerializedName("orderIds")
    @Expose
    private ArrayList<Long> orderIds=new ArrayList<>();

}
