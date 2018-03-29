package com.javaman.olcudefteri.base;


import android.os.Handler;

public class BasePresenterImpl implements BasePresenter ,BaseIntractor.onBaseProcessListener{

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
    public void onSuccess(final String message) {
        if(mBaseViev!=null){
            mBaseViev.hideProgress();
            mBaseViev.removeKeyFromPref("sessionId");
            mBaseViev.showAlert(message);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBaseViev.navigateToLogin();
                }
            },2000);


        }
    }

    @Override
    public void onFailure(String message) {
        if(mBaseViev!=null){
            mBaseViev.showAlert(message);
        }
    }
}
