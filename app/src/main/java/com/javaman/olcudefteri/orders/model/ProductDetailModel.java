package com.javaman.olcudefteri.orders.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by javaman on 07.03.2018.
 */

@Data
public class ProductDetailModel implements Parcelable {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("productValue")
    @Expose
    private int productValue;

    @SerializedName("variantCode")
    @Expose
    private String variantCode;

    @SerializedName("aliasName")
    @Expose
    private String aliasName;

    @SerializedName("patternCode")
    @Expose
    private String patternCode;

    public ProductDetailModel() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeInt(this.productValue);
        dest.writeString(this.variantCode);
        dest.writeString(this.aliasName);
        dest.writeString(this.patternCode);
    }

    protected ProductDetailModel(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.productValue = in.readInt();
        this.variantCode = in.readString();
        this.aliasName = in.readString();
        this.patternCode = in.readString();
    }

    public static final Parcelable.Creator<ProductDetailModel> CREATOR = new Parcelable.Creator<ProductDetailModel>() {
        @Override
        public ProductDetailModel createFromParcel(Parcel source) {
            return new ProductDetailModel(source);
        }

        @Override
        public ProductDetailModel[] newArray(int size) {
            return new ProductDetailModel[size];
        }
    };
}
