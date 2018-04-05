package com.javaman.olcudefteri.orders.model;

import java.util.Date;
import lombok.Data;

@Data
public class OrderUpdateModel{

	private Long id;
	private String userUsername;
	private Date orderDate;
	private double totalAmount;
	private double depositeAmount;
	private Date deliveryDate;
	private Date measureDate;
	private boolean isMountExist;
	private int orderStatus;
	private String orderNumber;



}
