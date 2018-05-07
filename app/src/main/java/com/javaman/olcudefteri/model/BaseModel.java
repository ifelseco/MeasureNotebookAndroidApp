package com.javaman.olcudefteri.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

/**
 * Created by javaman on 15.02.2018.
 */

@Data
public class BaseModel implements Parcelable {

    private int responseCode;
    private String responseMessage;

    public BaseModel() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.responseCode);
        dest.writeString(this.responseMessage);
    }

    protected BaseModel(Parcel in) {
        this.responseCode = in.readInt();
        this.responseMessage = in.readString();
    }

    public static final Parcelable.Creator<BaseModel> CREATOR = new Parcelable.Creator<BaseModel>() {
        @Override
        public BaseModel createFromParcel(Parcel source) {
            return new BaseModel(source);
        }

        @Override
        public BaseModel[] newArray(int size) {
            return new BaseModel[size];
        }
    };
}
