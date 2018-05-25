package com.javaman.olcudefteri.presenter.impl;



import com.javaman.olcudefteri.model.BaseModel;
import com.javaman.olcudefteri.model.OrderDetailModel;
import com.javaman.olcudefteri.model.OrderUpdateModel;
import com.javaman.olcudefteri.model.PageModel;
import com.javaman.olcudefteri.model.OrderDetailResponseModel;
import com.javaman.olcudefteri.model.OrderSummaryModel;
import com.javaman.olcudefteri.model.OrderSummaryPageReponseModel;
import com.javaman.olcudefteri.intractor.OrdersIntractor;
import com.javaman.olcudefteri.intractor.impl.OrdersIntractorImpl;
import com.javaman.olcudefteri.presenter.OrdersPresenter;
import com.javaman.olcudefteri.view.OrdersView;
import com.javaman.olcudefteri.view.TailorView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javaman on 26.02.2018.
 */

public class OrdersPresenterImpl implements OrdersPresenter,
        OrdersIntractor.onGetOrdersFinishedListener,
        OrdersIntractor.onDeleteOrdersFinishedListener,
        OrdersIntractor.onGetFilterOrdersFinishedListener,
        OrdersIntractor.onGetTailorFilterOrdersFinishedListener,
        OrdersIntractor.onOrderProcessListener,
        OrdersIntractor.onSearchOrderFinishedListener{

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
    public void orderUpdate(OrderUpdateModel orderUpdateModel, String headerData) {
        if (mTailorView!=null){
            mTailorView.showProgress(true);
            mOrdersIntractor.orderUpdate(headerData,orderUpdateModel,this);
        }
    }

    @Override
    public void orderSearch(String headerData, String orderNumber) {
        if(mOrdersView!=null){
            mOrdersView.showProgress();
            mOrdersIntractor.orderSearch(headerData,orderNumber,this);
        }
    }

    @Override
    public void onSuccessGetOrders(OrderSummaryPageReponseModel orderSummaryPageReponseModel) {
        if(mOrdersView!=null){
            mOrdersView.hideProgress();
            mOrdersView.getOrders(orderSummaryPageReponseModel);
            //mOrdersView.updateOrderFromAdapter(orderSummaryPageReponseModel.getOrderDetailPage().getContent());
            if(orderSummaryPageReponseModel.getOrderDetailPage().getTotalElements()>0){
                //mOrdersView.showAlert("Siparişler başarıyla listelendi",false,true);
                mOrdersView.hideEmptyBacground();
            }else{
                //mOrdersView.showAlert("Kayıtlı siparişiniz yok.",false,false);
                mOrdersView.setEmptyBacground();
            }

        }
    }

    @Override
    public void onSuccessDeleteOrders(String message, ArrayList<OrderDetailResponseModel> orders) {
        if(mOrdersView!=null){
            mOrdersView.hideProgress();
            mOrdersView.showAlert(message,false,false);
            mOrdersView.deleteOrdersFromAdapter(orders);
        }
    }

    @Override
    public void onFailureDeleteOrders(String message) {
        if(mOrdersView!=null){
            mOrdersView.hideProgress();
            mOrdersView.showAlert(message,true,true);

        }
    }

    @Override
    public void onFailureGetOrders(String message) {
        if(mOrdersView!=null){
            mOrdersView.hideProgress();
            mOrdersView.showAlert(message,true,false);
        }
    }

    @Override
    public void onSuccessGetFilterOrders(OrderSummaryPageReponseModel orderSummaryPageReponseModel, int orderStatus) {
        if(mOrdersView!=null){
            mOrdersView.hideProgress();
            //mOrdersView.showAlert("Siparişler listlendi",false,true);
            mOrdersView.getOrders(orderSummaryPageReponseModel);

            if(orderSummaryPageReponseModel.getOrderDetailPage().getTotalElements()>0){
                //mOrdersView.showAlert("Siparişler başarıyla listelendi",false,true);
                mOrdersView.hideEmptyBacground();
            }else{
                //mOrdersView.showAlert("Kayıtlı siparişiniz yok.",false,false);
                mOrdersView.setEmptyBacground();
            }
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

            //mTailorView.showAlert("Siparişler listelendi");


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
    public void onSuccessSearchOder(OrderSummaryModel orderSummaryModel) {
        if(mOrdersView!=null){
            mOrdersView.hideProgress();
            if(orderSummaryModel.getOrders().size()>0){
                List<OrderDetailResponseModel> orders=new ArrayList<>();
                for (OrderDetailModel orderDetailModel:orderSummaryModel.getOrders()){
                    OrderDetailResponseModel orderDetailResponseModel=new OrderDetailResponseModel();
                    orderDetailResponseModel.setId(orderDetailModel.getId());
                    orderDetailResponseModel.setOrderStatus(orderDetailModel.getOrderStatus());
                    orderDetailResponseModel.setMountExist(orderDetailModel.isMountExsist());
                    orderDetailResponseModel.setMeasureDate(orderDetailModel.getMeasureDate());
                    orderDetailResponseModel.setDeliveryDate(orderDetailModel.getDeliveryDate());
                    orderDetailResponseModel.setDepositeAmount(orderDetailModel.getDepositeAmount());
                    orderDetailResponseModel.setTotalAmount(orderDetailModel.getTotalAmount());
                    orderDetailResponseModel.setCustomer(orderDetailModel.getCustomer());
                    orderDetailResponseModel.setOrderDate(orderDetailModel.getOrderDate());
                    orderDetailResponseModel.setOrderNumber(orderDetailModel.getOrderNumber());
                    orderDetailResponseModel.setUserNameSurname(orderDetailModel.getUserNameSurname());
                    orders.add(orderDetailResponseModel);
                }

                mOrdersView.updateOrderAfterSearch(orders);
                mOrdersView.hideEmptyBacground();
            }else{
                mOrdersView.setEmptyBacground();
            }
        }
    }

    @Override
    public void onFailureSearchOder(String message) {
        if(mOrdersView!=null){
            mOrdersView.hideProgress();
            mOrdersView.showAlert(message,true,true);
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

    @Override
    public void onSuccessUpdateOrder(BaseModel baseModel,OrderUpdateModel orderUpdateModel) {
        if(mTailorView!=null){
            mTailorView.hideProgress(true);
            mTailorView.updateList(orderUpdateModel);
        }
    }

    @Override
    public void onFailureUpdateOrder(String message) {
        if(mTailorView!=null){
            mTailorView.hideProgress(true);
            mTailorView.showAlert(message);
        }
    }
}
