package com.javaman.olcudefteri.presenter.impl;

import com.javaman.olcudefteri.model.BaseModel;
import com.javaman.olcudefteri.intractor.OrderIntractor;
import com.javaman.olcudefteri.intractor.impl.OrderIntractorImpl;
import com.javaman.olcudefteri.model.OrderUpdateModel;
import com.javaman.olcudefteri.presenter.OrderPresenter;
import com.javaman.olcudefteri.view.AddOrderLineView;
import com.javaman.olcudefteri.view.OrderView;

public class OrderPresenterImpl implements OrderPresenter,OrderIntractor.onOrderProcessListener{

    AddOrderLineView mAddOrderLineView;
    OrderView mOrderVew;
    OrderIntractor mOrderIntractor;

    public OrderPresenterImpl(AddOrderLineView mAddOrderLineView) {
        this.mAddOrderLineView = mAddOrderLineView;
        mOrderIntractor=new OrderIntractorImpl();
    }

    public OrderPresenterImpl(OrderView mOrderVew) {
        this.mOrderVew = mOrderVew;
        mOrderIntractor=new OrderIntractorImpl();
    }

    @Override
    public void orderUpdate(OrderUpdateModel orderUpdateModel, String headerData) {
        if(mAddOrderLineView!=null){
            mAddOrderLineView.showProgress();
            mOrderIntractor.orderUpdate(headerData,orderUpdateModel,this);
        }else if(mOrderVew !=null){
            mOrderVew.showProgress("Sipariş güncelleniyor...");
            mOrderIntractor.orderUpdate(headerData,orderUpdateModel,this);
        }
    }

    @Override
    public void orderDelete(long id, String headerData) {
        if(mAddOrderLineView!=null){
            mAddOrderLineView.showProgress();
            mOrderIntractor.orderDelete(headerData,id,this);
        }else if(mOrderVew !=null){
            mOrderVew.showProgress("Sipariş siliniyor...");
            mOrderIntractor.orderDelete(headerData,id,this);
        }
    }

    @Override
    public void onDestroy() {
        if(mAddOrderLineView!=null){
            mAddOrderLineView=null;
        }else if(mOrderVew !=null){
            mOrderVew =null;
        }
    }

    @Override
    public void onSuccessUpdateOrder(BaseModel baseModel,OrderUpdateModel orderUpdateModel) {
        if(mAddOrderLineView!=null){
            mAddOrderLineView.hideProgress();
            mAddOrderLineView.updateView(orderUpdateModel);
        }else if(mOrderVew !=null){
            mOrderVew.hideProgress();
            mOrderVew.updateView(orderUpdateModel);
        }
    }

    @Override
    public void onFailureUpdateOrder(String message) {
        if(mAddOrderLineView!=null){
            mAddOrderLineView.hideProgress();
            mAddOrderLineView.showAlert(message,true);
        }else if(mOrderVew !=null){
            mOrderVew.hideProgress();
            mOrderVew.showAlert(message,true);
        }
    }


    @Override
    public void onSuccessDeleteOrder(String message) {
        if(mOrderVew !=null){
            mOrderVew.hideProgress();
            mOrderVew.showAlert(message,false);
            mOrderVew.navigateToOrders();
        }
    }

    @Override
    public void onFailureDeleteOrder(String message) {
        if(mOrderVew !=null){
            mOrderVew.hideProgress();
            mOrderVew.showAlert(message,true);
        }
    }



    @Override
    public void navigateToLogin() {
        if(mAddOrderLineView!=null){
            mAddOrderLineView.navigateToLogin();
        }else if(mOrderVew !=null){
            mOrderVew.navigateToLogin();
        }
    }
}
