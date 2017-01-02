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
public class OrderDetail {
    public String GoodsID;
    public String GoodsName;
    public String GoodsNum;
    public String GoodsCoefficients;
    public String GateSentryID;
    public String GateSentryName;

    public String getGoodsID() {
        return GoodsID;
    }

    public void setGoodsID(String GoodsID) {
        this.GoodsID = GoodsID;
    }

    public String getGoodsName() {
        return GoodsName;
    }

    public void setGoodsName(String GoodsName) {
        this.GoodsName = GoodsName;
    }

    public String getGoodsNum() {
        return GoodsNum;
    }

    public void setGoodsNum(String GoodsNum) {
        this.GoodsNum = GoodsNum;
    }

    public String getGateSentryID() {
        return GateSentryID;
    }

    public void setGateSentryID(String GateSentryID) {
        this.GateSentryID = GateSentryID;
    }

    public String getGateSentryName() {
        return GateSentryName;
    }

    public void setGateSentryName(String GateSentryName) {
        this.GateSentryName = GateSentryName;
    }

    public String getGoodsCoefficients() {
        return GoodsCoefficients;
    }

    public void setGoodsCoefficients(String GoodsCoefficients) {
        this.GoodsCoefficients = GoodsCoefficients;
    }
    
}
