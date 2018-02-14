package com.javaman.olcudefteri.login;

/**
 * Created by javaman on 07.02.2018.
 */

public interface LoginPresenter {

    void validateCredential(String username , String password);
    void onDestroy();
}