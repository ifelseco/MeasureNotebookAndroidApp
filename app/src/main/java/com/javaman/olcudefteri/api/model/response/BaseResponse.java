package com.javaman.olcudefteri.api.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

/**
 * Created by javaman on 15.02.2018.
 */

@Data
public class BaseResponse implements Parcelable {

    private int responseCode;
    private String responseMessage;

    public BaseResponse() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.responseCode);
        dest.writeString(this.responseMessage);
    }

    protected BaseResponse(Parcel in) {
        this.responseCode = in.readInt();
        this.responseMessage = in.readString();
    }

    public static final Parcelable.Creator<BaseResponse> CREATOR = new Parcelable.Creator<BaseResponse>() {
        @Override
        public BaseResponse createFromParcel(Parcel source) {
            return new BaseResponse(source);
        }

        @Override
        public BaseResponse[] newArray(int size) {
            return new BaseResponse[size];
        }
    };
}
