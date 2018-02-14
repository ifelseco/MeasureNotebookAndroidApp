package com.javaman.olcudefteri.model;

/**
 * Created by javaman on 13.01.2018.
 */

public class CustomerMeasure {

    private long id;
    private int curtainCode;
    private long measureId;
    private Customer customer;
    private double price;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCurtainCode() {
        return curtainCode;
    }

    public void setCurtainCode(int curtainCode) {
        this.curtainCode = curtainCode;
    }

    public long getMeasureId() {
        return measureId;
    }

    public void setMeasureId(long measureId) {
        this.measureId = measureId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
