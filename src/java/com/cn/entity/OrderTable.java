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
public class OrderTable {
    
    private static int taskRecordCount;

    public static int getTaskRecordCount() {
        return taskRecordCount;
    }

    public static void setTaskRecordCount(int aTaskRecordCount) {
        taskRecordCount = aTaskRecordCount;
    }
    
    private int debugIndex;
    private int taskID;
    private String taskSerial;
    private String taskEndTime;
    /**
     * 订单状态: -- 0 未发布 | 1 未完成 | 2 已完成
     */
    private int taskStatus;
    private String taskCreateTime;
    private String goodsType;
    /**
     * 定价或议价: 1 定价 | 2 议价
     */
    private int priceModel;
    private float price;
    /**
     * 抢单时长
     */
    private int timeCountDown;
    /**
     * 1 按重量数量 | 2 按车辆数量 | 3 指定车辆
     */
    private int taskType;
    private int startPlace;
    private String startPlaceName;
    private int endPlace;
    private String endPlaceName;
    private float carRenge;
    private int taskCarNum;//订单车辆数
    private float taskTonnage;//订单吨数
    private int enterpriseID;
    private String selectedCar;
    private String sendTime;
    private String carType;
    private float carLength;
    private float tLenth;
    private int factoryTime;
    private int distributorID;//经销商
    private int transportID;//运输处
    private String destinationDis;
    private String distributorContact;
    private String distributorPhoneNumber;
    private String distributorName;
    private int taxRate;
    private int pickupType;
    
    private int remainCarNum;//剩余车辆数
    private float remainTonnage;//剩余吨数
    private int grabedNum;//订单已抢成功单数
    private String serverCurTime;//服务器当前时间

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getTaskSerial() {
        return taskSerial;
    }

    public void setTaskSerial(String taskSerial) {
        this.taskSerial = taskSerial;
    }

    public String getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(String taskEndTime) {
        this.taskEndTime = taskEndTime;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskCreateTime() {
        return taskCreateTime;
    }

    public void setTaskCreateTime(String taskCreateTime) {
        this.taskCreateTime = taskCreateTime;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public int getPriceModel() {
        return priceModel;
    }

    public void setPriceModel(int priceModel) {
        this.priceModel = priceModel;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getTimeCountDown() {
        return timeCountDown;
    }

    public void setTimeCountDown(int timeCountDown) {
        this.timeCountDown = timeCountDown;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public int getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(int startPlace) {
        this.startPlace = startPlace;
    }

    public int getEndPlace() {
        return endPlace;
    }

    public void setEndPlace(int endPlace) {
        this.endPlace = endPlace;
    }

    public float getCarRenge() {
        return carRenge;
    }

    public void setCarRenge(float carRenge) {
        this.carRenge = carRenge;
    }

    public int getTaskCarNum() {
        return taskCarNum;
    }

    public void setTaskCarNum(int taskCarNum) {
        this.taskCarNum = taskCarNum;
    }

    public float getTaskTonnage() {
        return taskTonnage;
    }

    public void setTaskTonnage(float taskTonnage) {
        this.taskTonnage = taskTonnage;
    }

    public int getEnterpriseID() {
        return enterpriseID;
    }

    public void setEnterpriseID(int enterpriseID) {
        this.enterpriseID = enterpriseID;
    }

    public String getSelectedCar() {
        return selectedCar;
    }

    public void setSelectedCar(String selectedCar) {
        this.selectedCar = selectedCar;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public float gettLenth() {
        return tLenth;
    }

    public void settLenth(float tLenth) {
        this.tLenth = tLenth;
    }

    public int getFactoryTime() {
        return factoryTime;
    }

    public void setFactoryTime(int factoryTime) {
        this.factoryTime = factoryTime;
    }

    public int getDistributorID() {
        return distributorID;
    }

    public void setDistributorID(int distributorID) {
        this.distributorID = distributorID;
    }

    public int getTransportID() {
        return transportID;
    }

    public void setTransportID(int transportID) {
        this.transportID = transportID;
    }

    public int getRemainCarNum() {
        return remainCarNum;
    }

    public void setRemainCarNum(int remainCarNum) {
        this.remainCarNum = remainCarNum;
    }

    public float getRemainTonnage() {
        return remainTonnage;
    }

    public void setRemainTonnage(float remainTonnage) {
        this.remainTonnage = remainTonnage;
    }

    public String getStartPlaceName() {
        return startPlaceName;
    }

    public void setStartPlaceName(String startPlaceName) {
        this.startPlaceName = startPlaceName;
    }

    public String getEndPlaceName() {
        return endPlaceName;
    }

    public void setEndPlaceName(String endPlaceName) {
        this.endPlaceName = endPlaceName;
    }

    public int getGrabedNum() {
        return grabedNum;
    }

    public void setGrabedNum(int grabedNum) {
        this.grabedNum = grabedNum;
    }

    public String getServerCurTime() {
        return serverCurTime;
    }

    public void setServerCurTime(String serverCurTime) {
        this.serverCurTime = serverCurTime;
    }

    public float getCarLength() {
        return carLength;
    }

    public void setCarLength(float carLength) {
        this.carLength = carLength;
    }

    public String getDestinationDis() {
        return destinationDis;
    }

    public void setDestinationDis(String destinationDis) {
        this.destinationDis = destinationDis;
    }

    public String getDistributorContact() {
        return distributorContact;
    }

    public void setDistributorContact(String distributorContact) {
        this.distributorContact = distributorContact;
    }

    public String getDistributorPhoneNumber() {
        return distributorPhoneNumber;
    }

    public void setDistributorPhoneNumber(String distributorPhoneNumber) {
        this.distributorPhoneNumber = distributorPhoneNumber;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public int getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(int taxRate) {
        this.taxRate = taxRate;
    }

    public int getPickupType() {
        return pickupType;
    }

    public void setPickupType(int pickupType) {
        this.pickupType = pickupType;
    }

    public int getDebugIndex() {
        return debugIndex;
    }

    public void setDebugIndex(int debugIndex) {
        this.debugIndex = debugIndex;
    }
    
}
