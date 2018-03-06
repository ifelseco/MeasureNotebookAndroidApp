package com.javaman.olcudefteri.orders;

/**
 * Created by javaman on 06.03.2018.
 */

public class Paginator {

    private int itemPerPage;
    private int totalItem;
    private int remainingItem;
    private int currentPageNumber;
    private int totalPage;
    private int first;
    private boolean isFirstPage;
    private boolean isLastPage;

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public void setFirstPage(boolean firstPage) {
        isFirstPage = firstPage;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public int getItemPerPage() {
        return itemPerPage;
    }

    public void setItemPerPage(int itemPerPage) {
        this.itemPerPage = itemPerPage;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public int getRemainingItem() {
        return remainingItem;
    }

    public void setRemainingItem(int remainingItem) {
        this.remainingItem = remainingItem;
    }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    public void setCurrentPageNumber(int currentPageNumber) {
        this.currentPageNumber = currentPageNumber;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }
}
