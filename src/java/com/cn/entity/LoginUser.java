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
public class LoginUser {
    private int userID;
    /**
     * 1 -- 企业用户 | 2 -- 司机 | 3 -- 经销商 | 4 -- 运输处
     */
    private int userType;
    private String loginName;
    private String loginTime;
    private String imei;
    private String lastOperateTime;
    private String updateTime;
    
    private String phoneNum;
    private String address;
    private String company;
    private int authStatus;
    
    public int enterpriseType;
    public int enterpriseID;
    public String enterpriseUserName;
    public String enterpriseAddress;
    public String enterprisePhoneNumber;
    public int distributorID;
    public String distributorName;
    public String distributorUserName;
    public String distributorAddress;
    public String distributorPhoneNumber;
    public String distributorType;
    public String distributorCompany;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getLastOperateTime() {
        return lastOperateTime;
    }

    public void setLastOperateTime(String lastOperateTime) {
        this.lastOperateTime = lastOperateTime;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    
}
