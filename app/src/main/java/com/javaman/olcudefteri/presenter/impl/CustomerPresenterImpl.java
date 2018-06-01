package com.javaman.olcudefteri.presenter.impl;

import com.javaman.olcudefteri.intractor.CustomerIntractor;
import com.javaman.olcudefteri.intractor.impl.CustomerIntractorImpl;
import com.javaman.olcudefteri.model.CustomerDetailModel;
import com.javaman.olcudefteri.model.CustomerSummaryModel;
import com.javaman.olcudefteri.model.OrderSummaryModel;
import com.javaman.olcudefteri.presenter.CustomerPresenter;
import com.javaman.olcudefteri.view.AddOrderView;
import com.javaman.olcudefteri.view.CustomerView;

public class CustomerPresenterImpl implements CustomerPresenter,
        CustomerIntractor.onCustomerSearchListener,
        CustomerIntractor.onCustomerOrdersListener,
        CustomerIntractor.onCustomerUpdateListener{

    CustomerView customerView;
    AddOrderView addOrderView;
    CustomerIntractor customerIntractor;

    public CustomerPresenterImpl(CustomerView customerView) {
        this.customerView = customerView;
        customerIntractor = new CustomerIntractorImpl();
    }

    public CustomerPresenterImpl(AddOrderView addOrderView) {
        this.addOrderView = addOrderView;
        customerIntractor = new CustomerIntractorImpl();
    }

    @Override
    public void customerSearch(String headerData, String text) {
        if(customerView!=null){
            customerView.showProgress("Müşteri aranıyor...");
            customerIntractor.customerSearch(headerData,text,this);
        }
    }

    @Override
    public void getCustomerOrders(String headerData, long customerId) {
        if(customerView!=null){
            customerView.showProgress("Siparişler listeleniyor...");
            customerIntractor.getCustomerOrders(headerData,customerId,this);
        }
    }

    @Override
    public void updateCustomer(CustomerDetailModel customerDetailModel, String headerData) {
        if(addOrderView!=null){
            addOrderView.showProgress();
            customerIntractor.updateCustomer(customerDetailModel,headerData,this);
        }
    }

    @Override
    public void onSuccessCustomnerSearch(CustomerSummaryModel customerSummaryModel) {
        if(customerView!=null){
            customerView.hideProgress();
            customerView.getSearchResult(customerSummaryModel);
        }
    }

    @Override
    public void onFailureSearchCustomer(String message) {
        if(customerView!=null){
            customerView.hideProgress();
            customerView.showAlert(message,true);
        }
    }

    @Override
    public void onSuccessCustomnerOrders(OrderSummaryModel orderSummaryModel) {
        if(customerView!=null){
            customerView.hideProgress();
            customerView.getCustomerOrders(orderSummaryModel);
        }
    }

    @Override
    public void onFailureCustomnerOrders(String message) {
        if(customerView!=null){
            customerView.hideProgress();
        }
    }

    @Override
    public void onSuccessCustomnerUpdate(CustomerDetailModel customerDetailModel) {
        if(addOrderView!=null){
            addOrderView.hideProgress();
            addOrderView.updateData(customerDetailModel);
        }
    }

    @Override
    public void onFailureCustomnerUpdate(String message) {
        if(addOrderView!=null){
            addOrderView.hideProgress();
        }
    }

    @Override
    public void navigateLogin() {
        if(customerView!=null){
            customerView.navigateToLogin();
        }
    }



    @Override
    public void onDestroy() {
        if(customerView!=null){
            customerView=null;
        }
    }

    @Override
    public void onNameEmptyError() {
        if(addOrderView!=null){
            addOrderView.hideProgress();
            addOrderView.setNameEmptyError();
        }
    }

    @Override
    public void onPhoneEmptyError() {
        if(addOrderView!=null){
            addOrderView.hideProgress();
            addOrderView.setPhoneEmptyError();
        }
    }

    @Override
    public void onPhoneFormatError(boolean isMobile,boolean isFixed) {
        if(addOrderView!=null){
            addOrderView.hideProgress();
            addOrderView.setPhoneFormatError(isMobile,isFixed);
        }
    }


}
