package com.javaman.olcudefteri.intractor;

import com.javaman.olcudefteri.model.AddOrderLineDetailListModel;
import com.javaman.olcudefteri.model.DeleteOrderLinesModel;
import com.javaman.olcudefteri.model.OrderLineDetailModel;
import com.javaman.olcudefteri.model.AddOrderLineListResponse;
import com.javaman.olcudefteri.model.AddOrderLineResponse;
import com.javaman.olcudefteri.model.CalculationResponse;

/**
 * Created by javaman on 15.02.2018.
 */

public interface AddOrderLineIntractor {

    interface onAddOrderLineListener{
        void onSuccessAddOrderLine(AddOrderLineResponse addOrderLineResponse,OrderLineDetailModel orderLineDetailModel);
        void onFailureAddOrderLine(String message);
    }

    interface onAddOrderLinesListener{
        void onSuccessAddOrderLines(AddOrderLineListResponse addOrderLineListResponse);
        void onFailureAddOrderLines(String message);
    }

    interface onDeleteOrderLineListener{
        void onSuccessDeleteOrderLine();
        void onFailureDeleteOrderLine(String message);
    }

    interface onDeleteOrderLinesListener{
        void onSuccessDeleteOrderLines();
        void onFailureDeleteOrderLines(String message);
    }

    interface onCalculateOrderLineListener{
        void onSuccessCalculateOrderLines(CalculationResponse calculationResponse);
        void onFailureCalculateOrderLines(String message);
    }



    void addOrderLine(OrderLineDetailModel orderLineDetailModel, String headerData, onAddOrderLineListener listener);
    void addOrderLines(AddOrderLineDetailListModel orderLineDetailListModel, String headerData, onAddOrderLinesListener listener);
    void deleteOrderLine(long id, String headerData, onDeleteOrderLineListener listener);
    void deleteOrderLines(DeleteOrderLinesModel deleteOrderLinesModel, String headerData, onDeleteOrderLinesListener listener);
    void calculateOrderLine(AddOrderLineDetailListModel orderLineDetailListModel, String headerData, onCalculateOrderLineListener listener);



}
