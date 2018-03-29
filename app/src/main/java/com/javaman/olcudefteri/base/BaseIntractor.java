package com.javaman.olcudefteri.base;

public interface BaseIntractor {

    interface onBaseProcessListener{
        void onSuccess(String message);
        void onFailure(String message);
    }

    void logout(String sessionId , onBaseProcessListener listener);
    void checkSession(String sessionId , onBaseProcessListener listener);
}
