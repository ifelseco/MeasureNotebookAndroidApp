package com.javaman.olcudefteri.api.model.response;

import lombok.Data;

/**
 * Created by javaman on 11.02.2018.
 */

@Data
public class ApiError {

    private int status;
    private String message;

}
