package com.omug.androidfinalprojectadvance;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.sql.Date;

@Entity
public class Location implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "locationId")
    private long id;
    @ColumnInfo(name = "locationTitle")
    private String title;
    @ColumnInfo(name = "locationSubtitle")
    private String subtitle;
    @ColumnInfo(name = "locationLatitude")
    private double latitude;
    @ColumnInfo(name = "locationLongitude")
    private double longitude;
    @ColumnInfo(name = "locationGender")
    private String gender;
    @ColumnInfo(name = "locationBirtdate")
    private Date birtdate;

    private transient LatLng location;

    public Location(String title, String subtitle, double latitude, double longitude, String gender, Date birtdate) {
        this.title = title;
        this.subtitle = subtitle;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gender = gender;
        this.birtdate = birtdate;
        this.location = new LatLng(latitude, longitude);
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getSubtitle() { return subtitle; }

    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }

    public double getLatitude() { return latitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }

    public LatLng getLocation() { return location; }

    public void setLocation(LatLng location) { this.location = location; }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public Date getBirtdate() { return birtdate; }

    public void setBirtdate(Date birtdate) { this.birtdate = birtdate; }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", gender='" + gender + '\'' +
                ", birtdate=" + birtdate +
                ", location=" + location +
                '}';
    }
}
