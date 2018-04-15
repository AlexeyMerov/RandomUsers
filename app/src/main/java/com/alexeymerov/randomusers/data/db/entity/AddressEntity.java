package com.alexeymerov.randomusers.data.db.entity;

import android.arch.persistence.room.Embedded;

import com.squareup.moshi.Json;

public class AddressEntity {

    @Json(name = "zipcode")
    private String mZipcode;

    @Json(name = "geo")
    @Embedded
    private GeoEntity mGeoEntity;

    @Json(name = "suite")
    private String mSuite;

    @Json(name = "city")
    private String mCity;

    @Json(name = "street")
    private String mStreet;

    public String getZipcode() {
        return mZipcode;
    }

    public void setZipcode(String zipcode) {
        mZipcode = zipcode;
    }

    public GeoEntity getGeoEntity() {
        return mGeoEntity;
    }

    public void setGeoEntity(GeoEntity geoEntity) {
        mGeoEntity = geoEntity;
    }

    public String getSuite() {
        return mSuite;
    }

    public void setSuite(String suite) {
        mSuite = suite;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getStreet() {
        return mStreet;
    }

    public void setStreet(String street) {
        mStreet = street;
    }
}