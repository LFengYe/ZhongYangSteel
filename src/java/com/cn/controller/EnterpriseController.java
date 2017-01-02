/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.controller;

import com.cn.entity.Enterprise;
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
public class EnterpriseController {

    /**
     * 添加企业用户
     * @param type - 0 企业 | 1 个体
     * @param username
     * @param password
     * @param name
     * @param address
     * @param contact
     * @param phoneNumber
     * @param license
     * @param carrierId
     * @param carrierName
     * @return 
     */
    public int enterpriseAdd(int type, String username, String password, String name, String address, String contact, String phoneNumber, String license,
            int carrierId, String carrierName) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbEnterpriseAdd(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            statement.setInt("_enterpriseType", type);
            statement.setString("_enterpriseUserName", username);
            statement.setString("_enterprisePassword", password);
            statement.setString("_enterpriseName", name);
            statement.setString("_enterprisePhoneNumber", phoneNumber);
            statement.setString("_enterpriseAddress", address);
            statement.setString("_enterpriseContact", contact);
            statement.setString("_dLicense", license);
            statement.setInt("_enterpriseCarrierId", carrierId);
            statement.setString("_enterpriseCarrierName", carrierName);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.registerOutParameter("t_error", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            Logger.getLogger(EnterpriseController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EnterpriseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 企业用户认证
     * @param enterpriseId
     * @param name
     * @param phoneNumber
     * @param license
     * @param carrierName
     * @return 
     */
    public int enterpriseAuth(int enterpriseId, String name, String phoneNumber, String license, String carrierName) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbEnterpriseAuth(?, ?, ?, ?, ?, ?)}");
            statement.setInt("_enterpriseID", enterpriseId);
            statement.setString("_enterpriseName", name);
            statement.setString("_enterprisePhoneNumber", phoneNumber);
            statement.setString("_enterpriseIdentityCard", license);
            statement.setString("_enterpriseCarrierName", carrierName);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            Logger.getLogger(EnterpriseController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EnterpriseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 更新企业用户
     * @param enterpriseId
     * @param password
     * @param name
     * @param address
     * @param contact
     * @param phoneNumber
     * @return 
     */
    public int enterpriseUpdate(int enterpriseId, String password, String name, String address, String contact, String phoneNumber) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbEnterpriseUpdate(?, ?, ?, ?, ?, ?, ?)}");
            statement.setInt("_enterpriseId", enterpriseId);
//            statement.setString("_enterpriseUserName", username);
            statement.setString("_enterprisePassword", password);
            statement.setString("_enterpriseName", name);
            statement.setString("_enterprisePhoneNumber", phoneNumber);
            statement.setString("_enterpriseAddress", address);
            statement.setString("_enterpriseContact", contact);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            Logger.getLogger(EnterpriseController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EnterpriseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 
     * @param enterpriseId
     * @return 
     */
    public int enterpriseDelete(int enterpriseId) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = 1;
        try {
            statement = conn.prepareCall("{call tbEnterpriseDelate(?, ?)}");
            statement.setInt("_enterpriseId", enterpriseId);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            Logger.getLogger(EnterpriseController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EnterpriseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 企业用户或个体用户(司机)登录
     * @param userName
     * @param password
     * @param imei
     * @return 
     */
    public int enterpriseLogin(String userName, String password, String imei, String ipAddress) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbEnterpriseLogin(?, ?, ?, ?, ?)}");
            statement.setString("username", userName);
            statement.setString("password", password);
            statement.setString("_imei", imei);
            statement.setString("ipAddress", ipAddress);
            statement.registerOutParameter("result", java.sql.Types.INTEGER);
            statement.execute();
            result = statement.getInt("result");
        } catch (Exception e) {
            Logger.getLogger(EnterpriseController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EnterpriseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 获取企业用户列表
     * @param enterpriseID
     * @param enterpriseUserName
     * @param nowPage
     * @param pageSize
     * @return 
     */
    public ArrayList<Enterprise> enterpriseGet(int enterpriseID, String enterpriseUserName, int nowPage, int pageSize) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        ArrayList<Enterprise> result = new ArrayList<>();
        try {
            statement = conn.prepareCall("{call tbEnterpriseGet(?, ?, ?, ?)}");
            statement.setInt("_enterpriseID", enterpriseID);
            statement.setString("_enterpriseUserName", enterpriseUserName);
            statement.setInt("nowPage", nowPage);
            statement.setInt("pageSize", pageSize);
            
            ResultSet set = statement.executeQuery();
            while(set.next()) {
                Enterprise bean = new Enterprise();
                bean.setEnterpriseID(set.getInt("EnterpriseID"));
                bean.setEnterpriseType(set.getInt("EnterpriseType"));
                bean.setEnterpriseUserName(set.getString("EnterpriseUserName"));
                bean.setEnterprisePassword(set.getString("EnterprisePassword"));
                bean.setEnterpriseName((null != set.getString("EnterpriseName")) ? (set.getString("EnterpriseName")) : (set.getString("EnterpriseUserName")));
                bean.setEnterpriseAddress(set.getString("EnterpriseAddress"));
                bean.setEnterpriseContact(set.getString("EnterpriseContact"));
                bean.setEnterprisePhoneNumber(set.getString("EnterprisePhoneNumber"));
                bean.setEnterpriseIdentifyCard(set.getString("EnterpriseIdentityCard"));
                bean.setCarrierId(set.getInt("EnterpriseCarrierId"));
                bean.setCarrierName(set.getString("EnterpriseCarrierName"));
                bean.setAuthStatus(set.getInt("AuthenStatus"));
                bean.setLoginImei(set.getString("IMEI"));
                result.add(bean);
            }
        } catch (Exception e) {
            Logger.getLogger(EnterpriseController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EnterpriseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 根据用户名获取企业信息
     * @param enterpriseUserName
     * @return 
     */
    public Enterprise enterpriseGetWithUserName(String enterpriseUserName) {
        ArrayList<Enterprise> result = enterpriseGet(-1, enterpriseUserName, 1, 10);
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }
}
