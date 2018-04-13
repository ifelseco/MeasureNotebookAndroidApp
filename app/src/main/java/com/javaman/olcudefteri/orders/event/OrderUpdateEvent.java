package com.javaman.olcudefteri.orders.event;

import com.javaman.olcudefteri.orders.model.OrderUpdateModel;

import lombok.Data;

@Data
public class OrderUpdateEvent {

    private OrderUpdateModel orderUpdateModel;

}
