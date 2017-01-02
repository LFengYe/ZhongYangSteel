/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.controller;

import com.cn.entity.Version;
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
public class VersionController {
    
    /**
     * 获取最新版本
     * @param updateType
     * @return 
     */
    public Version getNewestVersion(int updateType) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        Version result = null;
        try {
            statement = conn.prepareCall("{call tbVersionInfoGet(?)}");
            statement.setInt("_updateType", updateType);
            ResultSet set = statement.executeQuery();
            while(set.next()) {
                result = new Version();
                result.setVarsionID(set.getInt("VersionID"));
                result.setUpdateTime(set.getString("UpdateTime"));
                result.setVersionNumber(set.getInt("VersionNumber"));
                result.setVersionNumberName(set.getString("VersionNumberName"));
                result.setAppPathUrl(set.getString("AppPathUrl"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SiteTableController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(SiteTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
}
