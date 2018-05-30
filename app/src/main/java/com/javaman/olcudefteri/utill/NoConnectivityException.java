package com.javaman.olcudefteri.utill;

import java.io.IOException;

public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return "No connectivity exception";
    }
}
