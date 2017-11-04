package com.ragazm.jsontest1.json;

import com.ragazm.jsontest1.json.JSONResponse;

import retrofit2.Call;
import retrofit2.http.GET;

import retrofit2.http.Query;

/**
 * Created by Andris on 002 02.11.17.
 */

public interface RequestInterface {

    @GET("/?")
    Call<JSONResponse> getSearch(@Query("s") String title,
                                 @Query("apikey") String apiKey);

    @GET("/?")
    Call<MovieDetails> getDetails(@Query("i") String imdbId,
                                  @Query("apikey") String apiKey);

}
