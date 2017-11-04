package com.ragazm.jsontest1.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ragazm.jsontest1.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andris on 002 02.11.17.
 */

public class JSONResponse {

    @SerializedName("Search")
    @Expose
    public List<Movie> data = new ArrayList<>();

    @SerializedName("Response")
    @Expose
    public String response;

    @SerializedName("totalResults")
    @Expose
    public String totalResults;


}
