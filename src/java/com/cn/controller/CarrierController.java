/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.controller;

import com.cn.entity.CarrierInfo;
import com.cn.util.DatabaseOpt;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.slf4j.LoggerFactory;

/**
 *
 * @author LFeng
 */
public class CarrierController {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(CarrierController.class);
    
    /**
     * 
     * @param carrierId
     * @param carrierName
     * @param nowPage
     * @param pageSize
     * @return 
     */
    public ArrayList<CarrierInfo> carrierGet(int carrierId, String carrierName, int nowPage, int pageSize) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        ArrayList<CarrierInfo> result = new ArrayList<>();
        try {
            statement = conn.prepareCall("{call tbCarrierGet(?, ?, ?, ?)}");
            statement.setInt("_carrierId", carrierId);
            statement.setString("_carrierName", carrierName);
            statement.setInt("nowPage", nowPage);
            statement.setInt("pageSize", pageSize);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                CarrierInfo info = new CarrierInfo();
                info.setCarrierId(set.getInt("CarrierId"));
                info.setCarrierName(set.getString("CarrierName"));
                result.add(info);
            }
        } catch (SQLException ex) {
            LOG.error("SQL执行错误", ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                LOG.error("数据库连接关闭错误", ex);
            }
        }
        return result;
    }
}
