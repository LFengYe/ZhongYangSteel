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
public class AccessFrequency {
    private String ipAddress;//IP地址
    private int userID;//用户ID
    private int userType;//用户类型
    private long accessTime;//访问时间
    private int overLimitcount;//超过限制次数
    private boolean isEnable;//是否可用
    private String interfaceName;//接口名称
    private String imei;
    private int ipChangeTimes;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
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

    public long getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(long accessTime) {
        this.accessTime = accessTime;
    }

    public int getOverLimitcount() {
        return overLimitcount;
    }

    public void setOverLimitcount(int overLimitcount) {
        this.overLimitcount = overLimitcount;
    }

    public boolean isIsEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public int getIpChangeTimes() {
        return ipChangeTimes;
    }

    public void setIpChangeTimes(int ipChangeTimes) {
        this.ipChangeTimes = ipChangeTimes;
    }
    
}
