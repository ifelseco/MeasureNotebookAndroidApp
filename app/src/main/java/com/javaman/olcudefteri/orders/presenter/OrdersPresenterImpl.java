package com.javaman.olcudefteri.orders.presenter;



import com.javaman.olcudefteri.orders.model.PageModel;
import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;
import com.javaman.olcudefteri.orders.model.response.OrderSummaryModel;
import com.javaman.olcudefteri.orders.model.response.OrderSummaryPageReponseModel;
import com.javaman.olcudefteri.orders.intractor.OrdersIntractor;
import com.javaman.olcudefteri.orders.intractor.OrdersIntractorImpl;
import com.javaman.olcudefteri.orders.view.OrdersView;
import com.javaman.olcudefteri.tailor.view.TailorView;

import java.util.ArrayList;

/**
 * Created by javaman on 26.02.2018.
 */

public class OrdersPresenterImpl implements OrdersPresenter ,
        OrdersIntractor.onGetOrdersFinishedListener,
        OrdersIntractor.onDeleteOrdersFinishedListener,
        OrdersIntractor.onGetFilterOrdersFinishedListener,
        OrdersIntractor.onGetTailorFilterOrdersFinishedListener{

    OrdersView mOrdersView;
    TailorView mTailorView;
    OrdersIntractor mOrdersIntractor;

    public OrdersPresenterImpl(OrdersView mOrdersView){
        this.mOrdersView=mOrdersView;
        mOrdersIntractor=new OrdersIntractorImpl();
    }

    public OrdersPresenterImpl(TailorView tailorView) {
        this.mTailorView=tailorView;
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
    public void sendPageRequestWithFilter(String xAuthToken, int orderStatus, PageModel pageModel) {
        if(mOrdersView!=null){
            mOrdersView.showProgress();
            mOrdersIntractor.sendPageRequestWithFilter(xAuthToken,orderStatus,pageModel,this);
        }else if(mTailorView!=null){
            mTailorView.showProgress(true);
            mOrdersIntractor.sendPageRequestWithFilter(xAuthToken,orderStatus,pageModel,this);
        }
    }

    @Override
    public void getTailorOrderWithFilter(String xAuthToken, int orderStatus) {
        if(mTailorView!=null){
            mTailorView.showProgress(true);
            mOrdersIntractor.getTailorOrdersWithFilter(xAuthToken,orderStatus,this);
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
    public void onSuccessGetOrders(OrderSummaryPageReponseModel orderSummaryPageReponseModel) {
        if(mOrdersView!=null){
            mOrdersView.hideProgress();
            mOrdersView.getOrders(orderSummaryPageReponseModel);
            //mOrdersView.updateOrderFromAdapter(orderSummaryPageReponseModel.getOrderDetailPage().getContent());
            if(orderSummaryPageReponseModel.getOrderDetailPage().getTotalElements()>0){
                mOrdersView.showAlert("Siparişler başarıyla listelendi",false,true);
            }else{
                mOrdersView.showAlert("Kayıtlı siparişiniz yok.",false,false);
            }

        }
    }

    @Override
    public void onSuccessDeleteOrders(String message, ArrayList<OrderDetailResponseModel> orders) {
        if(mOrdersView!=null){
            mOrdersView.showAlert(message,false,false);
            mOrdersView.deleteOrdersFromAdapter(orders);
        }
    }

    @Override
    public void onFailureDeleteOrders(String message) {
        if(mOrdersView!=null){
            mOrdersView.showAlert(message,true,true);

        }
    }

    @Override
    public void onFailureGetOrders(String message) {
        if(mOrdersView!=null){
            mOrdersView.showAlert(message,true,false);
        }
    }

    @Override
    public void onSuccessGetFilterOrders(OrderSummaryPageReponseModel orderSummaryPageReponseModel, int orderStatus) {
        if(mOrdersView!=null){
            mOrdersView.hideProgress();
            mOrdersView.showAlert("Siparişler listlendi",false,true);
            mOrdersView.getOrders(orderSummaryPageReponseModel);
        }
    }

    @Override
    public void onFailureGetFilterOrders(String message, int orderStatus) {
        if(mOrdersView!=null){
            mOrdersView.hideProgress();
            mOrdersView.showAlert(message,true,true);

        }
    }

    @Override
    public void onSuccessGetTailorFilterOrders(OrderSummaryModel orderSummaryModel, int orderStatus) {
        if(mTailorView!=null){
            mTailorView.hideProgress(true);

            if(orderStatus==3){

                mTailorView.getOrdersProcessing(orderSummaryModel);

            }else if(orderStatus==4){
                mTailorView.getOrdersProcessed(orderSummaryModel);
            }

            mTailorView.showAlert("Siparişler listelendi");


        }
    }

    @Override
    public void onFailureGetTailorFilterOrders(String message) {
        if(mTailorView!=null){
            mTailorView.hideProgress(true);
            mTailorView.showAlert(message);

        }
    }

    @Override
    public void navigateToLogin() {
        if(mOrdersView!=null){
            mOrdersView.navigateToLogin();
        }else if(mTailorView!=null){
            mTailorView.navigateLogin();
        }
    }
}
