package com.javaman.olcudefteri.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by javaman on 07.03.2018.
 */

@Data
public class OrderLineDetailModel implements Parcelable {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("product")
    @Expose
    private ProductDetailModel product;

    @SerializedName("order")
    @Expose
    private OrderDetailModel order;

    @SerializedName("lineDescription")
    @Expose
    private String lineDescription;

    @SerializedName("propertyWidth")
    @Expose
    private double propertyWidth;

    @SerializedName("propertyHeight")
    @Expose
    private double propertyHeight;

    @SerializedName("propertyAlternativeWidth")
    @Expose
    private double propertyAlternativeWidth;

    @SerializedName("propertyAlternativeHeight")
    @Expose
    private double propertyAlternativeHeight;

    @SerializedName("sizeOfPile")
    @Expose
    private double sizeOfPile;

    @SerializedName("unitPrice")
    @Expose
    private double unitPrice;

    @SerializedName("lineAmount")
    @Expose
    private double lineAmount;

    @SerializedName("propertyLeftWidth")
    @Expose
    private double propertyLeftWidth;

    @SerializedName("propertyRightWidth")
    @Expose
    private double propertyRightWidth;

    @SerializedName("skirtNo")
    @Expose
    private String skirtNo;

    @SerializedName("beadNo")
    @Expose
    private String beadNo;

    @SerializedName("mountType")
    @Expose
    private int mountType;

    @SerializedName("pileName")
    @Expose
    private String pileName;

    @SerializedName("piecesCount")
    @Expose
    private int piecesCount;

    @SerializedName("usedMaterial")
    @Expose
    private double usedMaterial;

    @SerializedName("locationType")
    @Expose
    private String locationType;

    @SerializedName("direction")
    @Expose
    private int direction;

    @SerializedName("locationName")
    @Expose
    private String locationName;

    @SerializedName("mechanismStatus")
    @Expose
    private int mechanismStatus;

    @SerializedName("fonType")
    @Expose
    private int fonType;

    @SerializedName("propertyModelName")
    @Expose
    private String propertyModelName;

    public OrderLineDetailModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeParcelable(this.product, flags);
        dest.writeParcelable(this.order, flags);
        dest.writeString(this.lineDescription);
        dest.writeDouble(this.propertyWidth);
        dest.writeDouble(this.propertyHeight);
        dest.writeDouble(this.propertyAlternativeWidth);
        dest.writeDouble(this.propertyAlternativeHeight);
        dest.writeDouble(this.sizeOfPile);
        dest.writeDouble(this.unitPrice);
        dest.writeDouble(this.lineAmount);
        dest.writeDouble(this.propertyLeftWidth);
        dest.writeDouble(this.propertyRightWidth);
        dest.writeString(this.skirtNo);
        dest.writeString(this.beadNo);
        dest.writeInt(this.mountType);
        dest.writeString(this.pileName);
        dest.writeInt(this.piecesCount);
        dest.writeDouble(this.usedMaterial);
        dest.writeString(this.locationType);
        dest.writeInt(this.direction);
        dest.writeString(this.locationName);
        dest.writeInt(this.mechanismStatus);
        dest.writeInt(this.fonType);
        dest.writeString(this.propertyModelName);
    }

    protected OrderLineDetailModel(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.product = in.readParcelable(ProductDetailModel.class.getClassLoader());
        this.order = in.readParcelable(OrderDetailModel.class.getClassLoader());
        this.lineDescription = in.readString();
        this.propertyWidth = in.readDouble();
        this.propertyHeight = in.readDouble();
        this.propertyAlternativeWidth = in.readDouble();
        this.propertyAlternativeHeight = in.readDouble();
        this.sizeOfPile = in.readDouble();
        this.unitPrice = in.readDouble();
        this.lineAmount = in.readDouble();
        this.propertyLeftWidth = in.readDouble();
        this.propertyRightWidth = in.readDouble();
        this.skirtNo = in.readString();
        this.beadNo = in.readString();
        this.mountType = in.readInt();
        this.pileName = in.readString();
        this.piecesCount = in.readInt();
        this.usedMaterial = in.readDouble();
        this.locationType = in.readString();
        this.direction = in.readInt();
        this.locationName = in.readString();
        this.mechanismStatus = in.readInt();
        this.fonType = in.readInt();
        this.propertyModelName = in.readString();
    }

    public static final Parcelable.Creator<OrderLineDetailModel> CREATOR = new Parcelable.Creator<OrderLineDetailModel>() {
        @Override
        public OrderLineDetailModel createFromParcel(Parcel source) {
            return new OrderLineDetailModel(source);
        }

        @Override
        public OrderLineDetailModel[] newArray(int size) {
            return new OrderLineDetailModel[size];
        }
    };
}
