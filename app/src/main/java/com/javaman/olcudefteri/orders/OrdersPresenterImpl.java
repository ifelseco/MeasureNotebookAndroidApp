package com.javaman.olcudefteri.orders;



import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.javaman.olcudefteri.model.OrdersDeleteModel;
import com.javaman.olcudefteri.model.PageModel;
import com.javaman.olcudefteri.model.response_model.OrderDetailResponseModel;
import com.javaman.olcudefteri.model.response_model.OrderSummaryReponseModel;

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
            mOrdersView.getOrders(orderSummaryReponseModel);
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
}
