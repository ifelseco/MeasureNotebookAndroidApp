package com.javaman.olcudefteri.orders.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by javaman on 07.03.2018.
 */

@Data
public class ProductDetailModel {

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
}
