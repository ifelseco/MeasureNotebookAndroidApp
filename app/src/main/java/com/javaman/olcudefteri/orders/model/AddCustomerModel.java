package com.javaman.olcudefteri.orders.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class AddCustomerModel {

	@SerializedName("customerDetailModel")
	@Expose
	private CustomerDetailModel customerDetailModel;

	@SerializedName("orderStatus")
	@Expose
	private int orderStatus;
	

	
	

}
