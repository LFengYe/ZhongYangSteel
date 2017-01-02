/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.controller;

import com.cn.entity.DestinationDis;
import com.cn.util.DatabaseOpt;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import org.slf4j.LoggerFactory;

/**
 *
 * @author LFeng
 */
public class DestinationDisController {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(DestinationDisController.class);
    
    /**
     * 
     * @param disId
     * @param disName
     * @return 
     */
    public int destinationDisAddZhongYang(String disId, String disName) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbZhongYang_DestinationDisAdd(?, ?, ?)}");
            statement.setString("_destinationUserSerial", disId);
            statement.setString("_destinatonDistribution", disName);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
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
    
    /**
     * 
     * @param disID
     * @param disSerial
     * @param disName
     * @param nowPage
     * @param pageSize
     * @return 
     */
    public ArrayList<DestinationDis> destinationDisGet(int disID, String disSerial, String disName, int nowPage, int pageSize) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        ArrayList<DestinationDis> result = new ArrayList<>();
        try {
            statement = conn.prepareCall("{call tbDestinationDisGet(?, ?, ?, ?, ?)}");
            statement.setInt("_disID", disID);
            statement.setString("_disSerial", disSerial);
            statement.setString("_disName", disName);
            statement.setInt("nowPage", nowPage);
            statement.setInt("pageSize", pageSize);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                DestinationDis dis = new DestinationDis();
                dis.setDestinationDis(set.getString("DestinationDistribution"));
                result.add(dis);
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
