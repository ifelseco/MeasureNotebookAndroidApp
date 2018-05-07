package com.javaman.olcudefteri.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

import java.util.Date;

@Data
public class NotificationDetailModel implements Parcelable {

    private Long id;
    private String title;
    private String message;
    private String data;
    private boolean readNotification;
    private Date createdDate;

    public NotificationDetailModel() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.title);
        dest.writeString(this.message);
        dest.writeString(this.data);
        dest.writeByte(this.readNotification ? (byte) 1 : (byte) 0);
        dest.writeLong(this.createdDate != null ? this.createdDate.getTime() : -1);
    }

    protected NotificationDetailModel(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.title = in.readString();
        this.message = in.readString();
        this.data = in.readString();
        this.readNotification = in.readByte() != 0;
        long tmpCreatedDate = in.readLong();
        this.createdDate = tmpCreatedDate == -1 ? null : new Date(tmpCreatedDate);
    }

    public static final Parcelable.Creator<NotificationDetailModel> CREATOR = new Parcelable.Creator<NotificationDetailModel>() {
        @Override
        public NotificationDetailModel createFromParcel(Parcel source) {
            return new NotificationDetailModel(source);
        }

        @Override
        public NotificationDetailModel[] newArray(int size) {
            return new NotificationDetailModel[size];
        }
    };
}
