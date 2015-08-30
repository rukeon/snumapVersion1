package com.snumap.snumapversion1.API;

import com.snumap.snumapversion1.Model.RouteModel;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by rukeon01 on 2015-08-28.
 */
public interface RouteApi {
    @GET("/navigator.php")      //here is the other url part.best way is to start using /
    public void getRoute(@Query("from") String from, @Query("to") String to, Callback<RouteModel> response);
}
