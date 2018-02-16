package com.javaman.olcudefteri.api.response_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by javaman on 15.02.2018.
 */

public class AddCustomerResponse extends BaseResponse {


    private Long id;


    private Date orderDate;


    private Long customerId;


    private String customerNameSurname;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerNameSurname() {
        return customerNameSurname;
    }

    public void setCustomerNameSurname(String customerNameSurname) {
        this.customerNameSurname = customerNameSurname;
    }
}
