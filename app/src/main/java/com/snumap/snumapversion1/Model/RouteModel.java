package com.snumap.snumapversion1.Model;

import com.google.gson.annotations.Expose;

/**
 * Created by rukeon01 on 2015-08-28.
 */
public class RouteModel {
    @Expose
    private String latitude;

    @Expose
    private String longitude;

    public String getLatitude()
    {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude()
    {
        return longitude;
    }

    public void setLongitude(String longitude){
        this.longitude = longitude;
    }
}
