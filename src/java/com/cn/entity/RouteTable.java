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
public class RouteTable {
    
    private int routeID;
    private float routeStartLat;
    private float routeStartLon;
    private String routeStartName;
    private float routeEndLat;
    private float routeEndLon;
    private String routeEndName;
    private float routeLength;

    public int getRouteID() {
        return routeID;
    }

    public void setRouteID(int routeID) {
        this.routeID = routeID;
    }

    public float getRouteStartLat() {
        return routeStartLat;
    }

    public void setRouteStartLat(float routeStartLat) {
        this.routeStartLat = routeStartLat;
    }

    public float getRouteStartLon() {
        return routeStartLon;
    }

    public void setRouteStartLon(float routeStartLon) {
        this.routeStartLon = routeStartLon;
    }

    public String getRouteStartName() {
        return routeStartName;
    }

    public void setRouteStartName(String routeStartName) {
        this.routeStartName = routeStartName;
    }

    public float getRouteEndLat() {
        return routeEndLat;
    }

    public void setRouteEndLat(float routeEndLat) {
        this.routeEndLat = routeEndLat;
    }

    public float getRouteEndLon() {
        return routeEndLon;
    }

    public void setRouteEndLon(float routeEndLon) {
        this.routeEndLon = routeEndLon;
    }

    public String getRouteEndName() {
        return routeEndName;
    }

    public void setRouteEndName(String routeEndName) {
        this.routeEndName = routeEndName;
    }

    public float getRouteLength() {
        return routeLength;
    }

    public void setRouteLength(float routeLength) {
        this.routeLength = routeLength;
    }
    
}
