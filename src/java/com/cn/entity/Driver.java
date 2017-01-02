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
public class Driver {
    private  String userType;
    private int driverID;
    private String driverName;
    private String dCarID;
    private String dPhoneNumber;
    private String dLicense;
    private String enterpriseID;
    private String enterpriseName;
    private String dUserName;
    private String dPassword;
    private int applyStatus;
    private int authStatus;
    private String loginImei;

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

    public String getdCarID() {
        return dCarID;
    }

    public void setdCarID(String dCarID) {
        this.dCarID = dCarID;
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

    public String getEnterpriseID() {
        return enterpriseID;
    }

    public void setEnterpriseID(String enterpriseID) {
        this.enterpriseID = enterpriseID;
    }

    public String getdUserName() {
        return dUserName;
    }

    public void setdUserName(String dUserName) {
        this.dUserName = dUserName;
    }

    public String getdPassword() {
        return dPassword;
    }

    public void setdPassword(String dPassword) {
        this.dPassword = dPassword;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
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

    public String getLoginImei() {
        return loginImei;
    }

    public void setLoginImei(String loginImei) {
        this.loginImei = loginImei;
    }
}
