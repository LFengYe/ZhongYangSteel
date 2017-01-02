/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.controller;

import com.cn.entity.Driver;
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
public class DriverController {

    /**
     * 添加司机信息
     * @param enterpriseID
     * @param name
     * @param phoneNumber
     * @param license
     * @return -  0 添加成功 | 大于0 已存在 | -1 添加失败
     */
    public int driverAdd(int enterpriseID, String name, String phoneNumber, String license) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbDriverAdd(?, ?, ?, ?, ?)}");
            statement.setInt("userID", enterpriseID);
            statement.setString("name", name);
            statement.setString("phoneNumber", phoneNumber);
            statement.setString("license", license);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            Logger.getLogger(DriverController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DriverController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public int driverLogin(String userName, String password, String imei, String ipAddress) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbDriverLogin(?, ?, ?, ?, ?)}");
            statement.setString("username", userName);
            statement.setString("password", password);
            statement.setString("_imei", imei);
            statement.setString("ipAddress", ipAddress);
            statement.registerOutParameter("result", java.sql.Types.INTEGER);
            statement.execute();
            result = statement.getInt("result");
        } catch (Exception e) {
            Logger.getLogger(DriverController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DriverController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 认证司机信息
     * @param driverID
     * @param name
     * @param phoneNumber
     * @param license
     * @return 
     */
    public int driverAuth(int driverID, String name, String phoneNumber, String license) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = 1;
        try {
            statement = conn.prepareCall("{call tbDriverAuth(?, ?, ?, ?, ?)}");
            statement.setInt("_driverID", driverID);
            statement.setString("_driverName", name);
            statement.setString("_dPhoneNumber", phoneNumber);
            statement.setString("_dLicense", license);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            Logger.getLogger(DriverController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DriverController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 修改司机信息
     * @param driverID
     * @param name
     * @param password
     * @param phoneNumber
     * @param license
     * @return --  1 更新出错 | 0 更新成功 | -1 更新失败
     */
    public int driverUpdate(int driverID, String name, String password, String phoneNumber, String license) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = 1;
        try {
            statement = conn.prepareCall("{call tbDriverUpdate(?, ?, ?, ?, ?, ?)}");
            statement.setInt("_driverID", driverID);
            statement.setString("_driverName", name);
            statement.setString("_dPassword", password);
            statement.setString("_dPhoneNumber", phoneNumber);
            statement.setString("_dLicense", license);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            Logger.getLogger(DriverController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DriverController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 申请删除车辆
     * @param driverID
     * @return 
     */
    public int applyDriverDelete(int driverID) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = 1;
        try {
            statement = conn.prepareCall("{call tbDriverApplyDelete(?, ?)}");
            statement.setInt("_driverID", driverID);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            Logger.getLogger(DriverController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DriverController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 删除司机信息
     * @param driverID
     * @return --  1 删除出错 | 0 删除成功 | -1 删除失败
     */
    public int driverDelete(int driverID) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = 1;
        try {
            statement = conn.prepareCall("{call tbDriverDelete(?, ?)}");
            statement.setInt("_driverID", driverID);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            Logger.getLogger(DriverController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DriverController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 获取司机信息列表
     * @param driverID - 司机ID
     * @param enterpriseID - 司机所属企业ID
     * @param dUserName - 司机用户名
     * @param applyStatus
     * @param nowPage
     * @param pageSize
     * @return 
     */
    public ArrayList<Driver> driverGet(int driverID, int enterpriseID, String dUserName, int applyStatus, int nowPage, int pageSize) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        ArrayList<Driver> result = new ArrayList<>();
        try {
            statement = conn.prepareCall("{call tbDriverGet(?, ?, ?, ?, ?, ?)}");
            statement.setInt("_driverID", driverID);
            statement.setInt("_enterpriseID", enterpriseID);
            statement.setString("_dUserName", dUserName);
            statement.setInt("_ApplyStatus", applyStatus);
            statement.setInt("nowPage", nowPage);
            statement.setInt("pageSize", pageSize);
            
            ResultSet set = statement.executeQuery();
            while(set.next()) {
                Driver bean = new Driver();
                bean.setDriverID(set.getInt("DriverID"));
                bean.setDriverName(set.getString("DriverName"));
                bean.setdCarID(set.getString("CarID"));
                bean.setdPhoneNumber(set.getString("DPhoneNumber"));
                bean.setdLicense(set.getString("DLicense"));
                bean.setEnterpriseID(set.getString("EnterpriseID"));
                bean.setEnterpriseName(set.getString("EnterpriseName"));
                bean.setdUserName(set.getString("DUserName"));
                bean.setAuthStatus(set.getInt("AuthenStatus"));
                bean.setApplyStatus(set.getInt("ApplyStatus"));
                bean.setLoginImei(set.getString("IMEI"));
                result.add(bean);
            }
        } catch (Exception e) {
            Logger.getLogger(DriverController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DriverController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public ArrayList<Driver> driverGet(int driverID, int enterpriseID, String dUserName, int nowPage, int pageSize) {
        return driverGet(driverID, enterpriseID, dUserName, -1, nowPage, pageSize);
    }
    
    /**
     * 获取申请删除的司机
     * @param applyStatus
     * @param nowPage
     * @param pageSize
     * @return 
     */
    public ArrayList<Driver> driverGetApplyDelete(int applyStatus, int nowPage, int pageSize) {
        return driverGet(-1, -1, null, applyStatus, nowPage, pageSize);
    }
    
    /**
     * 根据司机ID获取详细
     * @param driverID
     * @return 
     */
    public Driver driverDetailWithID(int driverID) {
        ArrayList<Driver> result = driverGet(driverID, -1, null, -1, 1, 10);
        if (result.size() > 0)
            return result.get(0);
        return null;
    }
    
    /**
     * 根据用户名获取司机信息
     * @param dUserName
     * @return 
     */
    public Driver driverGetWithUserName(String dUserName) {
        ArrayList<Driver> result = driverGet(-1, -1, dUserName, -1, 1, 10);
        if (result.size() > 0)
            return result.get(0);
        return null;
    }
    
//    public static void main(String[] args) {
//        DriverController controller = new DriverController();
//        System.out.println(JSON.toJSONString(controller.driverGetWithUserName("test1")));
//    }
}
