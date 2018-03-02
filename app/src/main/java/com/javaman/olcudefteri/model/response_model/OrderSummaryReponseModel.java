package com.javaman.olcudefteri.model.response_model;

/**
 * Created by javaman on 26.02.2018.
 */

public class OrderSummaryReponseModel{

    private BaseResponse baseResponse;

    private OrderDetailPage orderDetailPage;

    public BaseResponse getBaseResponse() {
        return baseResponse;
    }

    public void setBaseResponse(BaseResponse baseResponse) {
        this.baseResponse = baseResponse;
    }

    public OrderDetailPage getOrderDetailPage() {
        return orderDetailPage;
    }

    public void setOrderDetailPage(OrderDetailPage orderDetailPage) {
        this.orderDetailPage = orderDetailPage;
    }
}
