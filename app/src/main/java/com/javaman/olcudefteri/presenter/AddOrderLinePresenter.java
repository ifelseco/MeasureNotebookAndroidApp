package com.javaman.olcudefteri.presenter;

import com.javaman.olcudefteri.model.AddOrderLineDetailListModel;
import com.javaman.olcudefteri.model.DeleteOrderLinesModel;
import com.javaman.olcudefteri.model.OrderLineDetailModel;

/**
 * Created by javaman on 26.03.2018.
 */

public interface AddOrderLinePresenter {

    void addOrderLine(OrderLineDetailModel orderLineDetailModel,String headerData);
    void calculateOrderLine(AddOrderLineDetailListModel orderLineDetailListModel,String headerData);
    void addOrderLineList(AddOrderLineDetailListModel orderLineDetailListModel, String headerData);
    void deleteOrderLine(long id , String headerData);
    void deleteOrderLines(DeleteOrderLinesModel deleteOrderLinesModel, String headerData);
    void onDestroyAddOrderLine();
    void onDestroyCalculate();
}
