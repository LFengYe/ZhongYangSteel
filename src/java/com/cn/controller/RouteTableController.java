/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.controller;

import com.cn.entity.RouteTable;
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
public class RouteTableController {
    
    /**
     * 添加路径
     * @param startLat
     * @param startLon
     * @param startName
     * @param endLat
     * @param endLon
     * @param endName
     * @param length
     * @return 
     */
    public int routeAdd(float startLat, float startLon, String startName, float endLat, float endLon, String endName, float length) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbRouteAdd(?, ?, ?, ?, ?, ?, ?, ?)}");
            statement.setFloat("_startLatitude", startLat);
            statement.setFloat("_startLongitude", startLon);
            statement.setString("_startName", startName);
            statement.setFloat("_endLatitude", endLat);
            statement.setFloat("_endLongitude", endLon);
            statement.setString("_endName", endName);
            statement.setFloat("_routeLength", length);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            Logger.getLogger(RouteTableController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(RouteTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 获取路径列表
     * @param startName
     * @param endName
     * @param nowPage
     * @param pageSize
     * @return 
     */
    public ArrayList<RouteTable> routeGet(String startName, String endName, int nowPage, int pageSize) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        ArrayList<RouteTable> result = new ArrayList<>();
        try {
            statement = conn.prepareCall("{call tbRouteAdd(?, ?, ?. ?)}");
            statement.setString("_startName", startName);
            statement.setString("_endName", endName);
            statement.setInt("nowPage", nowPage);
            statement.setInt("pageSize", pageSize);
            ResultSet set = statement.executeQuery();
            while(set.next()) {
                RouteTable table = new RouteTable();
                table.setRouteID(set.getInt("RouteID"));
                table.setRouteStartLat(set.getFloat("RouteStartLatitude"));
                table.setRouteStartLon(set.getFloat("RouteStartLongitude"));
                table.setRouteStartName(set.getString("RouteStartName"));
                table.setRouteEndLat(set.getFloat("RouteEndLatitude"));
                table.setRouteEndLon(set.getFloat("RouteEndLongitude"));
                table.setRouteEndName(set.getString("RouteEndName"));
                table.setRouteLength(set.getFloat("RouteLength"));
                result.add(table);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RouteTableController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(RouteTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
}
