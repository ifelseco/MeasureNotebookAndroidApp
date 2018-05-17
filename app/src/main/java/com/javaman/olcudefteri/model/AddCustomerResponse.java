package com.javaman.olcudefteri.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import lombok.Data;

/**
 * Created by javaman on 15.02.2018.
 */

@Data
public class AddCustomerResponse implements Parcelable {

    private BaseModel baseModel;

    private Long id;


    private Date orderDate;


    private Long customerId;

    private String orderNumber;


    private String customerNameSurname;

    public AddCustomerResponse() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.baseModel, flags);
        dest.writeValue(this.id);
        dest.writeLong(this.orderDate != null ? this.orderDate.getTime() : -1);
        dest.writeValue(this.customerId);
        dest.writeString(this.orderNumber);
        dest.writeString(this.customerNameSurname);
    }

    protected AddCustomerResponse(Parcel in) {
        this.baseModel = in.readParcelable(BaseModel.class.getClassLoader());
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        long tmpOrderDate = in.readLong();
        this.orderDate = tmpOrderDate == -1 ? null : new Date(tmpOrderDate);
        this.customerId = (Long) in.readValue(Long.class.getClassLoader());
        this.orderNumber = in.readString();
        this.customerNameSurname = in.readString();
    }

    public static final Creator<AddCustomerResponse> CREATOR = new Creator<AddCustomerResponse>() {
        @Override
        public AddCustomerResponse createFromParcel(Parcel source) {
            return new AddCustomerResponse(source);
        }

        @Override
        public AddCustomerResponse[] newArray(int size) {
            return new AddCustomerResponse[size];
        }
    };
}
