package com.ragazm.jsontest1.json;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Andris on 004 04.11.17.
 *
 */

public class APIClient {
    private static final String BASE_URL = "http://www.omdbapi.com";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){

        //Interceptors monitor, rewrite and retry calls
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return  retrofit;
    }

}
