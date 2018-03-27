package com.javaman.olcudefteri.orders.presenter;

import com.javaman.olcudefteri.orders.intractor.AddOrderLineIntractor;
import com.javaman.olcudefteri.orders.intractor.AddOrderLineIntractorImpl;
import com.javaman.olcudefteri.orders.model.AddOrderLineDetailListModel;
import com.javaman.olcudefteri.orders.model.DeleteOrderLinesModel;
import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;
import com.javaman.olcudefteri.orders.model.response.AddOrderLineResponse;
import com.javaman.olcudefteri.orders.model.response.CalculationResponse;
import com.javaman.olcudefteri.orders.view.AddOrderLineView;
import com.javaman.olcudefteri.orders.view.CalculateView;

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

    @Override
    public void addOrderLine(OrderLineDetailModel orderLineDetailModel, String headerData) {
        if(mAddOrderLineView!=null){
            mAddOrderLineView.showProgress();
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
    public void onSuccessAddOrderLine(AddOrderLineResponse addOrderLineResponse) {
        if(mAddOrderLineView!=null){
            mAddOrderLineView.hideProgress();
            mAddOrderLineView.showAlert("Ölçü başarıyla kaydedildi.",false);
            mAddOrderLineView.updateCart(addOrderLineResponse);
        }
    }

    @Override
    public void onFailureAddOrderLine(String message) {
        if(mAddOrderLineView!=null){
            mAddOrderLineView.hideProgress();
            mAddOrderLineView.showAlert(message,true);
        }
    }

    @Override
    public void onSuccessAddOrderLines() {

    }

    @Override
    public void onFailureAddOrderLines(String message) {

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
