package com.javaman.olcudefteri.login.presenter;

import com.javaman.olcudefteri.login.view.LoginView;
import com.javaman.olcudefteri.login.intractor.LoginIntractor;
import com.javaman.olcudefteri.login.intractor.LoginIntractorImpl;

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
    public void validateCredential(String username, String password) {

        if(mLoginView!=null){
            mLoginView.showProgress();
           mLoginIntractor.login(username,password,this);
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
    public void onSuccess() {
        if(mLoginView!=null){
            mLoginView.hideProgress();
            mLoginView.navigatetoHome();
        }
    }

    @Override
    public void openSession(String sessionId) {
        if(mLoginView!=null){

            mLoginView.openSession(sessionId);
        }
    }

    @Override
    public void onFailure(String message) {
        if(mLoginView!=null){
            mLoginView.hideProgress();
            mLoginView.showAlert(message);
        }
    }
}
