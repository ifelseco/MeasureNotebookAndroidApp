package com.javaman.olcudefteri.event;

import com.javaman.olcudefteri.model.OrderUpdateModel;

import lombok.Data;

@Data
public class OrderUpdateEvent {

    private OrderUpdateModel orderUpdateModel;

}
