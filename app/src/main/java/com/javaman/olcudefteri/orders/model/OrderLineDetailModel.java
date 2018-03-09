package com.javaman.olcudefteri.orders.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by javaman on 07.03.2018.
 */

@Data
public class OrderLineDetailModel {

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
    private int locationName;

    @SerializedName("mechanismStatus")
    @Expose
    private int mechanismStatus;

    @SerializedName("fonType")
    @Expose
    private int fonType;

    @SerializedName("propertyModelName")
    @Expose
    private String propertyModelName;


}
