package com.javaman.olcudefteri.notification;

import com.javaman.olcudefteri.api.model.response.BaseModel;

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
    Call<BaseModel> sendRegId(@Header("X-Auth-Token") String xAuthToken,
                              @Body FirebaseRegIdModel regIdModel);
}
