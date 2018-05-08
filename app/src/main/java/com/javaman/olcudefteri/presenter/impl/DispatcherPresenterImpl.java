package com.javaman.olcudefteri.presenter.impl;

import com.javaman.olcudefteri.intractor.DispatcherIntractor;
import com.javaman.olcudefteri.intractor.impl.DispatcherIntractorImpl;
import com.javaman.olcudefteri.model.AppUtilInfoModel;
import com.javaman.olcudefteri.presenter.DispatcherPresenter;
import com.javaman.olcudefteri.view.DispatcherView;

public class DispatcherPresenterImpl implements DispatcherPresenter,DispatcherIntractor.onNotificationProcessListener{

    private DispatcherView mDispatcherView;
    private DispatcherIntractor mDispatcherIntractor;


    public DispatcherPresenterImpl(DispatcherView dispatcherView) {
        mDispatcherView=dispatcherView;
        mDispatcherIntractor=new DispatcherIntractorImpl();
    }

    @Override
    public void getAppUtilInfo(String headerData) {
        if(mDispatcherView!=null){
            mDispatcherIntractor.getAppUtilInfo(headerData,this);
        }
    }

    @Override
    public void onSuccess(AppUtilInfoModel appUtilInfoModel) {
        if(mDispatcherView!=null){
            mDispatcherView.saveAppUtilInfoToPref(appUtilInfoModel);
            mDispatcherView.redirect();
        }
    }

    @Override
    public void onFailure(String message) {
        if(mDispatcherView!=null){
            mDispatcherView.showAlert(message,true);
            mDispatcherView.redirect();
        }
    }

    @Override
    public void navigateToLogin() {
        if(mDispatcherView!=null){
            mDispatcherView.navigateToLogin();
        }
    }


}
