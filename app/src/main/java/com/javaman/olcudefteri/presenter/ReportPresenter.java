package com.javaman.olcudefteri.presenter;

public interface ReportPresenter {
    void getEndOfDay(String headerData);
    void getNextMeasure(String headerData);
    void getNextDelivery(String headerData);
}
