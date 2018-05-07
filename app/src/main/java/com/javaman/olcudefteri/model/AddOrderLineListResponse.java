package com.javaman.olcudefteri.model;

import java.util.ArrayList;

import lombok.Data;

@Data
public class AddOrderLineListResponse {
	
	private BaseModel baseModel;
	
	private double orderTotalAmount;
	
	private ArrayList<OrderLineDetailModel> orderLines=new ArrayList<>();

}
