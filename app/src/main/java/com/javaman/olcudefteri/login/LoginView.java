package com.javaman.olcudefteri.login;

/**
 * Created by javaman on 06.02.2018.
 */

public interface LoginView {

    void showProgress();
    void hideProgress();
    void setUserNameEmptyError();
    void setPasswordEmptyError();
    void navigatetoHome();
    void showAlert(String message);
    void openSession(String sessionId);
}
