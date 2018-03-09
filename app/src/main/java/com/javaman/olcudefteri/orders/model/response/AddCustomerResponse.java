package com.javaman.olcudefteri.orders.model.response;

import com.javaman.olcudefteri.api.model.response.BaseResponse;

import java.util.Date;

import lombok.Data;

/**
 * Created by javaman on 15.02.2018.
 */

@Data
public class AddCustomerResponse{

    private BaseResponse baseResponse;

    private Long id;


    private Date orderDate;


    private Long customerId;


    private String customerNameSurname;


}
