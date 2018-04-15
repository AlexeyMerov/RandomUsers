package com.alexeymerov.randomusers.data.db.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.squareup.moshi.Json;

import java.util.Objects;


@Entity(tableName = "user_entity")
public class UserEntity {

    @PrimaryKey
    @Json(name = "id")
    private Long mId;

    @Json(name = "name")
    private String mName;

    @Json(name = "email")
    private String mEmail;

    @Json(name = "phone")
    private String mPhoneNumber;

    @Json(name = "website")
    private String mWebsite;

    @Json(name = "address")
    @Embedded
    private AddressEntity mAddress;

    @Json(name = "username")
    private String mUserName;

    private String mPhotoUrl;
    private int mColor;


    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public void setId(Integer id) {
        mId = id.longValue();
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return getColor() == that.getColor() &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getUserName(), that.getUserName()) &&
                Objects.equals(getEmail(), that.getEmail()) &&
                Objects.equals(getPhotoUrl(), that.getPhotoUrl()) &&
                Objects.equals(getPhoneNumber(), that.getPhoneNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getUserName(), getEmail(), getPhotoUrl(), getPhoneNumber(), getColor());
    }

    public String getWebsite() {
        return mWebsite;
    }

    public void setWebsite(String website) {
        mWebsite = website;
    }

    public AddressEntity getAddress() {
        return mAddress;
    }

    public void setAddress(AddressEntity address) {
        mAddress = address;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mPhoneNumber='" + mPhoneNumber + '\'' +
                ", mWebsite='" + mWebsite + '\'' +
                ", mAddress=" + mAddress +
                ", mUserName='" + mUserName + '\'' +
                ", mPhotoUrl='" + mPhotoUrl + '\'' +
                ", mColor=" + mColor +
                '}';
    }
}
