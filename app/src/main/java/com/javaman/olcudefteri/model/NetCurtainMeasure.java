package com.javaman.olcudefteri.model;

/**
 * Created by javaman on 13.01.2018.
 */

public class NetCurtainMeasure{

    private long id;
    private String type;
    private String alias;
    private String pattern;
    private String variant;
    private String description;
    private Customer customer;
    private int curtainCode;
    private double pile;
    private String location;
    private String window;
    private double totalM2;
    private double totalPrice;
    private double unitPrice;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getCurtainCode() {
        return curtainCode;
    }

    public void setCurtainCode(int curtainCode) {
        this.curtainCode = curtainCode;
    }

    public double getPile() {
        return pile;
    }

    public void setPile(double pile) {
        this.pile = pile;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWindow() {
        return window;
    }

    public void setWindow(String window) {
        this.window = window;
    }

    public double getTotalM2() {
        return totalM2;
    }

    public void setTotalM2(double totalM2) {
        this.totalM2 = totalM2;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
