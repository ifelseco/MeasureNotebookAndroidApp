package com.javaman.olcudefteri.intractor;

import com.javaman.olcudefteri.model.AuthResponse;

/**
 * Created by javaman on 06.02.2018.
 */

public interface LoginIntractor {

    interface onLoginFinishedListener{
        void onUserNameEmptyError();
        void onPasswordEmptyError();
        void onSuccess(AuthResponse authResponse);
        void openSession(String sessionId);
        void setRememberMe(String username,String password,boolean isRememberMe);
        void onFailure(String message);

    }

    //void dummyLogin(String username, String password, onLoginFinishedListener listener);

    void login(String username, String password,boolean rememberMeActive, onLoginFinishedListener listener);



}
