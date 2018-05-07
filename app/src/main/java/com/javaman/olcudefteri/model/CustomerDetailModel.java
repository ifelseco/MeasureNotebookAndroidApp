package com.javaman.olcudefteri.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class CustomerDetailModel implements Parcelable {

	@SerializedName("id")
	@Expose
	private Long id;

	@SerializedName("mobilePhone")
	@Expose
	private String mobilePhone;

	@SerializedName("nameSurname")
	@Expose
	private String nameSurname;

	@SerializedName("fixedPhone")
	@Expose
	private String fixedPhone;

	@SerializedName("address")
	@Expose
	private String address;

	@SerializedName("newsletterAccepted")
	@Expose
	private boolean newsletterAccepted;

	public CustomerDetailModel() {}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(this.id);
		dest.writeString(this.mobilePhone);
		dest.writeString(this.nameSurname);
		dest.writeString(this.fixedPhone);
		dest.writeString(this.address);
		dest.writeByte(this.newsletterAccepted ? (byte) 1 : (byte) 0);
	}

	protected CustomerDetailModel(Parcel in) {
		this.id = (Long) in.readValue(Long.class.getClassLoader());
		this.mobilePhone = in.readString();
		this.nameSurname = in.readString();
		this.fixedPhone = in.readString();
		this.address = in.readString();
		this.newsletterAccepted = in.readByte() != 0;
	}

	public static final Parcelable.Creator<CustomerDetailModel> CREATOR = new Parcelable.Creator<CustomerDetailModel>() {
		@Override
		public CustomerDetailModel createFromParcel(Parcel source) {
			return new CustomerDetailModel(source);
		}

		@Override
		public CustomerDetailModel[] newArray(int size) {
			return new CustomerDetailModel[size];
		}
	};
}
