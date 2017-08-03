/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LFeng
 */
public class DatabaseOpt {
    
//    protected static final Logger LOG = LoggerFactory.getLogger(DatabaseOpt.class);
    
    /**
     * 连接数据库
     *
     * @return
     */
    public Connection getConnect() {
        //HashMap<String, String> userConfig = DOMParser(new File("java/src/com/cn/base/property.xml"));
        try {
            Properties prop = new Properties();
            prop.load(DatabaseOpt.class.getClassLoader().getResourceAsStream("./config.properties"));
            
            Class.forName(prop.getProperty("driverName"));
            Connection connect = DriverManager.getConnection(prop.getProperty("url"));
            return connect;
        } catch (IOException ex) {
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseOpt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseOpt.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }
        return null;
    }
    
    /*
    public Connection getConnect() {
        Context ctx;
        try {
            ctx = new InitialContext();
            Context envctx = (Context) ctx.lookup("java:comp/env");
            DataSource ds = (DataSource) envctx.lookup("jdbc/zcdb");
            Connection conn = ds.getConnection();
            return conn;
        } catch (NamingException e) {
            LOG.error("NamingException", e);
        } catch (SQLException e) {
            LOG.error("创建连接错误", e);
        } finally {
            
        }
        return null;
    }
    */
    
    /*
    public Connection getConnectWithDataSource() {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource db = (DataSource) envContext.lookup("testDBSource");//testDBSource为<Resource>元素中name属性的值
            Connection conn = db.getConnection();
            System.out.println("获取连接成功!" + conn.toString());
            return conn;
        } catch (NamingException ex) {
            Logger.getLogger(DatabaseOpt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseOpt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    */
    /**
     * 获取Redis数据库连接
     */
    /*
    public Jedis getRedisConnect() {
        Jedis jedis = new Jedis(Constants.REDIS_HOST, Constants.REDIS_PORT);
        return jedis;
    }
    */
}
