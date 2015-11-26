package com.example.darre_000.scoutr;

/**
 * Created by charlespierse on 26/11/15.
 */
public class LocationInfo {
    public boolean accessible;
    public boolean wifi;
    public boolean wc;
    public boolean sunlight;
    public boolean power;
    public String locationName;
    public double latitude;
    public double longitude;
    public String locationComment;
    public LocationInfo(boolean accessible,boolean wifi,boolean wc,boolean sunlight,boolean power,
                        String locationName,String locationComment,double latitude, double longitude){

        this.accessible = accessible;
        this.wifi = wifi;
        this.wc = wc;
        this.sunlight = sunlight;
        this.power = power;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationComment = locationComment;
        this.locationName = locationName;

    }


}
