package com.javaman.olcudefteri.orders.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.javaman.olcudefteri.api.model.response.BaseModel;

import lombok.Data;

/**
 * Created by javaman on 26.02.2018.
 */

@Data
public class OrderSummaryReponseModel implements Parcelable {

    private BaseModel baseModel;

    private OrderDetailPage orderDetailPage;

    public OrderSummaryReponseModel() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.baseModel, flags);
        dest.writeParcelable(this.orderDetailPage, flags);
    }

    protected OrderSummaryReponseModel(Parcel in) {
        this.baseModel = in.readParcelable(BaseModel.class.getClassLoader());
        this.orderDetailPage = in.readParcelable(OrderDetailPage.class.getClassLoader());
    }

    public static final Parcelable.Creator<OrderSummaryReponseModel> CREATOR = new Parcelable.Creator<OrderSummaryReponseModel>() {
        @Override
        public OrderSummaryReponseModel createFromParcel(Parcel source) {
            return new OrderSummaryReponseModel(source);
        }

        @Override
        public OrderSummaryReponseModel[] newArray(int size) {
            return new OrderSummaryReponseModel[size];
        }
    };
}
