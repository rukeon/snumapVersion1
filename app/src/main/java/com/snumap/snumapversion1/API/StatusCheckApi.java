package com.snumap.snumapversion1.API;

import com.snumap.snumapversion1.Model.StatusModel;
import com.snumap.snumapversion1.Status;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by rukeon01 on 2015-08-19.
 */
public interface StatusCheckApi {
    @POST("/StatusCheck.php")      //here is the other url part.best way is to start using /
    public void getStatusInfo(@Body Status status, Callback<StatusModel> response);
}
