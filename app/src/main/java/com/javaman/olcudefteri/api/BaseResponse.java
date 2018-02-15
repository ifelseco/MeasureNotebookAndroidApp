package com.javaman.olcudefteri.api;

/**
 * Created by javaman on 15.02.2018.
 */

public class BaseResponse {

    private int responseCode;
    private String responseMessage;


    public int getResponseCode() {
        return responseCode;
    }




    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }




    public String getResponseMessage() {
        return responseMessage;
    }




    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
