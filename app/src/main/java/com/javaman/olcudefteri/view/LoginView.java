package com.javaman.olcudefteri.view;

/**
 * Created by javaman on 06.02.2018.
 */

public interface LoginView {

    void showProgress();
    void hideProgress();
    void setUserNameEmptyError();
    void setPasswordEmptyError();
    void navigatetoHome(String role);
    void showAlert(String message, boolean isError);
    void openSession(String sessionId);
    void setRememberMe(String username,String password,boolean rememberMeActive);
}
