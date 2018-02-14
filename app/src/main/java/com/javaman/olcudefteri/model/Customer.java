package com.javaman.olcudefteri.model;

/**
 * Created by javaman on 13.01.2018.
 */

public class Customer {

    private long id;
    private String name;
    private String surname;
    private String mobilePhone;
    private String fixedPhone;
    private String address;
    private boolean isGivePermission;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getFixedPhone() {
        return fixedPhone;
    }

    public void setFixedPhone(String fixedPhone) {
        this.fixedPhone = fixedPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isGivePermission() {
        return isGivePermission;
    }

    public void setGivePermission(boolean givePermission) {
        isGivePermission = givePermission;
    }
}
