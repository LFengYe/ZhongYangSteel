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
public class GoodsType {
    private String goodsID;
    private String goodsName;
    private String goodsModel;
    private String goodsType;
    private int goodsNum;
    private float goodsWeight;
    private int goodsSequence;
    private String goodsWarehouse;
    private String gateSentryId;
    private String gateSentryName;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsModel() {
        return goodsModel;
    }

    public void setGoodsModel(String goodsModel) {
        this.goodsModel = goodsModel;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public float getGoodsWeight() {
        return goodsWeight;
    }

    public void setGoodsWeight(float goodsWeight) {
        this.goodsWeight = goodsWeight;
    }

    public String getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(String goodsID) {
        this.goodsID = goodsID;
    }

    public int getGoodsSequence() {
        return goodsSequence;
    }

    public void setGoodsSequence(int goodsSequence) {
        this.goodsSequence = goodsSequence;
    }

    public String getGoodsWarehouse() {
        return goodsWarehouse;
    }

    public void setGoodsWarehouse(String goodsWarehouse) {
        this.goodsWarehouse = goodsWarehouse;
    }

    public String getGateSentryId() {
        return gateSentryId;
    }

    public void setGateSentryId(String gateSentryId) {
        this.gateSentryId = gateSentryId;
    }

    public String getGateSentryName() {
        return gateSentryName;
    }

    public void setGateSentryName(String gateSentryName) {
        this.gateSentryName = gateSentryName;
    }
}
