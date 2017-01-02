/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.controller;

import com.cn.util.DatabaseOpt;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import org.slf4j.LoggerFactory;

/**
 *
 * @author LFeng
 */
public class WeightGoodsController {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(WeightGoodsController.class);
    
    /**
     * 添加称重信息
     * @param orderSerial
     * @param serialNumber
     * @param carNumber
     * @param weightHouseId
     * @param weightTime
     * @param goodsId
     * @param goodsNum
     * @param goodsWeight
     * @return 
     */
    public int addWeightGoods(String orderSerial, String serialNumber, String carNumber, String weightHouseId, String weightTime,
            String goodsId, int goodsNum, float goodsWeight) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbZhongYang_WeightGoodsAdd(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            statement.setString("_taskSerial", orderSerial);
            statement.setString("_serialNumber", serialNumber);
            statement.setString("_carNumber", carNumber);
            statement.setString("_weightHouseId", weightHouseId);
            statement.setString("_weightTime", weightTime);
            statement.setString("_goodsId", goodsId);
            statement.setInt("_goodsNum", goodsNum);
            statement.setFloat("_goodsWeight", goodsWeight);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            LOG.error("数据库执行出错", ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                LOG.error("数据库关闭出错", ex);
            }
        }
        return result;
    }
}
