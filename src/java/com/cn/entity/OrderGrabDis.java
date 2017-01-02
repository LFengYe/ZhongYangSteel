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
public class OrderGrabDis {
    private int taskID;
    private String taskSerial;
    private int carID;
    private String systemNo;
    private String carNumber;
    private int driverID;
    private String driverName;
    private String dPhoneNumber;
    private String dLicense;
    private int grabStatus;
    private int revokeStatus;
    private int inFactoryStatus;

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public String getSystemNo() {
        return systemNo;
    }

    public void setSystemNo(String systemNo) {
        this.systemNo = systemNo;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public int getDriverID() {
        return driverID;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getdPhoneNumber() {
        return dPhoneNumber;
    }

    public void setdPhoneNumber(String dPhoneNumber) {
        this.dPhoneNumber = dPhoneNumber;
    }

    public String getdLicense() {
        return dLicense;
    }

    public void setdLicense(String dLicense) {
        this.dLicense = dLicense;
    }

    public int getGrabStatus() {
        return grabStatus;
    }

    public void setGrabStatus(int grabStatus) {
        this.grabStatus = grabStatus;
    }

    public int getRevokeStatus() {
        return revokeStatus;
    }

    public void setRevokeStatus(int revokeStatus) {
        this.revokeStatus = revokeStatus;
    }

    public int getInFactoryStatus() {
        return inFactoryStatus;
    }

    public void setInFactoryStatus(int inFactoryStatus) {
        this.inFactoryStatus = inFactoryStatus;
    }

    public String getTaskSerial() {
        return taskSerial;
    }

    public void setTaskSerial(String taskSerial) {
        this.taskSerial = taskSerial;
    }
    
}
