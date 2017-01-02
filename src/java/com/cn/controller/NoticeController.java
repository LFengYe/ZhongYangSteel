/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.controller;

import com.cn.entity.Notice;
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
public class NoticeController {
    
    /**
     * 公告添加
     * @param noticeTitle
     * @param noticeContent
     * @return 
     */
    public int noticeAdd(String noticeTitle, String noticeContent) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbNoticeAdd(?, ?, ?)}");
            statement.setString("_noticeTitle", noticeTitle);
            statement.setString("_noticeContent", noticeContent);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 更新公告
     * @param noticeID
     * @param noticeTitle
     * @param noticeContent
     * @return 
     */
    public int noticeUpdate(int noticeID, String noticeTitle, String noticeContent) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbNoticeUpdate(?, ?, ?, ?)}");
            statement.setInt("_noticeID", noticeID);
            statement.setString("_noticeTitle", noticeTitle);
            statement.setString("_noticeContent", noticeContent);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 删除公告
     * @param noticeID
     * @return 
     */
    public int noticeDelete(int noticeID) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbNoticeDelete(?, ?)}");
            statement.setInt("_noticeID", noticeID);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 
     * @param noticeID
     * @param startTime
     * @param endTime
     * @param nowPage
     * @param pageSize
     * @return 
     */
    public ArrayList<Notice> getNoticeList(int noticeID, String startTime, String endTime, int nowPage, int pageSize) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        ArrayList<Notice> result = new ArrayList<>();
        try {
            statement = conn.prepareCall("{call tbNoticeGet(?, ?, ?, ?, ?)}");
            statement.setInt("_noticeID", noticeID);
            statement.setString("_startTime", startTime);
            statement.setString("_endTime", endTime);
            statement.setInt("nowPage", nowPage);
            statement.setInt("pageSize", pageSize);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                Notice notice = new Notice();
                notice.setNoticeID(set.getInt("NoticeID"));
                notice.setNoticeTitle(set.getString("NoticeTitle"));
                notice.setNoticeContent(set.getString("NoticeContent"));
                notice.setNoticeTime(set.getString("NoticeTime"));
                result.add(notice);
            }
        } catch (Exception e) {
            Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public Notice getNoticeDetail(int noticeID) {
        ArrayList<Notice> result = getNoticeList(noticeID, null, null, 1, 1);
        if (null != result && result.size() > 0) {
            return result.get(0);
        }
        return null;
    }
}
