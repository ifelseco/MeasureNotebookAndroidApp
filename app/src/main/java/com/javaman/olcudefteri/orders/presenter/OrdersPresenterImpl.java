package com.javaman.olcudefteri.orders.presenter;



import android.support.v7.widget.RecyclerView;

import com.javaman.olcudefteri.base.BaseView;
import com.javaman.olcudefteri.home.view.HomeView;
import com.javaman.olcudefteri.orders.model.PageModel;
import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;
import com.javaman.olcudefteri.orders.model.response.OrderSummaryReponseModel;
import com.javaman.olcudefteri.orders.intractor.OrdersIntractor;
import com.javaman.olcudefteri.orders.intractor.OrdersIntractorImpl;
import com.javaman.olcudefteri.orders.view.OrdersView;
import com.javaman.olcudefteri.tailor.TailorHomeActivity;
import com.javaman.olcudefteri.tailor.view.TailorView;

import java.util.ArrayList;

/**
 * Created by javaman on 26.02.2018.
 */

public class OrdersPresenterImpl implements OrdersPresenter ,OrdersIntractor.onGetOrdersFinishedListener,OrdersIntractor.onDeleteOrdersFinishedListener,OrdersIntractor.onGetFilterOrdersFinishedListener{

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
    public void onSuccessGetOrders(OrderSummaryReponseModel orderSummaryReponseModel) {
        if(mOrdersView!=null){
            mOrdersView.hideProgress();
            mOrdersView.getOrders(orderSummaryReponseModel);
            //mOrdersView.updateOrderFromAdapter(orderSummaryReponseModel.getOrderDetailPage().getContent());
            if(orderSummaryReponseModel.getOrderDetailPage().getTotalElements()>0){
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
    public void onSuccessGetFilterOrders(OrderSummaryReponseModel orderSummaryReponseModel, int orderStatus) {
        if(mOrdersView!=null){
            mOrdersView.hideProgress();
            mOrdersView.showAlert("Siparişler listlendi",false,true);
            mOrdersView.getOrders(orderSummaryReponseModel);
        }else if(mTailorView!=null){
            mTailorView.hideProgress(true);

            if(orderStatus==3){

                mTailorView.getOrdersProcessing(orderSummaryReponseModel);

            }else if(orderStatus==4){
                mTailorView.getOrdersProcessed(orderSummaryReponseModel);
            }

            mTailorView.showAlert("Siparişler listelendi");


        }
    }

    @Override
    public void onFailureGetFilterOrders(String message, int orderStatus) {
        if(mOrdersView!=null){
            mOrdersView.hideProgress();
            mOrdersView.showAlert("Siparişler listelenirken hata oluştu",true,true);

        }else if(mTailorView!=null){
            mTailorView.hideProgress(true);
            mTailorView.showAlert("Siparişler listelenirken hata oluştu");

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
