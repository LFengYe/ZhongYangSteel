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
public class Transport {
    private int transportID;
    private String transportName;
    private String transportUserName;
    private String transportPassword;
    private String transportType;
    private String transportPhoneNumber;

    public int getTransportID() {
        return transportID;
    }

    public void setTransportID(int transportID) {
        this.transportID = transportID;
    }

    public String getTransportName() {
        return transportName;
    }

    public void setTransportName(String transportName) {
        this.transportName = transportName;
    }

    public String getTransportPassword() {
        return transportPassword;
    }

    public void setTransportPassword(String transportPassword) {
        this.transportPassword = transportPassword;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getTransportPhoneNumber() {
        return transportPhoneNumber;
    }

    public void setTransportPhoneNumber(String transportPhoneNumber) {
        this.transportPhoneNumber = transportPhoneNumber;
    }

    public String getTransportUserName() {
        return transportUserName;
    }

    public void setTransportUserName(String transportUserName) {
        this.transportUserName = transportUserName;
    }
    
}
