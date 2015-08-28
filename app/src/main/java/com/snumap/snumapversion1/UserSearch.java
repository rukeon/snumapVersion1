package com.snumap.snumapversion1;

import java.util.Date;

/**
 * Created by rukeon01 on 2015-08-28.
 */
public class UserSearch {
    private String from;
    private String to;
    private Date date;

    public UserSearch(String from, String to)
    {
        this.from = from;
        this.to = to;
        // 현재 시간을 msec으로 구한다.
        long now = System.currentTimeMillis();
        // 현재 시간을 저장 한다.
        Date date = new Date(now);
        this.date = date;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }
}
