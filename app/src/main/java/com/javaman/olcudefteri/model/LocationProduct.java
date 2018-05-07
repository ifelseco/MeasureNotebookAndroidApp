package com.javaman.olcudefteri.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Created by javaman on 16.03.2018.
 */

@Data
public class LocationProduct {

    private String locationName;
    private String locationType;
    private List<String> productValue=new ArrayList<>();
}
