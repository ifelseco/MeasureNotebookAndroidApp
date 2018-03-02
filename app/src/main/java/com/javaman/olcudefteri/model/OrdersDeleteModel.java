package com.javaman.olcudefteri.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by javaman on 27.02.2018.
 */

public class OrdersDeleteModel {

    @SerializedName("orderIds")
    @Expose
    private ArrayList<Long> orderIds=new ArrayList<>();



    public ArrayList<Long> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(ArrayList<Long> orderIds) {
        this.orderIds = orderIds;
    }
}
