package com.javaman.olcudefteri.orders.intractor;

import com.javaman.olcudefteri.api.model.response.BaseModel;
import com.javaman.olcudefteri.orders.model.OrderUpdateModel;

public interface OrderIntractor {

    interface onOrderProcessListener{
        void onSuccessUpdateOrder(BaseModel baseModel,OrderUpdateModel orderUpdateModel);
        void onFailureUpdateOrder(String message);
        void onSuccessDeleteOrder(String message);
        void onFailureDeleteOrder(String message);
        void navigateToLogin();
    }

    void orderUpdate(String xAuthToken, OrderUpdateModel orderUpdateModel ,onOrderProcessListener listener);
    void orderDelete(String xAuthToken, long id,onOrderProcessListener listener);
}
