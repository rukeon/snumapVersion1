package com.snumap.snumapversion1.Model;

import com.google.gson.annotations.Expose;

/**
 * Created by rukeon01 on 2015-08-19.
 */
public class StatusModel {
    @Expose
    private boolean status;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status)
    {
        this.status = status;
    }
}
