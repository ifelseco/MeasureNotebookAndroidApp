package com.javaman.olcudefteri.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddCustomerModel {

	@SerializedName("customerDetailModel")
	@Expose
	private CustomerDetailModel customerDetailModel;

	@SerializedName("orderStatus")
	@Expose
	private int orderStatus;
	
	public CustomerDetailModel getCustomerDetailModel() {
		return customerDetailModel;
	}

	public void setCustomerDetailModel(CustomerDetailModel customerDetailModel) {
		this.customerDetailModel = customerDetailModel;
	}
	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	

}
