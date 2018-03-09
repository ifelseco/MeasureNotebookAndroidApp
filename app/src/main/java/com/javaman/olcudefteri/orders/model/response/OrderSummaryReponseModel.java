package com.javaman.olcudefteri.orders.model.response;

import com.javaman.olcudefteri.api.model.response.BaseResponse;

import lombok.Data;

/**
 * Created by javaman on 26.02.2018.
 */

@Data
public class OrderSummaryReponseModel{

    private BaseResponse baseResponse;

    private OrderDetailPage orderDetailPage;


}
