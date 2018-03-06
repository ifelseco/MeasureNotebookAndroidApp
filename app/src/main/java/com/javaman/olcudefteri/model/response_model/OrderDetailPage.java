package com.javaman.olcudefteri.model.response_model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javaman on 26.02.2018.
 */

public class OrderDetailPage {

    private List<OrderDetailResponseModel> content=new ArrayList<>();
    private int totalPages;
    private int totalElements;
    private boolean last;
    private int numberOfElements;
    private boolean first;
    private int size;
    private int number;
    //private Sort sort;


    public List<OrderDetailResponseModel> getContent() {
        return content;
    }

    public void setContent(List<OrderDetailResponseModel> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
