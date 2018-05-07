package com.javaman.olcudefteri.utill;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by javaman on 08.02.2018.
 */

public class ApiUtils {

    public static final String BASE_URL="https://measure-notebook-api.herokuapp.com/";

    public static String getAuthToken(String username , String password) {
        byte[] data = new byte[0];
        try {
            data = (username + ":" + password).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "Basic " + Base64.encodeToString(data, Base64.NO_WRAP);
    }

}
