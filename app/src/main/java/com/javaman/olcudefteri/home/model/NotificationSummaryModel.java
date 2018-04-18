package com.javaman.olcudefteri.home.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.javaman.olcudefteri.api.model.response.BaseModel;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class NotificationSummaryModel implements Parcelable {

    private BaseModel baseModel;
    private List<NotificationDetailModel> notificationDetailModelList;

    public NotificationSummaryModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.baseModel, flags);
        dest.writeList(this.notificationDetailModelList);
    }

    protected NotificationSummaryModel(Parcel in) {
        this.baseModel = in.readParcelable(BaseModel.class.getClassLoader());
        this.notificationDetailModelList = new ArrayList<NotificationDetailModel>();
        in.readList(this.notificationDetailModelList, NotificationDetailModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<NotificationSummaryModel> CREATOR = new Parcelable.Creator<NotificationSummaryModel>() {
        @Override
        public NotificationSummaryModel createFromParcel(Parcel source) {
            return new NotificationSummaryModel(source);
        }

        @Override
        public NotificationSummaryModel[] newArray(int size) {
            return new NotificationSummaryModel[size];
        }
    };
}
