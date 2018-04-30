package com.javaman.olcudefteri.orders.presenter;

import com.javaman.olcudefteri.orders.intractor.AddOrderLineIntractor;
import com.javaman.olcudefteri.orders.intractor.AddOrderLineIntractorImpl;
import com.javaman.olcudefteri.orders.model.AddOrderLineDetailListModel;
import com.javaman.olcudefteri.orders.model.DeleteOrderLinesModel;
import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;
import com.javaman.olcudefteri.orders.model.response.AddOrderLineListResponse;
import com.javaman.olcudefteri.orders.model.response.AddOrderLineResponse;
import com.javaman.olcudefteri.orders.model.response.CalculationResponse;
import com.javaman.olcudefteri.orders.view.AddOrderLineView;
import com.javaman.olcudefteri.orders.view.CalculateView;
import com.javaman.olcudefteri.orders.view.OrderLineView;

/**
 * Created by javaman on 15.02.2018.
 */

public class AddOrderLinePresenterImpl implements AddOrderLinePresenter,
        AddOrderLineIntractor.onAddOrderLineListener,
        AddOrderLineIntractor.onAddOrderLinesListener,
        AddOrderLineIntractor.onDeleteOrderLineListener,
        AddOrderLineIntractor.onDeleteOrderLinesListener,
        AddOrderLineIntractor.onCalculateOrderLineListener{

    AddOrderLineView mAddOrderLineView;
    OrderLineView mOrderLineView;
    CalculateView mCalculateView;
    AddOrderLineIntractor mAddOrderLineIntractor;

    public AddOrderLinePresenterImpl(AddOrderLineView addOrderLineView){
        this.mAddOrderLineView=addOrderLineView;
        mAddOrderLineIntractor=new AddOrderLineIntractorImpl();
    }

    public AddOrderLinePresenterImpl(CalculateView calculateView){
        this.mCalculateView=calculateView;
        mAddOrderLineIntractor=new AddOrderLineIntractorImpl();

    }

    public AddOrderLinePresenterImpl(OrderLineView mOrderLineView) {
        this.mOrderLineView = mOrderLineView;
        mAddOrderLineIntractor=new AddOrderLineIntractorImpl();
    }

    @Override
    public void addOrderLine(OrderLineDetailModel orderLineDetailModel, String headerData) {
        if(mAddOrderLineView!=null){
            //add
            mAddOrderLineView.showProgress();
            mAddOrderLineIntractor.addOrderLine(orderLineDetailModel,headerData,this);
        }else if(mOrderLineView!=null){
            //update
            mOrderLineView.showProgress("Ölçü güncelleniyor");
            mAddOrderLineIntractor.addOrderLine(orderLineDetailModel,headerData,this);
        }
    }

    @Override
    public void calculateOrderLine(AddOrderLineDetailListModel orderLineDetailListModel, String headerData) {
        if(mCalculateView!=null){
            mCalculateView.showProgress();
            mAddOrderLineIntractor.calculateOrderLine(orderLineDetailListModel,headerData,this);
        }
    }


    @Override
    public void addOrderLineList(AddOrderLineDetailListModel orderLineDetailListModel, String headerData) {
        if(mAddOrderLineView!=null){
            mAddOrderLineView.showProgress();
            mAddOrderLineIntractor.addOrderLines(orderLineDetailListModel,headerData,this);
        }
    }

    @Override
    public void deleteOrderLine(long id, String headerData) {

    }

    @Override
    public void deleteOrderLines(DeleteOrderLinesModel deleteOrderLinesModel, String headerData) {

    }

    @Override
    public void onDestroyAddOrderLine() {
        if(mAddOrderLineView!=null){
            mAddOrderLineView=null;
        }
    }

    @Override
    public void onDestroyCalculate() {
        if(mCalculateView!=null){
            mCalculateView=null;
        }
    }


    @Override
    public void onSuccessAddOrderLine(AddOrderLineResponse addOrderLineResponse,OrderLineDetailModel orderLineDetailModel) {
        if(mAddOrderLineView!=null){
            //add
            mAddOrderLineView.hideProgress();
            mAddOrderLineView.showAlert("Ölçü başarıyla kaydedildi.",false);
            mAddOrderLineView.updateCart(addOrderLineResponse.getOrderTotalAmount());
        }else if(mOrderLineView!=null){
            //update
            mOrderLineView.hideProgress();
            mOrderLineView.showAlert("Ölçü güncellendi",false,false);
            mOrderLineView.updateAdapter(orderLineDetailModel);
        }
    }

    @Override
    public void onFailureAddOrderLine(String message) {
        if(mAddOrderLineView!=null){
            mAddOrderLineView.hideProgress();
            mAddOrderLineView.showAlert(message,true);
        }else if(mOrderLineView!=null){
            mOrderLineView.hideProgress();
            mOrderLineView.showAlert(message,true,false);

        }
    }

    @Override
    public void onSuccessAddOrderLines(AddOrderLineListResponse addOrderLineListResponse) {
        if(mAddOrderLineView!=null){
            mAddOrderLineView.hideProgress();
            mAddOrderLineView.showAlert("Ölçü başarıyla kaydedildi.",false);
            mAddOrderLineView.updateCart(addOrderLineListResponse.getOrderTotalAmount());
        }
    }

    @Override
    public void onFailureAddOrderLines(String message) {
        if(mAddOrderLineView!=null){
            mAddOrderLineView.hideProgress();
            mAddOrderLineView.showAlert(message,true);
        }
    }

    @Override
    public void onSuccessDeleteOrderLine() {

    }

    @Override
    public void onFailureDeleteOrderLine(String message) {

    }

    @Override
    public void onSuccessDeleteOrderLines() {

    }

    @Override
    public void onFailureDeleteOrderLines(String message) {

    }


    @Override
    public void onSuccessCalculateOrderLines(CalculationResponse calculationResponse) {
        if(mCalculateView!=null){
            mCalculateView.hideProgress();
            mCalculateView.updateAmount(calculationResponse);
        }
    }

    @Override
    public void onFailureCalculateOrderLines(String message) {
        if(mCalculateView!=null){
            mCalculateView.hideProgress();
            mCalculateView.showAlert(message);
        }
    }
}
