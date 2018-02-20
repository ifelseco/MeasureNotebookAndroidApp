package com.javaman.olcudefteri.notification;

import com.javaman.olcudefteri.api.response_model.AddCustomerResponse;
import com.javaman.olcudefteri.api.response_model.BaseResponse;
import com.javaman.olcudefteri.model.CustomerDetailModel;
import com.javaman.olcudefteri.model.FirebaseRegIdModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by javaman on 19.02.2018.
 */

public interface FirebaseService {

    @POST("/firebase/add/regId")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<BaseResponse> sendRegId(@Header("X-Auth-Token") String xAuthToken,
                                 @Body FirebaseRegIdModel regIdModel);
}
