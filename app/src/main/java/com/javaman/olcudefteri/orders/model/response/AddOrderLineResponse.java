package com.javaman.olcudefteri.orders.model.response;

import com.javaman.olcudefteri.api.model.response.BaseModel;

import lombok.Data;

@Data
public class AddOrderLineResponse {

	private BaseModel baseModel;
	private Long id;
	private double lineAmount;
	private double orderTotalAmount;
}
