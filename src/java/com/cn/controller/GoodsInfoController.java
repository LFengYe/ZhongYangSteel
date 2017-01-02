/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.controller;

import com.cn.entity.GoodsInfo;
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
public class GoodsInfoController {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(GoodsInfoController.class);
    
    /**
     * 
     * @param goodsId
     * @param goodsName
     * @param goodsType
     * @param goodsCoefficients
     * @param goodsKind
     * @return 
     */
    public int goodsInfoAddZhongYang(String goodsId, String goodsName, String goodsType, String goodsCoefficients, String goodsKind) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbZhongYang_GoodsInfoAdd(?, ?, ?, ?, ?, ?)}");
            statement.setString("_goodsId", goodsId);
            statement.setString("_goodsName", goodsName);
            statement.setString("_goodsType", goodsType);
            statement.setString("_goodsCoefficients", goodsCoefficients);
            statement.setString("_goodsKind", goodsKind);
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
     * 更新物料信息
     * @param goodsId
     * @param goodsName
     * @param goodsType
     * @param goodsCoefficients
     * @param goodsKind
     * @return 
     */
    public int goodsInfoUpdateZhongYang(String goodsId, String goodsName, String goodsType, String goodsCoefficients, String goodsKind) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbZhongYang_GoodsInfoUpdate(?, ?, ?, ?, ?, ?)}");
            statement.setString("_goodsId", goodsId);
            statement.setString("_goodsName", goodsName);
            statement.setString("_goodsType", goodsType);
            statement.setString("_goodsCoefficients", goodsCoefficients);
            statement.setString("_goodsKind", goodsKind);
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
     * @param goodsId
     * @param goodsName
     * @param nowPage
     * @param pageSize
     * @return 
     */
    public ArrayList<GoodsInfo> goodsInfoGet(String goodsId, String goodsName, int nowPage, int pageSize) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        ArrayList<GoodsInfo> result = new ArrayList<>();
        try {
            statement = conn.prepareCall("{call tbGoodsInfoGet(?, ?, ?, ?)}");
            statement.setString("_goodsId", goodsId);
            statement.setString("_goodsName", goodsName);
            statement.setInt("nowPage", nowPage);
            statement.setInt("pageSize", pageSize);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                GoodsInfo info = new GoodsInfo();
                info.setGoodsID(set.getString("GoodsID"));
                info.setGoodsName(set.getString("GoodsName"));
                info.setGoodsCoefficients(set.getString("GoodsCoefficients"));
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
}
