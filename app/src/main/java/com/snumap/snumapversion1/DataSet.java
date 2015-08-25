package com.snumap.snumapversion1;

/**
 * Created by rukeon01 on 2015-07-16.
 */
public class DataSet {
    private String _id; // 인문관1
    private String name; // 1 (1동)
    private String number; // 17562889 (다음 api id)
    private String latitude; // 37.46...
    private String longitude; // 126.99...
    private int positionChecker = 100;

    public DataSet() {

    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() { return longitude; }

    public int getPositionChecker() { return positionChecker; }

    public void set_id(String id) {
        this._id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setLatitude(String latitude) { this.latitude = latitude; }

    public void setLongitude(String longitude) { this.longitude = longitude; }

    public void setPositionChecker(int positionChecker)
    {
        this.positionChecker = positionChecker;
    }
}
