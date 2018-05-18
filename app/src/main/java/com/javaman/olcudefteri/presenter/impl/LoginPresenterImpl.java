package com.javaman.olcudefteri.presenter.impl;

import android.text.TextUtils;

import com.javaman.olcudefteri.model.AuthResponse;
import com.javaman.olcudefteri.presenter.LoginPresenter;
import com.javaman.olcudefteri.view.LoginView;
import com.javaman.olcudefteri.intractor.LoginIntractor;
import com.javaman.olcudefteri.intractor.impl.LoginIntractorImpl;

/**
 * Created by javaman on 06.02.2018.
 */

public class LoginPresenterImpl implements LoginPresenter,LoginIntractor.onLoginFinishedListener {

    LoginView mLoginView;
    LoginIntractor mLoginIntractor;

    public LoginPresenterImpl(LoginView mLoginView ){
        this.mLoginView=mLoginView;
        mLoginIntractor=new LoginIntractorImpl();
    }


    @Override
    public void validateCredential(String username, String password,boolean rememberMeActive) {

        if(mLoginView!=null){
            mLoginView.showProgress();
           mLoginIntractor.login(username,password,rememberMeActive,this);
           // mLoginIntractor.dummyLogin(username,password,this);
        }

    }


    @Override
    public void onDestroy() {
        if(mLoginView!=null){
            mLoginView=null;
        }
    }

    @Override
    public void onUserNameEmptyError() {
        if(mLoginView!=null){
            mLoginView.hideProgress();
            mLoginView.setUserNameEmptyError();
        }
    }

    @Override
    public void onPasswordEmptyError() {
        if(mLoginView!=null){
            mLoginView.hideProgress();
            mLoginView.setPasswordEmptyError();
        }
    }

    @Override
    public void onSuccess(AuthResponse authResponse) {

        if(mLoginView!=null){
            boolean isTailor = false;
            if(TextUtils.equals(authResponse.getUserDetailModel().getRole(),"r3")){
                isTailor=true;
            }
            mLoginView.hideProgress();
            mLoginView.navigatetoHome(isTailor);
        }
    }

    @Override
    public void openSession(String sessionId) {
        if(mLoginView!=null){

            mLoginView.openSession(sessionId);
        }
    }

    @Override
    public void setRememberMe(String username, String password, boolean isRememberMe) {
        mLoginView.setRememberMe(username,password,isRememberMe);
    }

    @Override
    public void onFailure(String message) {
        if(mLoginView!=null){
            mLoginView.hideProgress();
            mLoginView.showAlert(message);
        }
    }
}
