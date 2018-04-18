package com.javaman.olcudefteri.orders.presenter;

import com.javaman.olcudefteri.api.model.response.BaseModel;
import com.javaman.olcudefteri.orders.intractor.OrderIntractor;
import com.javaman.olcudefteri.orders.intractor.OrderIntractorImpl;
import com.javaman.olcudefteri.orders.model.OrderUpdateModel;
import com.javaman.olcudefteri.orders.view.AddOrderLineView;
import com.javaman.olcudefteri.orders.view.OrderView;

public class OrderPresenterImpl implements OrderPresenter ,OrderIntractor.onOrderProcessListener{

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
