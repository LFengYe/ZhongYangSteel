/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.controller;

import com.cn.entity.OrderCarStatus;
import com.cn.util.DatabaseOpt;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LFeng
 */
public class OrderCarStatusController {

    /**
     * 获取指定订单指定车辆的状态
     *
     * @param carID
     * @param taskID
     * @return
     */
    public ArrayList<OrderCarStatus> getOrderCarStatusList(int carID, int taskID) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        ArrayList<OrderCarStatus> result = new ArrayList<>();
        try {
            statement = conn.prepareCall("{call tbOrderCarStatusGet(?, ?)}");
            statement.setInt("_carID", carID);
            statement.setInt("_taskID", taskID);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                OrderCarStatus carStatus = new OrderCarStatus();
                carStatus.setCarID(set.getInt("CarID"));
                carStatus.setStateType(set.getInt("StateType"));
                carStatus.setStateName(set.getString("StateName"));
                carStatus.setStateTime(set.getString("StateTime"));
                carStatus.setTaskID(set.getInt("TaskID"));
                result.add(carStatus);
            }
        } catch (Exception e) {
            Logger.getLogger(OrderCarStatusController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderCarStatusController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * 获取轨迹回放的起止时间
     *
     * @param carID
     * @param taskID
     * @return
     */
    public HashMap<String, String> getHistoryTime(int carID, int taskID) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        HashMap<String, String> result = new HashMap<>();
        try {
            statement = conn.prepareCall("{call tbOrderCarStatusGetGetHistoryTime(?, ?, ?)}");
            statement.setInt("_carID", carID);
            statement.setInt("_taskID", taskID);
            statement.registerOutParameter("result", Types.INTEGER);
            ResultSet set = statement.executeQuery();
            
            int status = statement.getInt("result");
            if (status == 0) {
                while (set.next()) {
                    result.put("InFactoryTime", set.getString("InFactoryTime"));
                    result.put("SignInTime", set.getString("SignInTime"));
                    result.put("SystemNo", set.getString("SystemNo"));
                }
            }
            
            result.put("result", String.valueOf(statement.getInt("result")));
        } catch (Exception e) {
            Logger.getLogger(OrderCarStatusController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderCarStatusController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
}
