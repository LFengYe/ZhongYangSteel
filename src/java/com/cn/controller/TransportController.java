/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.controller;

import com.cn.entity.Transport;
import com.cn.util.DatabaseOpt;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LFeng
 */
public class TransportController {

    /**
     * 添加运输处信息
     * @param username
     * @param password
     * @param name
     * @param phoneNumber
     * @return 
     */
    public int transportAdd(String username, String password, String name, String phoneNumber) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;

        int result = -1;
        try {
            statement = conn.prepareCall("{call tbTransportAdd(?, ?, ?, ?, ?)}");
            statement.setString("_transportUserName", username);
            statement.setString("_transportPassword", password);
            statement.setString("_transportName", name);
            statement.setString("_transportPhoneNumber", phoneNumber);
            statement.registerOutParameter("_addResult", java.sql.Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("_addResult");
        } catch (Exception e) {
            Logger.getLogger(TransportController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TransportController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 修改运输处用户信息
     * transportID为必填参数,
     * 其余参数填值则修改相应字段为该值; 填null或者是"", 则不修改对应字段
     * @param transportID - 必填参数
     * @param username
     * @param password
     * @param name
     * @param phoneNumber
     * @return -  1 更新出错 | 0 更新成功 | -1更新失败
     */
    public int transportUpdate(int transportID, String username, String password, String name, String phoneNumber) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;

        int result = 1;
        try {
            statement = conn.prepareCall("{call tbTransportUpdate(?, ?, ?, ?, ?, ?)}");
            statement.setInt("_transportID", transportID);
            statement.setString("_transportUserName", username);
            statement.setString("_transportPassword", password);
            statement.setString("_transportName", name);
            statement.setString("_transportPhoneNumber", phoneNumber);
            statement.registerOutParameter("result", java.sql.Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (Exception e) {
            Logger.getLogger(TransportController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TransportController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 
     * @param transportID
     * @return -  1 删除出错 | 0 删除成功 | -1删除失败
     */
    public int transportDelete(int transportID) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;

        int result = 1;
        try {
            statement = conn.prepareCall("{call tbTransportDelete(?, ?)}");
            statement.setInt("_transportID", transportID);
            statement.registerOutParameter("result", java.sql.Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (Exception e) {
            Logger.getLogger(TransportController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TransportController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 
     * @param userName
     * @param password
     * @return -1 出错 | 1 登录成功 | 0 登录失败
     */
    public int transportLogin(String userName, String password) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbTransportLogin(?, ?, ?)}");
            statement.setString("userName", userName);
            statement.setString("password", password);
            statement.registerOutParameter("loginState", java.sql.Types.INTEGER);
            statement.execute();
            result = statement.getInt("loginState");
        } catch (Exception e) {
            Logger.getLogger(TransportController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TransportController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 
     * @param transportID
     * @param transportName
     * @param transportUserName
     * @param nowPage
     * @param pageSize
     * @return 
     */
    public ArrayList<Transport> transportGet(int transportID, String transportName, String transportUserName, int nowPage, int pageSize) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        ArrayList<Transport> result = new ArrayList<>();
        try {
            statement = conn.prepareCall("{call tbTransportGet(?, ?, ?, ?, ?)}");
            statement.setInt("_transportID", transportID);
            statement.setString("_transportUserName", transportUserName);
            statement.setString("_transportName", transportName);
            statement.setInt("nowPage", nowPage);
            statement.setInt("pageSize", pageSize);
            ResultSet set = statement.executeQuery();
            while(set.next()) {
                Transport bean = new Transport();
                bean.setTransportID(set.getInt("TransportID"));
                bean.setTransportUserName(set.getString("TranSportUserName"));
                bean.setTransportPassword(set.getString("TranSportPassword"));
                bean.setTransportName(set.getString("TranSportName"));
                bean.setTransportPhoneNumber(set.getString("TranSportPhoneNumber"));
                
                result.add(bean);
            }
        } catch (Exception e) {
            Logger.getLogger(TransportController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TransportController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 
     * @param transportUserName
     * @return 
     */
    public Transport transportGetWithUserName(String transportUserName) {
        ArrayList<Transport> result = transportGet(-1, null, transportUserName, 1, 10);
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }
}
