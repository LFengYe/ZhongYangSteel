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
public class OrderGrab {
    
    private String EnterpriseID;
    private String EnterpriseName;
    private String carrierName;
//    private int EnterpriseType;
//    private String EnterpriseAddress;
//    private String EnterpriseContact;
    private String EnterprisePhoneNumber;
    private int DriverID;
    private String DriverName;
    private String DLicense;
//    private String DPhoneNumber;
    private int CarID;
    private String SystemNo;
    private String CarNumber;
//    private String CarType;
//    private String CarWeight;
    private int TaskID;
    private String TaskSerial;
    private int TaskStatus;
    private int TaskType;
    private String GoodsType;
    private int PriceModel;
    private int StartPlace;
    private String StartPlaceName;
    private float StartPlaceLatitude;
    private float StartPlaceLongitude;
    private int EndPlace;
    private String EndPlaceName;
    private float EndPlaceLongitude;
    private float EndPlaceLatitude;
    private float CurLongitude;
    private float CurLatitude;
    private float carMinLength;
    private String distributorName;
    private String distributorContact;
    private String distributorPhoneNumber;
    private String destinationDis;
    private int taxRate;
    private int pickupType;
    
    private String SendTime;
    private float TLenth;
    private int FactoryTime;
    private float OPrice;
    private float TaskNum;
    private int GrabTimes;
    private int GrabStatus;
    private int RevokeStatus;
    private String GrabTime;
    private String FailReason;
    private String AuditOpinion;
    private int inFactoryStatus;
    private float mileageScale;
    private String serverCurTime;

    public float getEndPlaceLongitude() {
        return EndPlaceLongitude;
    }

    public void setEndPlaceLongitude(float EndPlaceLongitude) {
        this.EndPlaceLongitude = EndPlaceLongitude;
    }

    public float getEndPlaceLatitude() {
        return EndPlaceLatitude;
    }

    public void setEndPlaceLatitude(float EndPlaceLatitude) {
        this.EndPlaceLatitude = EndPlaceLatitude;
    }

    public float getCurLongitude() {
        return CurLongitude;
    }

    public void setCurLongitude(float CurLongitude) {
        this.CurLongitude = CurLongitude;
    }

    public float getCurLatitude() {
        return CurLatitude;
    }

    public void setCurLatitude(float CurLatitude) {
        this.CurLatitude = CurLatitude;
    }

    public String getEnterpriseID() {
        return EnterpriseID;
    }

    public void setEnterpriseID(String EnterpriseID) {
        this.EnterpriseID = EnterpriseID;
    }

    public int getDriverID() {
        return DriverID;
    }

    public void setDriverID(int DriverID) {
        this.DriverID = DriverID;
    }

    public int getCarID() {
        return CarID;
    }

    public void setCarID(int CarID) {
        this.CarID = CarID;
    }

    public String getSystemNo() {
        return SystemNo;
    }

    public void setSystemNo(String SystemNo) {
        this.SystemNo = SystemNo;
    }

    public String getCarNumber() {
        return CarNumber;
    }

    public void setCarNumber(String CarNumber) {
        this.CarNumber = CarNumber;
    }

    public int getTaskID() {
        return TaskID;
    }

    public void setTaskID(int TaskID) {
        this.TaskID = TaskID;
    }

    public String getTaskSerial() {
        return TaskSerial;
    }

    public void setTaskSerial(String TaskSerial) {
        this.TaskSerial = TaskSerial;
    }

    public int getTaskStatus() {
        return TaskStatus;
    }

    public void setTaskStatus(int TaskStatus) {
        this.TaskStatus = TaskStatus;
    }

    public int getStartPlace() {
        return StartPlace;
    }

    public void setStartPlace(int StartPlace) {
        this.StartPlace = StartPlace;
    }

    public int getEndPlace() {
        return EndPlace;
    }

    public void setEndPlace(int EndPlace) {
        this.EndPlace = EndPlace;
    }

    public float getTLenth() {
        return TLenth;
    }

    public void setTLenth(float TLenth) {
        this.TLenth = TLenth;
    }

    public int getFactoryTime() {
        return FactoryTime;
    }

    public void setFactoryTime(int FactoryTime) {
        this.FactoryTime = FactoryTime;
    }

