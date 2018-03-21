package com.javaman.olcudefteri.orders;

import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;

import lombok.Data;

/**
 * Created by javaman on 21.03.2018.
 */

@Data
public class OrderLineEvent {

    private OrderLineDetailModel orderLineDetailModel;
}
