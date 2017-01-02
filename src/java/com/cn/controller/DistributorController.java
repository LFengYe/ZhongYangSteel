/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.controller;

import com.cn.entity.Distributor;
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
public class DistributorController {
    
    /**
     * 添加经销商
     * @param username
     * @param password
     * @param name
     * @param phoneNumber
     * @param address
     * @param company
     * @return 
     */
    public int distributorAdd(String username, String password, String name, String phoneNumber, String address, String company) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbDistributorAdd(?, ?, ?, ?, ?, ?, ?, ?)}");
            statement.setString("_distributorUserName", username);
            statement.setString("_distributorPassword", password);
            statement.setString("_distributorName", name);
            statement.setString("_distributorAddress", address);
            statement.setString("_distributorPhoneNumber", phoneNumber);
            statement.setString("_distributorCompany", company);
            statement.registerOutParameter("t_error", Types.INTEGER);
            statement.registerOutParameter("_addStatue", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("_addStatue");
        } catch (SQLException ex) {
            Logger.getLogger(DistributorController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DistributorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 中阳添加经销商接口
     * @param name
     * @param code
     * @return 
     */
    public int distributorAddZhongYang(String name, String code) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbZhongYang_DistributorAdd(?, ?, ?)}");
            statement.setString("_distributorName", name);
            statement.setString("_distributorCode", code);
            statement.registerOutParameter("_addStatue", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("_addStatue");
        } catch (SQLException ex) {
            Logger.getLogger(DistributorController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DistributorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 经销商更新
     * @param distributorID
     * @param password
     * @param name
     * @param phoneNumber
     * @param address
     * @param company
     * @return 
     */
    public int distributorUpdate(int distributorID, String password, String name, String phoneNumber, String address, String company) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = 1;
        try {
            statement = conn.prepareCall("{call tbDistributorUpdate(?, ?, ?, ?, ?, ?, ?)}");
            statement.setInt("_distributorID", distributorID);
            //statement.setString("_distributorUserName", username);
            statement.setString("_distributorPassword", password);
            statement.setString("_distributorName", name);
            statement.setString("_distributorAddress", address);
            statement.setString("_distributorPhoneNumber", phoneNumber);
            statement.setString("_distributorCompany", company);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            Logger.getLogger(DistributorController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DistributorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 经销商删除
     * @param distributorID
     * @return 
     */
    public int distributorDelete(int distributorID) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = 1;
        try {
            statement = conn.prepareCall("{call tbDistributorDelete(?, ?)}");
            statement.setInt("_distributorID", distributorID);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            Logger.getLogger(DistributorController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DistributorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 经销商登录
     * @param username
     * @param password
     * @param imei
     * @param ipAddress
     * @return 
     */
    public int distributorLogin(String username, String password, String imei, String ipAddress) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = 1;
        try {
            statement = conn.prepareCall("{call tbDistributorLogin(?, ?, ?, ?, ?)}");
            statement.setString("username", username);
            statement.setString("password", password);
            statement.setString("_imei", imei);
            statement.setString("ipAddress", ipAddress);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            Logger.getLogger(DistributorController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DistributorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 获取经销商列表
     * @param distributorID
     * @param distributorName - 模糊查询
     * @param distributorUserName
     * @param nowPage
     * @param pageSize
     * @return 
     */
    public ArrayList<Distributor> distributorGet(int distributorID, String distributorName, String distributorUserName, int nowPage, int pageSize) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        ArrayList<Distributor> result = new ArrayList<>();
        try {
            statement = conn.prepareCall("{call tbDistributorGet(?, ?, ?, ?, ?)}");
            statement.setInt("_distributorID", distributorID);
            statement.setString("_distributorUserName", distributorUserName);
            statement.setString("_distributorName", distributorName);
            statement.setInt("nowPage", nowPage);
            statement.setInt("pageSize", pageSize);
            ResultSet set = statement.executeQuery();
            while(set.next()) {
                Distributor bean = new Distributor();
                bean.setDistributorID(set.getInt("DistributorID"));
                bean.setDistributorUserName(set.getString("DistributorUserName"));
                //bean.setDistributorPassword(set.getString("DistributorPassword"));
                bean.setDistributorName(set.getString("DistributorName"));
                bean.setDistributorAddress(set.getString("DistributorAddress"));
                bean.setDistributorPhoneNumber(set.getString("DistributorPhoneNumber"));
                bean.setDistributorCompany(set.getString("DistributorCompany"));
                bean.setLoginImei(set.getString("IMEI"));
                result.add(bean);
            }
        } catch (Exception e) {
            Logger.getLogger(DistributorController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DistributorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public Distributor distributorGetWithUsername(String usernmae) {
        ArrayList<Distributor> result = distributorGet(-1, null, usernmae, 1, 11);
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }
}
