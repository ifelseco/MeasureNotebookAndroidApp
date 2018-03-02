package com.javaman.olcudefteri.model.response_model;

import java.util.Date;

/**
 * Created by javaman on 15.02.2018.
 */

public class AddCustomerResponse{

    private BaseResponse baseResponse;

    private Long id;


    private Date orderDate;


    private Long customerId;


    private String customerNameSurname;

    public BaseResponse getBaseResponse() {
        return baseResponse;
    }

    public void setBaseResponse(BaseResponse baseResponse) {
        this.baseResponse = baseResponse;
    }

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
