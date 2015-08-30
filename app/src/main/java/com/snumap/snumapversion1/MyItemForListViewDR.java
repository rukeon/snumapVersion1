package com.snumap.snumapversion1;

/**
 * Created by rukeon01 on 2015-08-30.
 */
public class MyItemForListViewDR {
    int iconButton;
    int iconArrow;
    UserSearch search;

    MyItemForListViewDR(int icon1, UserSearch mySearch, int icon2) {
        this.iconButton = icon1;
        this.iconArrow = icon2;
        this.search = mySearch;
    }
}
