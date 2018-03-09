package com.javaman.olcudefteri.orders.presenter;



import android.support.v7.widget.RecyclerView;

import com.javaman.olcudefteri.orders.model.PageModel;
import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;
import com.javaman.olcudefteri.orders.model.response.OrderSummaryReponseModel;
import com.javaman.olcudefteri.orders.intractor.OrdersIntractor;
import com.javaman.olcudefteri.orders.intractor.OrdersIntractorImpl;
import com.javaman.olcudefteri.orders.view.OrdersView;

import java.util.ArrayList;

/**
 * Created by javaman on 26.02.2018.
 */

public class OrdersPresenterImpl implements OrdersPresenter ,OrdersIntractor.onGetOrdersFinishedListener,OrdersIntractor.onDeleteOrdersFinishedListener{

    OrdersView mOrdersView;
    OrdersIntractor mOrdersIntractor;
    RecyclerView.Adapter adapter;

    public OrdersPresenterImpl(OrdersView mOrdersView){
        this.mOrdersView=mOrdersView;
        mOrdersIntractor=new OrdersIntractorImpl();
    }


    @Override
    public void sendPageRequest(String xAuthToken, PageModel pageModel) {
        if(mOrdersView!=null){
            mOrdersView.showProgress();
            mOrdersIntractor.sendPageRequestToServer(xAuthToken,pageModel,this);
        }
    }

    @Override
    public void sendDeleteOrderListRequest(String xAuthToken, ArrayList<OrderDetailResponseModel> orders) {
        if(mOrdersView!=null){
            mOrdersIntractor.sendDeleteOrderListRequestToServer(xAuthToken,orders,this);
        }
    }

    @Override
    public void onDestroy() {
        if(mOrdersView!=null){
            mOrdersView=null;
        }
    }

    @Override
    public void onSuccess(OrderSummaryReponseModel orderSummaryReponseModel) {
        if(mOrdersView!=null){
            mOrdersView.hideProgress();
            mOrdersView.getOrders(orderSummaryReponseModel);
            //mOrdersView.updateOrderFromAdapter(orderSummaryReponseModel.getOrderDetailPage().getContent());
            mOrdersView.showAlert("Siparişler başarıyla listelendi");
        }
    }

    @Override
    public void onSuccess(String message,ArrayList<OrderDetailResponseModel> orders) {
        if(mOrdersView!=null){
            mOrdersView.showAlert(message);
            mOrdersView.deleteOrdersFromAdapter(orders);

        }
    }

    @Override
    public void onFailure(String message) {
        if(mOrdersView!=null){
            mOrdersView.showAlert(message);
        }
    }

    @Override
    public void navigateToLogin() {
        if(mOrdersView!=null){
            mOrdersView.navigateToLogin();
        }
    }
}
