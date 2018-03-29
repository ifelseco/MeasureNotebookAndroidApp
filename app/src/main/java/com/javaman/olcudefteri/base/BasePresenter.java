package com.javaman.olcudefteri.base;

public interface BasePresenter {

    void logout(String sessionId);
    void checkSession(String sessionId);
    void onDestroy();
}
