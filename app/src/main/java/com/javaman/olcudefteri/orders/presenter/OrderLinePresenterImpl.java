package com.javaman.olcudefteri.orders.presenter;

import com.javaman.olcudefteri.orders.intractor.OrderLineIntractor;
import com.javaman.olcudefteri.orders.intractor.OrderLineIntractorImpl;
import com.javaman.olcudefteri.orders.intractor.OrdersIntractor;
import com.javaman.olcudefteri.orders.intractor.OrdersIntractorImpl;
import com.javaman.olcudefteri.orders.model.response.OrderLineSummaryResponseModel;
import com.javaman.olcudefteri.orders.view.OrderDetailVew;
import com.javaman.olcudefteri.orders.view.OrdersView;

/**
 * Created by javaman on 12.03.2018.
 */

public class OrderLinePresenterImpl implements OrderLinePresenter,OrderLineIntractor.onGetOrderLineFinishedListener {

    OrderDetailVew mOrderDetailVew;
    OrderLineIntractor mOrderLineIntractor;

    public OrderLinePresenterImpl(OrderDetailVew mOrderDetailVew) {
        this.mOrderDetailVew=mOrderDetailVew;
        mOrderLineIntractor= new OrderLineIntractorImpl();
    }

    @Override
    public void sendGetOrderLineRequest(String xAuthToken, Long orderId) {
        if(mOrderDetailVew!=null){
            mOrderDetailVew.showProgress();
            mOrderLineIntractor.sendGetOrderLineRequest(xAuthToken,orderId,this);
        }
    }

    @Override
    public void onDestroy() {
        if(mOrderDetailVew!=null){
            mOrderDetailVew=null;
        }
    }

    @Override
    public void onSuccess(OrderLineSummaryResponseModel orderLineSummaryReponseModel) {
        if(mOrderDetailVew!=null){
            mOrderDetailVew.hideProgress();
            mOrderDetailVew.getOrderLines(orderLineSummaryReponseModel);
        }
    }

    @Override
    public void onFailure(String message) {
        if(mOrderDetailVew!=null){
            mOrderDetailVew.showAlert(message);
        }
    }

    @Override
    public void navigateToLogin() {
        if(mOrderDetailVew!=null){
            mOrderDetailVew.navigateToLogin();
        }
    }
}
