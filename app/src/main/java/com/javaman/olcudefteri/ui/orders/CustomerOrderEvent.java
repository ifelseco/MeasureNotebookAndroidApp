package com.javaman.olcudefteri.ui.orders;

/**
 * Created by javaman on 12.02.2018.
 */

public class CustomerOrderEvent {

    private String message;
    private boolean isOrderRegistered;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isOrderRegistered() {
        return isOrderRegistered;
    }

    public void setOrderRegistered(boolean orderRegistered) {
        isOrderRegistered = orderRegistered;
    }
}

