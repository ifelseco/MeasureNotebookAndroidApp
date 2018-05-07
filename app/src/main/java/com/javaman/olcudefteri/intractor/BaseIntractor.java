package com.javaman.olcudefteri.intractor;

import com.javaman.olcudefteri.model.LoginUserModel;

public interface BaseIntractor {

    interface onBaseProcessListener{
        void onSuccessLogout(String message);
        void onFailureLogout(String message);
        void onSuccessCheckSession(LoginUserModel loginUserModel);
        void onFailureCheckSession(String message);
    }

    void logout(String sessionId , onBaseProcessListener listener);
    void checkSession(String sessionId , onBaseProcessListener listener);
}
