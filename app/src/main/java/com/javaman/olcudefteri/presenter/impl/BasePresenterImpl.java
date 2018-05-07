package com.javaman.olcudefteri.presenter.impl;


import android.os.Handler;

import com.javaman.olcudefteri.presenter.BasePresenter;
import com.javaman.olcudefteri.view.BaseView;
import com.javaman.olcudefteri.intractor.BaseIntractor;
import com.javaman.olcudefteri.intractor.impl.BaseIntractorImpl;
import com.javaman.olcudefteri.model.LoginUserModel;

public class BasePresenterImpl implements BasePresenter,BaseIntractor.onBaseProcessListener{

    BaseView mBaseViev;
    BaseIntractor mBaseIntractor;

    public BasePresenterImpl(BaseView baseView ){
        this.mBaseViev=baseView;
        mBaseIntractor=new BaseIntractorImpl();
    }


    @Override
    public void logout(String sessionId) {
        if(mBaseViev!=null){
            mBaseViev.showProgress("Çıkış Yapılıyor");
            mBaseIntractor.logout(sessionId,this);
        }
    }

    @Override
    public void checkSession(String sessionId) {
        if(mBaseViev!=null){
            mBaseIntractor.checkSession(sessionId,this);
        }
    }

    @Override
    public void onDestroy() {
        if(mBaseViev!=null){
            mBaseViev=null;
        }
    }

    @Override
    public void onSuccessLogout(final String message) {
        if(mBaseViev!=null){
            mBaseViev.hideProgress();
            mBaseViev.removeKeyFromPref("sessionId");
            mBaseViev.removeKeyFromPref("lastActivity");
            mBaseViev.showAlert(message,true);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBaseViev.navigateToLogin();
                }
            },2000);


        }
    }

    @Override
    public void onFailureLogout(String message) {
        if(mBaseViev!=null){
            mBaseViev.showAlert(message,true);
        }
    }





    @Override
    public void onSuccessCheckSession(LoginUserModel loginUserModel) {
        if(mBaseViev!=null){
        }
    }

    @Override
    public void onFailureCheckSession(String message) {
        if(mBaseViev!=null){
            mBaseViev.showAlert(message,true);
            mBaseViev.logout();
        }
    }
}
