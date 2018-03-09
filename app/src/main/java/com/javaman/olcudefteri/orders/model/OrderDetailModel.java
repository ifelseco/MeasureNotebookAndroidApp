package com.javaman.olcudefteri.orders.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import lombok.Data;

/**
 * Created by javaman on 07.03.2018.
 */

@Data
class OrderDetailModel {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("userUsername")
    @Expose
    private String userUsername;

    @SerializedName("orderDate")
    @Expose
    private Date orderDate;

    @SerializedName("totalAmount")
    @Expose
    private double totalAmount;

    @SerializedName("depositeAmount")
    @Expose
    private double depositeAmount;

    @SerializedName("deliveryDate")
    @Expose
    private Date deliveryDate;

    @SerializedName("mountDate")
    @Expose
    private Date mountDate;

    @SerializedName("measureDate")
    @Expose
    private Date measureDate;

    @SerializedName("orderStatus")
    @Expose
    private int orderStatus;

    @SerializedName("customer")
    @Expose
    private CustomerDetailModel customer;


}
