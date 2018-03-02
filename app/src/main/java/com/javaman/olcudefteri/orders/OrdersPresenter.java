package com.javaman.olcudefteri.orders;

import com.javaman.olcudefteri.model.OrdersDeleteModel;
import com.javaman.olcudefteri.model.PageModel;
import com.javaman.olcudefteri.model.response_model.OrderDetailResponseModel;

import java.util.ArrayList;

/**
 * Created by javaman on 26.02.2018.
 */

public interface OrdersPresenter {
    void sendPageRequest(String xAuthToken , PageModel pageModel);
    void sendDeleteOrderListRequest(String xAuthToken , ArrayList<OrderDetailResponseModel> orders);
    void onDestroy();
}
