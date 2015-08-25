package com.snumap.snumapversion1;

/**
 * Created by rukeon01 on 2015-07-29.
 */
public class SlidingListData {
    private int mIcon;
    private String mActivity;

    public SlidingListData(int mIcon, String mActivity)
    {
        this.mIcon = mIcon;
        this.mActivity = mActivity;
    }

    public int getmIcon() {
        return mIcon;
    }

    public String getmActivity() {
        return mActivity;
    }
}
