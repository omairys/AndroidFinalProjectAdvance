package com.omug.androidlabtest2;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.android.gms.maps.model.LatLng;
import java.io.Serializable;

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
    private transient LatLng location;

    public Location(String title, String subtitle, double latitude, double longitude) {
        this.title = title;
        this.subtitle = subtitle;
        this.latitude = latitude;
        this.longitude = longitude;
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
}
