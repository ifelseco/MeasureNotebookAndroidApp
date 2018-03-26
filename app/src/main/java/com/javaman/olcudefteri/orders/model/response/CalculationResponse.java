package com.javaman.olcudefteri.orders.model.response;

import com.javaman.olcudefteri.api.model.response.BaseModel;

import lombok.Data;

@Data
public class CalculationResponse {
	
	private BaseModel baseModel;
	private double totalAmount;
	private double usedMaterial;
	

	
	

}
