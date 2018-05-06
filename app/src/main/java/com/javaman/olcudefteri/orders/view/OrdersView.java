package com.javaman.olcudefteri.orders.view;

import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;
import com.javaman.olcudefteri.orders.model.response.OrderSummaryPageReponseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javaman on 26.02.2018.
 */

public interface OrdersView {

      void getOrders(OrderSummaryPageReponseModel orderSummaryPageReponseModel);
      void sendDeleteOrderListRequest(ArrayList<OrderDetailResponseModel> orders);
      void sendPageRequest(int first,int rows);
      void sendPageRequestWithFilter(int first,int rows,int orderStatus);
      void deleteOrdersFromAdapter(ArrayList<OrderDetailResponseModel> orders);
      void updateOrderAfterSearch(List<OrderDetailResponseModel> orders);
      void orderSearch(String query);
      void showProgress();
      void hideProgress();
      String getSessionIdFromPref();
      void navigateToLogin();
      void checkSession();
      void showAlert(String message,boolean isError,boolean isOnlyToast);
      void setEmptyBacground();
      void hideEmptyBacground();

}
