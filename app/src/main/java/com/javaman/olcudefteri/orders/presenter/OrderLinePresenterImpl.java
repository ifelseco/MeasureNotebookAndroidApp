package com.javaman.olcudefteri.orders.presenter;

import com.javaman.olcudefteri.orders.intractor.OrderLineIntractor;
import com.javaman.olcudefteri.orders.intractor.OrderLineIntractorImpl;
import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;
import com.javaman.olcudefteri.orders.model.response.OrderLineSummaryResponseModel;
import com.javaman.olcudefteri.orders.view.OrderDetailVew;
import com.javaman.olcudefteri.orders.view.OrderLineView;

/**
 * Created by javaman on 12.03.2018.
 */

public class OrderLinePresenterImpl implements OrderLinePresenter,OrderLineIntractor.onOrderLineProcessListener {

    OrderDetailVew mOrderDetailVew;
    OrderLineView mOrderLineView;
    OrderLineIntractor mOrderLineIntractor;

    public OrderLinePresenterImpl(OrderDetailVew mOrderDetailVew) {
        this.mOrderDetailVew=mOrderDetailVew;
        mOrderLineIntractor= new OrderLineIntractorImpl();
    }

    public OrderLinePresenterImpl(OrderLineView mOrderLineView) {
        this.mOrderLineView = mOrderLineView;
        mOrderLineIntractor=new OrderLineIntractorImpl();
    }

    @Override
    public void sendGetOrderLineRequest(String xAuthToken, Long orderId) {
        if(mOrderDetailVew!=null){
            mOrderDetailVew.showProgress();
            mOrderLineIntractor.sendGetOrderLineRequest(xAuthToken,orderId,this);
        }
    }

    @Override
    public void deleteOrderLine(String xAuthToken, OrderLineDetailModel orderLineDetailModel) {
        if(mOrderLineView!=null){
            mOrderLineView.showProgress("Ölçü siliniyor");
            mOrderLineIntractor.deleteOrderLine(xAuthToken,orderLineDetailModel,this);
        }

    }

    @Override
    public void onDestroy() {
        if(mOrderDetailVew!=null){
            mOrderDetailVew=null;
        }
    }

    @Override
    public void onSuccessGetOrderLines(OrderLineSummaryResponseModel orderLineSummaryReponseModel) {
        if(mOrderDetailVew!=null){
            mOrderDetailVew.hideProgress();
            mOrderDetailVew.getOrderLines(orderLineSummaryReponseModel);
        }
    }

    @Override
    public void onFailureGetOrderLines(String message) {
        if(mOrderDetailVew!=null){
            mOrderDetailVew.showAlert(message);
        }
    }

    @Override
    public void onSuccessDeleteOrderLine(OrderLineDetailModel orderLineDetailModel, String message) {
        if(mOrderLineView!=null){
            mOrderLineView.hideProgress();
            mOrderLineView.updateView(orderLineDetailModel);
            mOrderLineView.showAlert(message,false,true);
        }
    }

    @Override
    public void onFailureDeleteOrderLine(String message) {
        if(mOrderLineView!=null){
            mOrderLineView.showAlert(message,true,false);
        }
    }

    @Override
    public void navigateToLogin() {
        if(mOrderDetailVew!=null){
            mOrderDetailVew.navigateToLogin();
        }else if(mOrderLineView!=null){
            mOrderLineView.navigateToLogin();
        }
    }
}
