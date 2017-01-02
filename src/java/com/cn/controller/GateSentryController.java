/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.controller;

import com.cn.entity.GateSentry;
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
public class GateSentryController {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(GateSentryController.class);
    
    /**
     * 
     * @return 
     */
    public ArrayList<GateSentry> getGateSentryList() {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        ArrayList<GateSentry> result = new ArrayList<>();
        try {
            statement = conn.prepareCall("{call tbGateSentryGet()}");
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                GateSentry gateSentry = new GateSentry();
                gateSentry.setGateSentryID(set.getString("GateSentryID"));
                gateSentry.setGateSentryName(set.getString("GateSentryName"));
                result.add(gateSentry);
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
