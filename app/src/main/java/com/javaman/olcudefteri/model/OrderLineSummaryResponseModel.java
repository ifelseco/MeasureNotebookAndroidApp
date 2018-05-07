package com.javaman.olcudefteri.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Created by javaman on 07.03.2018.
 */

@Data
public class OrderLineSummaryResponseModel implements Parcelable {

    private BaseModel baseModel;
    private OrderDetailResponseModel order;
    private List<OrderLineDetailModel> orderLineDetailList=new ArrayList<>();

    public OrderLineSummaryResponseModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.baseModel, flags);
        dest.writeParcelable(this.order, flags);
        dest.writeTypedList(this.orderLineDetailList);
    }

    protected OrderLineSummaryResponseModel(Parcel in) {
        this.baseModel = in.readParcelable(BaseModel.class.getClassLoader());
        this.order = in.readParcelable(OrderDetailResponseModel.class.getClassLoader());
        this.orderLineDetailList = in.createTypedArrayList(OrderLineDetailModel.CREATOR);
    }

    public static final Parcelable.Creator<OrderLineSummaryResponseModel> CREATOR = new Parcelable.Creator<OrderLineSummaryResponseModel>() {
        @Override
        public OrderLineSummaryResponseModel createFromParcel(Parcel source) {
            return new OrderLineSummaryResponseModel(source);
        }

        @Override
        public OrderLineSummaryResponseModel[] newArray(int size) {
            return new OrderLineSummaryResponseModel[size];
        }
    };
}
