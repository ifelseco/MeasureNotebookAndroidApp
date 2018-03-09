package com.javaman.olcudefteri.orders.model.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Created by javaman on 26.02.2018.
 */

@Data
public class OrderDetailPage {

    private List<OrderDetailResponseModel> content=new ArrayList<>();
    private int totalPages;
    private int totalElements;
    private boolean last;
    private int numberOfElements;
    private boolean first;
    private int size;
    private int number;
    private List<SortModel> sort=new ArrayList<>();



}
