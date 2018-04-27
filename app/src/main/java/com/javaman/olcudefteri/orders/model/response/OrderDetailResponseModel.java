package com.javaman.olcudefteri.orders.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.javaman.olcudefteri.api.model.response.BaseModel;
import com.javaman.olcudefteri.orders.model.CustomerDetailModel;

import java.util.Date;

import lombok.Data;

@Data
public class OrderDetailResponseModel implements Parcelable {

	private BaseModel baseModel;
	private Long id;
	private String userNameSurname;
	private Date orderDate;
	private double totalAmount;
	private double depositeAmount;
	private Date deliveryDate;
	private boolean mountExist;
	private Date measureDate;
	private int orderStatus;
	private CustomerDetailModel customer;

	public OrderDetailResponseModel() {}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.baseModel, flags);
		dest.writeValue(this.id);
		dest.writeString(this.userNameSurname);
		dest.writeLong(this.orderDate != null ? this.orderDate.getTime() : -1);
		dest.writeDouble(this.totalAmount);
		dest.writeDouble(this.depositeAmount);
		dest.writeLong(this.deliveryDate != null ? this.deliveryDate.getTime() : -1);
		dest.writeByte(this.mountExist ? (byte) 1 : (byte) 0);
		dest.writeLong(this.measureDate != null ? this.measureDate.getTime() : -1);
		dest.writeInt(this.orderStatus);
		dest.writeParcelable(this.customer, flags);
	}

	protected OrderDetailResponseModel(Parcel in) {
		this.baseModel = in.readParcelable(BaseModel.class.getClassLoader());
		this.id = (Long) in.readValue(Long.class.getClassLoader());
		this.userNameSurname = in.readString();
		long tmpOrderDate = in.readLong();
		this.orderDate = tmpOrderDate == -1 ? null : new Date(tmpOrderDate);
		this.totalAmount = in.readDouble();
		this.depositeAmount = in.readDouble();
		long tmpDeliveryDate = in.readLong();
		this.deliveryDate = tmpDeliveryDate == -1 ? null : new Date(tmpDeliveryDate);
		this.mountExist = in.readByte() != 0;
		long tmpMeasureDate = in.readLong();
		this.measureDate = tmpMeasureDate == -1 ? null : new Date(tmpMeasureDate);
		this.orderStatus = in.readInt();
		this.customer = in.readParcelable(CustomerDetailModel.class.getClassLoader());
	}

	public static final Creator<OrderDetailResponseModel> CREATOR = new Creator<OrderDetailResponseModel>() {
		@Override
		public OrderDetailResponseModel createFromParcel(Parcel source) {
			return new OrderDetailResponseModel(source);
		}

		@Override
		public OrderDetailResponseModel[] newArray(int size) {
			return new OrderDetailResponseModel[size];
		}
	};
}
