package com.javaman.olcudefteri.login;

/**
 * Created by javaman on 06.02.2018.
 */

public interface LoginIntractor {

    interface onLoginFinishedListener{
        void onUserNameEmptyError();
        void onPasswordEmptyError();
        void onSuccess();
        void onFailure(String message);

    }

    void dummyLogin(String username, String password, onLoginFinishedListener listener);

    void login(String username, String password, onLoginFinishedListener listener);
}
