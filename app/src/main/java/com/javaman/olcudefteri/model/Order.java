package com.javaman.olcudefteri.model;

import java.util.Date;
import java.util.List;

/**
 * Created by javaman on 13.01.2018.
 */

public class Order {

    private long id;
    private Customer customer;
    private List<CustomerMeasure> measureList;
    private double totalPrice;
    private double downPayment;
    private Date deliveryDate;
    private Date orderDate;


    private int orderStatus;
    //1:hazır , 2:terzide , 3:teslim edildi


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<CustomerMeasure> getMeasureList() {
        return measureList;
    }

    public void setMeasureList(List<CustomerMeasure> measureList) {
        this.measureList = measureList;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(double downPayment) {
        this.downPayment = downPayment;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
}
