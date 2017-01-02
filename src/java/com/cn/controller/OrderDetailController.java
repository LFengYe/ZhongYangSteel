/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.controller;

import com.cn.entity.OrderDetail;
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
public class OrderDetailController {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(OrderDetailController.class);
    
    /**
     * 
     * @param taskID
     * @param nowPage
     * @param pageSize
     * @return 
     */
    public ArrayList<OrderDetail> getOrderGoodsInfo(int taskID, int nowPage, int pageSize) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        ArrayList<OrderDetail> result = new ArrayList<>();
        try {
            statement = conn.prepareCall("{call tbOrderDetailGet(?, ?, ?)}");
            statement.setInt("_taskID", taskID);
            statement.setInt("nowPage", nowPage);
            statement.setInt("pageSize", pageSize);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                OrderDetail info = new OrderDetail();
                info.setGoodsID(set.getString("GoodsID"));
                info.setGoodsName(set.getString("GoodsName"));
                info.setGoodsNum(set.getString("GoodsNum"));
                //info.setGoodsCoefficients(set.getString("GoodsCoefficients"));
                info.setGateSentryID(set.getString("GateSentryID"));
                info.setGateSentryName(set.getString("GateSentryName"));
                result.add(info);
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
    /*
    public static void main(String[] args) {
        OrderDetailController controller = new OrderDetailController();
        System.out.println(JSONArray.toJSONString(controller.getOrderGoodsInfo(1, 1, 10)));
    }
    */
}
