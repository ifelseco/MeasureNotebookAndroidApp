package com.javaman.olcudefteri.model;

import lombok.Data;

/**
 * Created by javaman on 06.03.2018.
 */

@Data
public class Paginator {

    private int itemPerPage;
    private int totalItem;
    private int remainingItem;
    private int currentPageNumber;
    private int totalPage;
    private int first;
    private boolean isFirstPage;
    private boolean isLastPage;

}
