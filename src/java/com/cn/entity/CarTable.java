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
public class CarTable {
    private int carID;
    private String systemNo;
    private String carNumber;
    private String carType;
    private float carWeight;
    private float carLength;
//    private String maintainDate;
//    private String annualDate;
//    private String maintainExpireDate;
    private String registerTime;
    private String insuranceExpireTime;
    private String carImagepath1;
    private String carImagepath2;
    private String carImagepath3;
    private String remark;
    private int enterpriseID;
    private int driverID;
    private String driverName;
    private float longitude;
    private float latitude;
    private int applyStatus;
    private int authStatus;

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

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public float getCarWeight() {
        return carWeight;
    }

    public void setCarWeight(float carWeight) {
        this.carWeight = carWeight;
    }

    public float getCarLength() {
        return carLength;
    }

    public void setCarLength(float carLength) {
        this.carLength = carLength;
    }

    public String getCarImagepath1() {
        return carImagepath1;
    }

    public void setCarImagepath1(String carImagepath1) {
        this.carImagepath1 = carImagepath1;
    }

    public String getCarImagepath2() {
        return carImagepath2;
    }

    public void setCarImagepath2(String carImagepath2) {
        this.carImagepath2 = carImagepath2;
    }

    public String getCarImagepath3() {
        return carImagepath3;
    }

    public void setCarImagepath3(String carImagepath3) {
        this.carImagepath3 = carImagepath3;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getEnterpriseID() {
        return enterpriseID;
    }

    public void setEnterpriseID(int enterpriseID) {
        this.enterpriseID = enterpriseID;
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

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getInsuranceExpireTime() {
        return insuranceExpireTime;
    }

    public void setInsuranceExpireTime(String insuranceExpireTime) {
        this.insuranceExpireTime = insuranceExpireTime;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public int getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(int applyStatus) {
        this.applyStatus = applyStatus;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }
}
