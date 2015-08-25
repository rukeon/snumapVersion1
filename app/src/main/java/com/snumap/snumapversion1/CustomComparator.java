package com.snumap.snumapversion1;

import java.util.Comparator;

/**
 * Created by rukeon01 on 2015-08-21.
 */
public class CustomComparator implements Comparator<User> {
    private boolean orderByTime = true;

    CustomComparator(boolean orderByTime)
    {
        this.orderByTime = orderByTime;
    }

    @Override
    public int compare(User one, User another) {
        if (orderByTime)
        {
            return another.getDate().compareTo(one.getDate());
        } else {
            return one.getName().compareTo(another.getName());
        }
    }
}
