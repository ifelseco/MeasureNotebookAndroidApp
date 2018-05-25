package com.javaman.olcudefteri.view;

import com.javaman.olcudefteri.model.AddCustomerResponse;
import com.javaman.olcudefteri.model.CustomerSummaryModel;
import com.javaman.olcudefteri.model.OrderSummaryModel;

public interface CustomerView extends BaseView {
    void searchCustomer(String text);
    void getSearchResult(CustomerSummaryModel customerSummaryModel);
    void navigateToOrder(AddCustomerResponse addCustomerResponse);
    void getCustomerOrders(OrderSummaryModel orderSummaryModel);
}
