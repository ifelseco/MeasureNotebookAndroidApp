package com.javaman.olcudefteri.orders.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Created by javaman on 26.02.2018.
 */

@Data
public class OrderDetailPage implements Parcelable {

    private List<OrderDetailResponseModel> content=new ArrayList<>();
    private int totalPages;
    private int totalElements;
    private boolean last;
    private int numberOfElements;
    private boolean first;
    private int size;
    private int number;
    private List<SortModel> sort=new ArrayList<>();

    public OrderDetailPage() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.content);
        dest.writeInt(this.totalPages);
        dest.writeInt(this.totalElements);
        dest.writeByte(this.last ? (byte) 1 : (byte) 0);
        dest.writeInt(this.numberOfElements);
        dest.writeByte(this.first ? (byte) 1 : (byte) 0);
        dest.writeInt(this.size);
        dest.writeInt(this.number);
        dest.writeList(this.sort);
    }

    protected OrderDetailPage(Parcel in) {
        this.content = in.createTypedArrayList(OrderDetailResponseModel.CREATOR);
        this.totalPages = in.readInt();
        this.totalElements = in.readInt();
        this.last = in.readByte() != 0;
        this.numberOfElements = in.readInt();
        this.first = in.readByte() != 0;
        this.size = in.readInt();
        this.number = in.readInt();
        this.sort = new ArrayList<SortModel>();
        in.readList(this.sort, SortModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<OrderDetailPage> CREATOR = new Parcelable.Creator<OrderDetailPage>() {
        @Override
        public OrderDetailPage createFromParcel(Parcel source) {
            return new OrderDetailPage(source);
        }

        @Override
        public OrderDetailPage[] newArray(int size) {
            return new OrderDetailPage[size];
        }
    };
}
