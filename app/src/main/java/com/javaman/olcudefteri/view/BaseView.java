package com.javaman.olcudefteri.view;

public interface BaseView {

    void logout();
    void checkSession();
    void navigateToLogin();
    String getSessionIdFromPref();
    void removeKeyFromPref(String key);
    void showAlert(String message,boolean isToast);
    void showProgress(String message);
    void hideProgress();
    void setLogoutPref(boolean isLogout);
}
