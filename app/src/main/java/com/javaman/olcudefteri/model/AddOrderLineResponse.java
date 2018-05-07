package com.javaman.olcudefteri.model;

import com.javaman.olcudefteri.model.BaseModel;

import lombok.Data;

@Data
public class AddOrderLineResponse {

	private BaseModel baseModel;
	private Long id;
	private double lineAmount;
	private double orderTotalAmount;
}
