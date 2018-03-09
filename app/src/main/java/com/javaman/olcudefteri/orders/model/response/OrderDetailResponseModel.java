package com.javaman.olcudefteri.orders.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.javaman.olcudefteri.api.model.response.BaseResponse;
import com.javaman.olcudefteri.orders.model.CustomerDetailModel;

import java.util.Date;

import lombok.Data;

@Data
public class OrderDetailResponseModel implements Parcelable {

	private BaseResponse baseResponse;
	private Long id;
	private String userUsername;
	private Date orderDate;
	private double totalAmount;
	private double depositeAmount;
	private Date deliveryDate;
	private boolean isMountExsist;
	private Date measureDate;
	private int orderStatus;
	private CustomerDetailModel customer;

    public OrderDetailResponseModel() {
    }


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.baseResponse, flags);
		dest.writeValue(this.id);
		dest.writeString(this.userUsername);
		dest.writeLong(this.orderDate != null ? this.orderDate.getTime() : -1);
		dest.writeDouble(this.totalAmount);
		dest.writeDouble(this.depositeAmount);
		dest.writeLong(this.deliveryDate != null ? this.deliveryDate.getTime() : -1);
		dest.writeByte(this.isMountExsist ? (byte) 1 : (byte) 0);
		dest.writeLong(this.measureDate != null ? this.measureDate.getTime() : -1);
		dest.writeInt(this.orderStatus);
		dest.writeParcelable(this.customer, flags);
	}

	protected OrderDetailResponseModel(Parcel in) {
		this.baseResponse = in.readParcelable(BaseResponse.class.getClassLoader());
		this.id = (Long) in.readValue(Long.class.getClassLoader());
		this.userUsername = in.readString();
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
