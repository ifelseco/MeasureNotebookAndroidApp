package com.javaman.olcudefteri.dispatcher.presenter;

import com.javaman.olcudefteri.dispatcher.intractor.DispatcherIntractor;
import com.javaman.olcudefteri.dispatcher.intractor.DispatcherIntractorImpl;
import com.javaman.olcudefteri.dispatcher.model.CountModel;
import com.javaman.olcudefteri.dispatcher.view.DispatcherView;

public class DispatcherPresenterImpl implements DispatcherPresenter ,DispatcherIntractor.onNotificationProcessListener{

    private DispatcherView mDispatcherView;
    private DispatcherIntractor mDispatcherIntractor;


    public DispatcherPresenterImpl(DispatcherView dispatcherView) {
        mDispatcherView=dispatcherView;
        mDispatcherIntractor=new DispatcherIntractorImpl();
    }

    @Override
    public void getNotificationCount(String headerData) {
        if(mDispatcherView!=null){
            mDispatcherIntractor.getNotificationCountFromServer(headerData,this);
        }
    }

    @Override
    public void onSuccess(CountModel countModel) {
        if(mDispatcherView!=null){
            mDispatcherView.saveNotfCountToPref(countModel.getCount());
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
