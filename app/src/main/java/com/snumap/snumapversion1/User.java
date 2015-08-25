package com.snumap.snumapversion1;

import java.util.Date;

/**
 * Created by rukeon01 on 2015-08-21.
 */
public class User  {
    private String name;
    private Date date;

    public User(String name) {
        this.name = name;
        // 현재 시간을 msec으로 구한다.
        long now = System.currentTimeMillis();
        // 현재 시간을 저장 한다.
        Date date = new Date(now);
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    // 여기서 하면 안되겠다...
//    @Override
//    public int compareTo(Object another) {
//        User tmp = (User) another;
//        return this.date.compareTo(tmp.date);
//    }
}
