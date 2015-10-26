package com.example.naveen.gps_tracking_with_sql;

import java.io.Serializable;

/**
 * Created by Naveen on 15-10-2015.
 */
public class Location1 implements Serializable {

    private int _id;

    private double _latitude;
    private double _longitude;
    private String _time;

    public Location1() {
    }

    public Location1(double latitude, double longitude, String time) {
        this._latitude = latitude;
        this._longitude = longitude;
        this._time = time;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_latitude(long _latitude) {
        this._latitude = _latitude;
    }

    public void set_longitude(long _longitude) {
        this._longitude = _longitude;
    }

    public void set_time(String _time) {
        this._time = _time;
    }

    public int get_id() {
        return _id;
    }

    public double get_latitude() {
        return _latitude;
    }

    public double get_longitude() {
        return _longitude;
    }

    public String get_time() {
        return _time;
    }

}
