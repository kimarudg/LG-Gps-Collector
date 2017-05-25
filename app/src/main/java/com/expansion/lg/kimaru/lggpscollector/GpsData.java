package com.expansion.lg.kimaru.lggpscollector;

/**
 * Created by kimaru on 5/25/17.
 */

public class GpsData {
    String uuid, chpName, chpPhone, chpUuid, chpRecorduuid, country,
            supervisorName, supervisoPhone, supervisorEmail;
    Long dateAdded;
    Double latitude, longitude;

    int color = -1;

    public GpsData(){}
    public GpsData(String uuid, String chpName, String chpPhone, String chpUuid, String chpRecorduuid,
                   String supervisorName, String supervisoPhone, String supervisorEmail,
                   Long dateAdded, Double latitude, Double longitude, String country){
        this.uuid = uuid;
        this.chpName = chpName;
        this.chpPhone = chpPhone;
        this.chpUuid = chpUuid;
        this.chpRecorduuid = chpRecorduuid;
        this.supervisorName = supervisorName;
        this.supervisoPhone = supervisoPhone;
        this.supervisorEmail = supervisorEmail;
        this.dateAdded = dateAdded;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
    }
    //get

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Long getDateAdded() {
        return dateAdded;
    }

    public String getChpName() {
        return chpName;
    }

    public String getChpPhone() {
        return chpPhone;
    }

    public String getChpRecorduuid() {
        return chpRecorduuid;
    }

    public String getChpUuid() {
        return chpUuid;
    }

    public String getSupervisoPhone() {
        return supervisoPhone;
    }

    public String getSupervisorEmail() {
        return supervisorEmail;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setChpName(String chpName) {
        this.chpName = chpName;
    }

    public void setChpPhone(String chpPhone) {
        this.chpPhone = chpPhone;
    }

    public void setChpRecorduuid(String chpRecorduuid) {
        this.chpRecorduuid = chpRecorduuid;
    }

    public void setChpUuid(String chpUuid) {
        this.chpUuid = chpUuid;
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

    public void setSupervisoPhone(String supervisoPhone) {
        this.supervisoPhone = supervisoPhone;
    }

    public void setSupervisorEmail(String supervisorEmail) {
        this.supervisorEmail = supervisorEmail;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