    public float getOPrice() {
        return OPrice;
    }

    public void setOPrice(float OPrice) {
        this.OPrice = OPrice;
    }

    public float getTaskNum() {
        return TaskNum;
    }

    public void setTaskNum(float TaskNum) {
        this.TaskNum = TaskNum;
    }

    public int getGrabTimes() {
        return GrabTimes;
    }

    public void setGrabTimes(int GrabTimes) {
        this.GrabTimes = GrabTimes;
    }

    public int getGrabStatus() {
        return GrabStatus;
    }

    public void setGrabStatus(int GrabStatus) {
        this.GrabStatus = GrabStatus;
    }

    public String getGrabTime() {
        return GrabTime;
    }

    public void setGrabTime(String GrabTime) {
        this.GrabTime = GrabTime;
    }

    public String getFailReason() {
        return FailReason;
    }

    public void setFailReason(String FailReason) {
        this.FailReason = FailReason;
    }

    public String getGoodsType() {
        return GoodsType;
    }

    public void setGoodsType(String GoodsType) {
        this.GoodsType = GoodsType;
    }

    public String getEnterpriseName() {
        return EnterpriseName;
    }

    public void setEnterpriseName(String EnterpriseName) {
        this.EnterpriseName = EnterpriseName;
    }

    public int getInFactoryStatus() {
        return inFactoryStatus;
    }

    public void setInFactoryStatus(int inFactoryStatus) {
        this.inFactoryStatus = inFactoryStatus;
    }

    public float getMileageScale() {
        return mileageScale;
    }

    public void setMileageScale(float mileageScale) {
        this.mileageScale = mileageScale;
    }

    public String getStartPlaceName() {
        return StartPlaceName;
    }

    public void setStartPlaceName(String StartPlaceName) {
        this.StartPlaceName = StartPlaceName;
    }

    public String getEndPlaceName() {
        return EndPlaceName;
    }

    public void setEndPlaceName(String EndPlaceName) {
        this.EndPlaceName = EndPlaceName;
    }

    public int getRevokeStatus() {
        return RevokeStatus;
    }

    public void setRevokeStatus(int RevokeStatus) {
        this.RevokeStatus = RevokeStatus;
    }

    public int getPriceModel() {
        return PriceModel;
    }

    public void setPriceModel(int PriceModel) {
        this.PriceModel = PriceModel;
    }

    public float getStartPlaceLatitude() {
        return StartPlaceLatitude;
    }

    public void setStartPlaceLatitude(float StartPlaceLatitude) {
        this.StartPlaceLatitude = StartPlaceLatitude;
    }

    public float getStartPlaceLongitude() {
        return StartPlaceLongitude;
    }

    public void setStartPlaceLongitude(float StartPlaceLongitude) {
        this.StartPlaceLongitude = StartPlaceLongitude;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String DriverName) {
        this.DriverName = DriverName;
    }

    public int getTaskType() {
        return TaskType;
    }

    public void setTaskType(int TaskType) {
        this.TaskType = TaskType;
    }

    public String getAuditOpinion() {
        return AuditOpinion;
    }

    public void setAuditOpinion(String AuditOpinion) {
        this.AuditOpinion = AuditOpinion;
    }

    public String getSendTime() {
        return SendTime;
    }

    public void setSendTime(String SendTime) {
        this.SendTime = SendTime;
    }

    public String getEnterprisePhoneNumber() {
        return EnterprisePhoneNumber;
    }

    public void setEnterprisePhoneNumber(String EnterprisePhoneNumber) {
        this.EnterprisePhoneNumber = EnterprisePhoneNumber;
    }

    public float getCarMinLength() {
        return carMinLength;
    }

    public void setCarMinLength(float carMinLength) {
        this.carMinLength = carMinLength;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
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

    public String getDestinationDis() {
        return destinationDis;
    }

    public void setDestinationDis(String destinationDis) {
        this.destinationDis = destinationDis;
    }

    public String getServerCurTime() {
        return serverCurTime;
    }

    public void setServerCurTime(String serverCurTime) {
        this.serverCurTime = serverCurTime;
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

    public String getDLicense() {
        return DLicense;
    }

    public void setDLicense(String DLicense) {
        this.DLicense = DLicense;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }
}
