/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.entity;

/**
 *
 * @author LFeng
 */
public class CarLocation {
    private static int locationRecordCounter;

    public static int getLocationRecordCounter() {
        return locationRecordCounter;
    }

    public static void setLocationRecordCounter(int aLocationRecordCounter) {
        locationRecordCounter = aLocationRecordCounter;
    }
    
    private int carID;
    private String carNO;
    private String systemNo;
    private String gpsTime;
    private float latitude;
    private float longitude;
    private float baiduLatitude;
    private float baiduLongitude;
    private int velocity;
    private int angle;

    public String getGpsTime() {
        return gpsTime;
    }

    public void setGpsTime(String gpsTime) {
        this.gpsTime = gpsTime;
    }
    
    public String getCarNO() {
        return carNO;
    }

    public void setCarNO(String carNO) {
        this.carNO = carNO;
    }

    public String getSystemNo() {
        return systemNo;
    }

    public void setSystemNo(String systemNo) {
        this.systemNo = systemNo;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public float getBaiduLatitude() {
        return baiduLatitude;
    }

    public void setBaiduLatitude(float baiduLatitude) {
        this.baiduLatitude = baiduLatitude;
    }

    public float getBaiduLongitude() {
        return baiduLongitude;
    }

    public void setBaiduLongitude(float baiduLongitude) {
        this.baiduLongitude = baiduLongitude;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }
    
}
