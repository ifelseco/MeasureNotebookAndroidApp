package com.javaman.olcudefteri.base;

public interface BaseView {

    void logout();
    void checkSession();
    void navigateToLogin();
    String getSessionIdFromPref();
    void removeKeyFromPref(String key);
    void showAlert(String message);
    void showProgress(String message);
    void hideProgress();
}
