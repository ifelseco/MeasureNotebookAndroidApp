package com.javaman.olcudefteri.orders.view;

import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;
import com.javaman.olcudefteri.orders.model.response.OrderSummaryReponseModel;

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
      void updateOrderFromAdapter(List<OrderDetailResponseModel> orders);
      void navigateToLogin();
}
