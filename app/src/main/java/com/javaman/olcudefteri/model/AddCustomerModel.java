package com.javaman.olcudefteri.model;

public class AddCustomerModel {
	
	private CustomerDetailModel customerDetailModel;
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
