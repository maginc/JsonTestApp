package com.ragazm.jsontest1;

import retrofit2.Call;
import retrofit2.http.GET;

import retrofit2.http.Query;

/**
 * Created by Andris on 002 02.11.17.
 */

public interface RequestInterface {

    @GET("/?")
    Call<JSONResponse> getJSON(@Query("s") String title,
                               @Query("apikey") String apiKey);
}
