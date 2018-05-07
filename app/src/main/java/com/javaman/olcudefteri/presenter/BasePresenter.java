package com.javaman.olcudefteri.presenter;

public interface BasePresenter {

    void logout(String sessionId);
    void checkSession(String sessionId);
    void onDestroy();
}
