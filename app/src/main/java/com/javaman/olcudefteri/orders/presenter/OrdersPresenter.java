package com.javaman.olcudefteri.orders.presenter;

import com.javaman.olcudefteri.orders.model.OrderUpdateModel;
import com.javaman.olcudefteri.orders.model.PageModel;
import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;

import java.util.ArrayList;

/**
 * Created by javaman on 26.02.2018.
 */

public interface OrdersPresenter {
    void sendPageRequest(String xAuthToken , PageModel pageModel);
    void sendPageRequestWithFilter(String xAuthToken , int orderStatus , PageModel pageModel);
    void getTailorOrderWithFilter(String xAuthToken , int orderStatus );
    void sendDeleteOrderListRequest(String xAuthToken , ArrayList<OrderDetailResponseModel> orders);
    void onDestroy();
    void orderUpdate(OrderUpdateModel orderUpdateModel, String headerData);
    void orderSearch(String headerData,String orderNumber);
}
