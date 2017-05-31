package com.expansion.lg.kimaru.lggpscollector;

/**
 * Created by kimaru on 5/25/17.
 */

public class GpsData {
    String uuid, chpPhone, referenceId, country, activityType, approximateTimeToResolve;
    Long dateAdded;
    boolean gpsOn;
    Double latitude, longitude;
    int color = -1;

    public GpsData(){}
    public GpsData(String uuid, String chpPhone, String referenceId, String country,
                   Long dateAdded, boolean gpsOn, String activityType, Double latitude,
                   Double longitude, String approximateTimeToResolve){
        this.uuid = uuid;
        this.chpPhone = chpPhone;
        this.referenceId = referenceId;
        this.gpsOn = gpsOn;
        this.activityType = activityType;
        this.approximateTimeToResolve = approximateTimeToResolve;
        this.dateAdded = dateAdded;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
    }
    //get

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Long getDateAdded() {
        return dateAdded;
    }


    public String getChpPhone() {
        return chpPhone;
    }


    public String getUuid() {
        return uuid;
    }

    public void setChpPhone(String chpPhone) {
        this.chpPhone = chpPhone;
    }



    public void setDateAdded(Long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getApproximateTimeToResolve() {
        return approximateTimeToResolve;
    }

    public void setApproximateTimeToResolve(String approximateTimeToResolve) {
        this.approximateTimeToResolve = approximateTimeToResolve;
    }

    public boolean isGpsOn() {
        return gpsOn;
    }

    public void setGpsOn(boolean gpsOn) {
        this.gpsOn = gpsOn;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
