/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.controller;

import com.cn.entity.LoginUser;
import com.cn.util.DatabaseOpt;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LFeng
 */
public class LoginUserController {
    
    /**
     * 
     * @param userType
     * @param userID
     * @param startTime
     * @param endTime
     * @return 
     */
    public ArrayList<LoginUser> getLoginUserList(int userType, int userID, String startTime, String endTime) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        ArrayList<LoginUser> result = new ArrayList<>();
        try {
            statement = conn.prepareCall("{call tbLoginUserGet(?, ?, ?, ?)}");
            statement.setInt("_userType", userType);
            statement.setInt("_userID", userID);
            statement.setString("_startTime", startTime);
            statement.setString("_endTime", endTime);
            ResultSet set = statement.executeQuery();
            while(set.next()) {
                LoginUser user = new LoginUser();
                user.setUserID(set.getInt("UserID"));
                user.setUserType(set.getInt("UserType"));
                user.setLoginTime(set.getString("LoginTime"));
                user.setImei(set.getString("IMEI"));
                user.setUpdateTime(set.getString("UpdateTime"));
                result.add(user);
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(LoginUserController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(LoginUserController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    /**
     * 获取可接受推送消息的登录用户
     * @return 
     */
    public ArrayList<LoginUser> getCanUseUserList() {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        ArrayList<LoginUser> result = new ArrayList<>();
        try {
            statement = conn.prepareCall("{call tbEnterpriseCanUse()}");
            ResultSet set = statement.executeQuery();
            while(set.next()) {
                LoginUser user = new LoginUser();
                user.setUserID(set.getInt("UserID"));
                user.setUserType(set.getInt("UserType"));
                user.setLoginTime(set.getString("LoginTime"));
                user.setImei(set.getString("IMEI"));
                result.add(user);
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(LoginUserController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(LoginUserController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    /**
     * 更新已登录用户的IMEI
     * @param userType
     * @param userId
     * @param imei
     * @return 
     */
    public int updateLoginUserImei(int userType, int userId, String imei) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbLoginUserUpdate(?, ?, ?, ?)}");
            statement.setInt("_userID", userId);
            statement.setInt("_userType", userType);
            statement.setString("_imei", imei);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
}
