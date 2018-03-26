package com.javaman.olcudefteri.orders.model;

import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;

import java.util.List;

import lombok.Data;

@Data
public class AddOrderLineDetailListModel {
	
	private List<OrderLineDetailModel> orderLineDetailModelList;


}
