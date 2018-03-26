package com.javaman.olcudefteri.orders.model.response;

import com.javaman.olcudefteri.api.model.response.BaseModel;
import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;

import java.util.ArrayList;

import lombok.Data;

@Data
public class AddOrderLineListResponse {
	
	private BaseModel baseModel;
	
	private double orderTotalAmount;
	
	private ArrayList<OrderLineDetailModel> orderLines=new ArrayList<>();

}
