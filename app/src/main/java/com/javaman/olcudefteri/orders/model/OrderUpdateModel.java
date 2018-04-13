package com.javaman.olcudefteri.orders.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import lombok.Data;

@Data
public class OrderUpdateModel implements Parcelable {

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

	public OrderUpdateModel() {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(this.id);
		dest.writeString(this.userUsername);
		dest.writeLong(this.orderDate != null ? this.orderDate.getTime() : -1);
		dest.writeDouble(this.totalAmount);
		dest.writeDouble(this.depositeAmount);
		dest.writeLong(this.deliveryDate != null ? this.deliveryDate.getTime() : -1);
		dest.writeLong(this.measureDate != null ? this.measureDate.getTime() : -1);
		dest.writeByte(this.isMountExist ? (byte) 1 : (byte) 0);
		dest.writeInt(this.orderStatus);
		dest.writeString(this.orderNumber);
	}

	protected OrderUpdateModel(Parcel in) {
		this.id = (Long) in.readValue(Long.class.getClassLoader());
		this.userUsername = in.readString();
		long tmpOrderDate = in.readLong();
		this.orderDate = tmpOrderDate == -1 ? null : new Date(tmpOrderDate);
		this.totalAmount = in.readDouble();
		this.depositeAmount = in.readDouble();
		long tmpDeliveryDate = in.readLong();
		this.deliveryDate = tmpDeliveryDate == -1 ? null : new Date(tmpDeliveryDate);
		long tmpMeasureDate = in.readLong();
		this.measureDate = tmpMeasureDate == -1 ? null : new Date(tmpMeasureDate);
		this.isMountExist = in.readByte() != 0;
		this.orderStatus = in.readInt();
		this.orderNumber = in.readString();
	}

	public static final Parcelable.Creator<OrderUpdateModel> CREATOR = new Parcelable.Creator<OrderUpdateModel>() {
		@Override
		public OrderUpdateModel createFromParcel(Parcel source) {
			return new OrderUpdateModel(source);
		}

		@Override
		public OrderUpdateModel[] newArray(int size) {
			return new OrderUpdateModel[size];
		}
	};
}
