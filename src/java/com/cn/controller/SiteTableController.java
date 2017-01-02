/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.controller;

import com.cn.entity.SiteTalble;
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
public class SiteTableController {
    
    /**
     * 
     * @param siteLat
     * @param siteLon
     * @param siteName
     * @param siteType
     * @return 
     */
    public SiteAddResult siteAdd(float siteLat, float siteLon, String siteName, int siteType) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        SiteAddResult result = new SiteAddResult();
        result.setResult(-1);
        try {
            statement = conn.prepareCall("{call tbSiteAdd(?, ?, ?, ?, ?)}");
            statement.setFloat("_siteLatitude", siteLat);
            statement.setFloat("_siteLongitude", siteLon);
            statement.setString("_siteName", siteName);
            statement.setInt("_siteType", siteType);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.execute();
            result.setResult(statement.getInt("result"));
            
            if (result.result == 0) {
                ResultSet set = statement.getResultSet();
                while(set.next()) {
                    SiteTalble site = new SiteTalble();
                    site.setSiteID(set.getInt("SiteID"));
                    site.setSiteName(set.getString("SiteName"));
                    site.setSiteType(set.getInt("SiteType"));
                    result.setSite(site);
                }
            }
            return result;
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
    
    /**
     * 
     * @param siteID
     * @param siteName
     * @param siteType
     * @param nowPage
     * @param pageSize
     * @return 
     */
    public ArrayList<SiteTalble> siteGet(int siteID, String siteName, int siteType, int nowPage, int pageSize) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        ArrayList<SiteTalble> result = new ArrayList<>();
        try {
            statement = conn.prepareCall("{call tbSiteGet(?, ?, ?, ?, ?)}");
            statement.setInt("_siteID", siteID);
            statement.setString("_siteName", siteName);
            statement.setInt("_siteType", siteType);
            statement.setInt("nowPage", nowPage);
            statement.setInt("pageSize", pageSize);
            ResultSet set = statement.executeQuery();
            while(set.next()) {
                SiteTalble table = new SiteTalble();
                table.setSiteID(set.getInt("SiteID"));
                table.setSiteLatitude(set.getFloat("SiteLatitude"));
                table.setSiteLongitude(set.getFloat("SiteLongitude"));
                table.setSiteName(set.getString("SiteName"));
                table.setSiteType(set.getInt("SiteType"));
                result.add(table);
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
    
    /**
     * 
     * @param siteName
     * @param siteType
     * @param nowPage
     * @param pageSize
     * @return 
     */
    public ArrayList<SiteTalble> siteGet(String siteName, int siteType, int nowPage, int pageSize) {
        return siteGet(-1, siteName, siteType, nowPage, pageSize);
    }
    
    /**
     * 
     * @param siteID
     * @return 
     */
    public SiteTalble siteGetWithID(int siteID) {
        ArrayList<SiteTalble> result = siteGet(siteID, null, -1, 1, 10);
        if (null != result && result.size() > 0) {
            return result.get(0);
        }
        return null;
    }
    
    public class SiteAddResult {
        private int result;
        private SiteTalble site;

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public SiteTalble getSite() {
            return site;
        }

        public void setSite(SiteTalble site) {
            this.site = site;
        }
        
    }
}
