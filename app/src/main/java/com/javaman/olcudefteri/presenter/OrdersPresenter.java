package com.javaman.olcudefteri.presenter;

import com.javaman.olcudefteri.model.OrderUpdateModel;
import com.javaman.olcudefteri.model.PageModel;
import com.javaman.olcudefteri.model.OrderDetailResponseModel;

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
