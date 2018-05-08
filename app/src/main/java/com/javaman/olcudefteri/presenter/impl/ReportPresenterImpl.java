package com.javaman.olcudefteri.presenter.impl;

import com.javaman.olcudefteri.intractor.ReportIntractor;
import com.javaman.olcudefteri.intractor.impl.ReportIntractorImpl;
import com.javaman.olcudefteri.model.OrderSummaryModel;
import com.javaman.olcudefteri.presenter.ReportPresenter;
import com.javaman.olcudefteri.view.ReportView;

public class ReportPresenterImpl implements ReportPresenter,ReportIntractor.onReportProcess {

    ReportView mReportView;
    ReportIntractor mReportIntractor;

    public ReportPresenterImpl(ReportView reportView) {
        this.mReportView = reportView;
        mReportIntractor=new ReportIntractorImpl();
    }

    @Override
    public void getEndOfDay(String headerData) {
        if(mReportView!=null){
            mReportView.showProgress("Gün sonu yükleniyor");
            mReportIntractor.getEndOfDay(headerData,this);
        }
    }

    @Override
    public void getNextMeasure(String headerData) {
        if(mReportView!=null){
            mReportView.showProgress("Gelecek ölçüler yükleniyor");
            mReportIntractor.getNextMeasure(headerData,this);
        }
    }

    @Override
    public void getNextDelivery(String headerData) {
        if(mReportView!=null){
            mReportView.showProgress("Gelecek teslimatlar yükleniyor");
            mReportIntractor.getNextDelivery(headerData,this);
        }
    }

    @Override
    public void onSuccess(OrderSummaryModel orderSummaryModel) {
        if(mReportView!=null){
            mReportView.hideProgress();
            mReportView.getOrders(orderSummaryModel);
        }
    }

    @Override
    public void onFailure(String message) {
        if(mReportView!=null){
            mReportView.hideProgress();
            mReportView.showAlert(message,true);
        }
    }

    @Override
    public void navigateToLogin() {
        if(mReportView!=null){
            mReportView.navigateToLogin();
        }
    }


}
