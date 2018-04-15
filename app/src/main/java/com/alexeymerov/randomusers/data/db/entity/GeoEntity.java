package com.alexeymerov.randomusers.data.db.entity;

import com.squareup.moshi.Json;

public class GeoEntity {

    @Json(name = "lng")
    private double mLng;

    @Json(name = "lat")
    private double mLat;

    public double getLng() {
        return mLng;
    }

    public void setLng(double lng) {
        mLng = lng;
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        mLat = lat;
    }
}