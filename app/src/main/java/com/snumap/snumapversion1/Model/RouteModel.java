package com.snumap.snumapversion1.Model;

import com.google.gson.annotations.Expose;

/**
 * Created by rukeon01 on 2015-08-28.
 */
public class RouteModel {
    @Expose
    private int[] latitude;

    @Expose
    private int[] longitude;

    public int[] getLatitude()
    {
        return latitude;
    }

    public void setLatitude(int[] latitude) {
        this.latitude = latitude;
    }

    public int[] getLongitude()
    {
        return longitude;
    }

    public void setLongitude(int[] longitude){
        this.longitude = longitude;
    }
}
