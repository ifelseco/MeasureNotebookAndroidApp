package com.javaman.olcudefteri.orders.model.response;

import lombok.Data;

/**
 * Created by javaman on 07.03.2018.
 */

@Data
public class SortModel {

    private String direction;
    private String property;
    private boolean ignoreCase;
    private boolean descending;
    private String nullHandling;
    private boolean ascending;


}
