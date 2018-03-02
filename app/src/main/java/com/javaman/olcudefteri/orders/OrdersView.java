package com.javaman.olcudefteri.orders;

import com.javaman.olcudefteri.model.PageModel;
import com.javaman.olcudefteri.model.response_model.OrderDetailResponseModel;
import com.javaman.olcudefteri.model.response_model.OrderSummaryReponseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javaman on 26.02.2018.
 */

public interface OrdersView {


      String getSessionIdFromPref();
      void checkSession();
      void showAlert(String message);
      void getOrders(OrderSummaryReponseModel orderSummaryReponseModel);
      void sendDeleteOrderListRequest(ArrayList<OrderDetailResponseModel> orders);
      void sendPageRequest(int first,int rows);
      void deleteOrdersFromAdapter(ArrayList<OrderDetailResponseModel> orders);

}
