package com.javaman.olcudefteri.model.response_model;

import com.javaman.olcudefteri.model.CustomerDetailModel;

import java.util.Date;


public class OrderDetailResponseModel{

	private BaseResponse baseResponse;
	private Long id;
	private String userUsername;
	private Date orderDate;
	private double totalAmount;
	private double depositeAmount;
	private Date deliveryDate;
	private Date mountDate;
	private Date measureDate;
	private int orderStatus;
	private CustomerDetailModel customer;

	public BaseResponse getBaseResponse() {
		return baseResponse;
	}

	public void setBaseResponse(BaseResponse baseResponse) {
		this.baseResponse = baseResponse;
	}

	public CustomerDetailModel getCustomer() {
		return customer;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public double getDepositeAmount() {
		return depositeAmount;
	}

	public Long getId() {
		return id;
	}

	public Date getMeasureDate() {
		return measureDate;
	}

	public Date getMountDate() {
		return mountDate;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public String getUserUsername() {
		return userUsername;
	}

	public void setCustomer(CustomerDetailModel customer) {
		this.customer = customer;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public void setDepositeAmount(double depositeAmount) {
		this.depositeAmount = depositeAmount;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setMeasureDate(Date measureDate) {
		this.measureDate = measureDate;
	}

	public void setMountDate(Date mountDate) {
		this.mountDate = mountDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}


	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setUserUsername(String userUsername) {
		this.userUsername = userUsername;
	}


	@Override
	public String toString() {
		return "OrderDetailResponseModel{" +
				"baseResponse=" + baseResponse +
				", id=" + id +
				", userUsername='" + userUsername + '\'' +
				", orderDate=" + orderDate +
				", totalAmount=" + totalAmount +
				", depositeAmount=" + depositeAmount +
				", deliveryDate=" + deliveryDate +
				", mountDate=" + mountDate +
				", measureDate=" + measureDate +
				", orderStatus=" + orderStatus +
				", customer=" + customer +
				'}';
	}
}
