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
public class SiteTalble {
    private int siteID;
    private String siteName;
    private float siteLatitude;
    private float siteLongitude;
    private int siteType;

    public int getSiteID() {
        return siteID;
    }

    public void setSiteID(int siteID) {
        this.siteID = siteID;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public float getSiteLatitude() {
        return siteLatitude;
    }

    public void setSiteLatitude(float siteLatitude) {
        this.siteLatitude = siteLatitude;
    }

    public float getSiteLongitude() {
        return siteLongitude;
    }

    public void setSiteLongitude(float siteLongitude) {
        this.siteLongitude = siteLongitude;
    }

    public int getSiteType() {
        return siteType;
    }

    public void setSiteType(int siteType) {
        this.siteType = siteType;
    }
    
}
