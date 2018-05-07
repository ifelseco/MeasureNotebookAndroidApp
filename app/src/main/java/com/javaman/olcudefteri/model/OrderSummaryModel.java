package com.javaman.olcudefteri.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderSummaryModel implements Parcelable {
	
	private BaseModel baseModel;
	private List<OrderDetailModel> orders=new ArrayList<>();

	public OrderSummaryModel() {
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.baseModel, flags);
		dest.writeTypedList(this.orders);
	}

	protected OrderSummaryModel(Parcel in) {
		this.baseModel = in.readParcelable(BaseModel.class.getClassLoader());
		this.orders = in.createTypedArrayList(OrderDetailModel.CREATOR);
	}

	public static final Parcelable.Creator<OrderSummaryModel> CREATOR = new Parcelable.Creator<OrderSummaryModel>() {
		@Override
		public OrderSummaryModel createFromParcel(Parcel source) {
			return new OrderSummaryModel(source);
		}

		@Override
		public OrderSummaryModel[] newArray(int size) {
			return new OrderSummaryModel[size];
		}
	};
}
