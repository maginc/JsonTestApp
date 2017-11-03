package com.ragazm.jsontest1;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Andris on 002 02.11.17.
 */

public class JSONResponse {

    @SerializedName("Search")
    @Expose
    private Movie[] movies;

    public Movie[] getMovies() {
        return movies;
    }
}
