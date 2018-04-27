package com.javaman.olcudefteri.orders.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import lombok.Data;

/**
 * Created by javaman on 07.03.2018.
 */

@Data
public class OrderDetailModel implements Parcelable {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("userNameSurname")
    @Expose
    private String userNameSurname;

    @SerializedName("orderDate")
    @Expose
    private Date orderDate;

    @SerializedName("totalAmount")
    @Expose
    private double totalAmount;

    @SerializedName("depositeAmount")
    @Expose
    private double depositeAmount;

    @SerializedName("deliveryDate")
    @Expose
    private Date deliveryDate;

    @SerializedName("isMountExsist")
    @Expose
    private boolean isMountExsist;

    @SerializedName("measureDate")
    @Expose
    private Date measureDate;

    @SerializedName("orderStatus")
    @Expose
    private int orderStatus;

    @SerializedName("customer")
    @Expose
    private CustomerDetailModel customer;


    public OrderDetailModel() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.userNameSurname);
        dest.writeLong(this.orderDate != null ? this.orderDate.getTime() : -1);
        dest.writeDouble(this.totalAmount);
        dest.writeDouble(this.depositeAmount);
        dest.writeLong(this.deliveryDate != null ? this.deliveryDate.getTime() : -1);
        dest.writeByte(this.isMountExsist ? (byte) 1 : (byte) 0);
        dest.writeLong(this.measureDate != null ? this.measureDate.getTime() : -1);
        dest.writeInt(this.orderStatus);
        dest.writeParcelable(this.customer, flags);
    }

    protected OrderDetailModel(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.userNameSurname = in.readString();
        long tmpOrderDate = in.readLong();
        this.orderDate = tmpOrderDate == -1 ? null : new Date(tmpOrderDate);
        this.totalAmount = in.readDouble();
        this.depositeAmount = in.readDouble();
        long tmpDeliveryDate = in.readLong();
        this.deliveryDate = tmpDeliveryDate == -1 ? null : new Date(tmpDeliveryDate);
        this.isMountExsist = in.readByte() != 0;
        long tmpMeasureDate = in.readLong();
        this.measureDate = tmpMeasureDate == -1 ? null : new Date(tmpMeasureDate);
        this.orderStatus = in.readInt();
        this.customer = in.readParcelable(CustomerDetailModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<OrderDetailModel> CREATOR = new Parcelable.Creator<OrderDetailModel>() {
        @Override
        public OrderDetailModel createFromParcel(Parcel source) {
            return new OrderDetailModel(source);
        }

        @Override
        public OrderDetailModel[] newArray(int size) {
            return new OrderDetailModel[size];
        }
    };
}
