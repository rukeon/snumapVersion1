package com.snumap.snumapversion1;

import java.util.Comparator;

/**
 * Created by rukeon01 on 2015-08-29.
 */
public class CustomComparatorSR implements Comparator<UserSearch> {

    @Override
    public int compare(UserSearch one, UserSearch another) {
        return another.getDate().compareTo(one.getDate());
    }
}
