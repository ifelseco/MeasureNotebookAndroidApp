package com.javaman.olcudefteri.orders.model.response;

import com.javaman.olcudefteri.api.model.response.BaseResponse;
import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Created by javaman on 07.03.2018.
 */

@Data
public class OrderLineSummaryResponseModel {

    private BaseResponse baseResponse;
    private OrderDetailResponseModel order;
    private List<OrderLineDetailModel> orderLineDetailList=new ArrayList<>();


}
