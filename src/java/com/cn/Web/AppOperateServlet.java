/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.Web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cn.controller.CarTableController;
import com.cn.controller.CarrierController;
import com.cn.controller.DistributorController;
import com.cn.controller.DriverController;
import com.cn.controller.EnterpriseController;
import com.cn.controller.LoginUserController;
import com.cn.controller.NoticeController;
import com.cn.controller.OrderCarStatusController;
import com.cn.controller.OrderGrabController;
import com.cn.controller.OrderTableController;
import com.cn.controller.SiteTableController;
import com.cn.controller.VersionController;
import com.cn.entity.AccessFrequency;
import com.cn.entity.CarLocation;
import com.cn.entity.CarTable;
import com.cn.entity.CarrierInfo;
import com.cn.entity.Distributor;
import com.cn.entity.Driver;
import com.cn.entity.Enterprise;
import com.cn.entity.LoginUser;
import com.cn.entity.Notice;
import com.cn.entity.OrderCarStatus;
import com.cn.entity.OrderGrab;
import com.cn.entity.OrderGrabDis;
import com.cn.entity.OrderTable;
import com.cn.entity.OrderTableTwoTopGrab;
import com.cn.entity.SiteTalble;
import com.cn.entity.Version;
import com.cn.util.CarNumberVerifie;
import com.cn.util.Constants;
import com.cn.util.EncryptUtil;
import com.cn.util.Units;
import com.cn.webService.client.GpsWebService;
import com.mysql.jdbc.StringUtils;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.LoggerFactory;

/**
 *
 * @author LFeng
 */
public class AppOperateServlet extends HttpServlet {

    protected static final org.slf4j.Logger LOG = LoggerFactory.getLogger(AppOperateServlet.class);
    private static final long offlineTime = 30 * 60 * 1000;//30分钟
    private static CopyOnWriteArrayList<AccessFrequency> frequencys;
    private static CopyOnWriteArrayList<AccessFrequency> grabFrequencys;
    private int minLimitTime;//最小时间间隔,单位毫秒
    private static final SerializerFeature[] features = {SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero,
        SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullListAsEmpty};

//    private HashMap<String, LoginUser> loginUserList;
    @Override
    public void init() throws ServletException {
        try {
            super.init();
            Properties prop = new Properties();
            prop.load(AppOperateServlet.class.getClassLoader().getResourceAsStream("./config.properties"));
            minLimitTime = Integer.valueOf(prop.getProperty("minLimitTime", "500"));
            frequencys = new CopyOnWriteArrayList<>();
            grabFrequencys = new CopyOnWriteArrayList<>();
        } catch (IOException ex) {
            LOG.error("初始化错误!", ex);
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @param params
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response, String params)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        String subUri = uri.substring(uri.lastIndexOf("/") + 1,
                uri.lastIndexOf("."));
        String json = null;
        boolean DESFlag = false;
        HttpSession session = request.getSession(false);
        long timestamp = Long.valueOf(request.getHeader("timestamp"));
        System.out.println("timestamp:" + timestamp);
        
        try {
            //System.out.println(subUri + ", params is:" + params);

            //控制同一个IP对同一个接口的访问频率
            String ipAddress = Units.getIpAddress(request);
            AccessFrequency exitsFrequency = isHasHostIP(ipAddress, frequencys);
            if (null != exitsFrequency) {
                long nowTime = new Date().getTime();
                if (nowTime - exitsFrequency.getAccessTime() < minLimitTime
                        && exitsFrequency.getInterfaceName().compareTo(subUri) == 0) {
                    //如果同一个已存在的IP上次访问同一个接口的时间小于最小时间限制(现是500ms)
                    LOG.info("丢掉" + ipAddress + "对接口" + subUri + "的访问");
                    return;
                } else {
                    exitsFrequency.setAccessTime(nowTime);
                    exitsFrequency.setInterfaceName(subUri);
                }
            } else {
                AccessFrequency frequency = new AccessFrequency();
                frequency.setIpAddress(ipAddress);
                frequency.setAccessTime(new Date().getTime());
                frequency.setOverLimitcount(0);
                frequency.setIsEnable(true);
                frequency.setInterfaceName(subUri);
                frequencys.add(frequency);
            }

            //将字符串转换为json
            JSONObject paramsJson = null;
            try {
                paramsJson = JSONObject.parseObject(EncryptUtil.decryptDES(params));
                System.out.println("接口:" + subUri + "解密后的数据:" + paramsJson);
                DESFlag = true;
            } catch (Exception e) {
                LOG.info("接口:" + subUri + "解密异常!");
                paramsJson = null;
//                paramsJson = JSONObject.parseObject(params);
                DESFlag = false;
            }

            if (paramsJson == null) {
                json = Units.objectToJson(-1, "输入参数错误!", null);
                PrintWriter out = response.getWriter();
                try {
                    response.setContentType("text/html;charset=UTF-8");
                    response.setHeader("Cache-Control", "no-store");
                    response.setHeader("Pragma", "no-cache");
                    response.setDateHeader("Expires", 0);
                    if (DESFlag) {
                        out.print(EncryptUtil.encryptDES(json));
                    } else {
                        out.print(json);
                    }
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
                return;
            }

            String imei = paramsJson.getString("IMEI");
            switch (subUri) {
                case "test": {
                    Date date = new Date();
                    json = JSONObject.toJSONString(date);
                    break;
                }

                /**
                 * *******************************订单相关************************************
                 */
                // <editor-fold defaultstate="collapsed" desc="getOrderList">
                case "getOrderList": {
                    int listType = Integer.valueOf((null != paramsJson.getString("listType")) ? paramsJson.getString("listType") : "-1");
                    int startPlace = Integer.valueOf((null != paramsJson.getString("startPlace")) ? paramsJson.getString("startPlace") : "-1");
                    int endPlace = Integer.valueOf((null != paramsJson.getString("endPlace")) ? paramsJson.getString("endPlace") : "-1");
                    int enterpriseID = Integer.valueOf((null != paramsJson.getString("enterpriseID")) ? paramsJson.getString("enterpriseID") : "-1");
                    String startTime = paramsJson.getString("startTime");
                    String endTime = paramsJson.getString("endTime");
                    String distributorName = paramsJson.getString("distributorName");
                    int nowPage = Integer.valueOf((null != paramsJson.getString("nowPage")) ? paramsJson.getString("nowPage") : "0");
                    int pageSize = Integer.valueOf((null != paramsJson.getString("pageSize")) ? paramsJson.getString("pageSize") : "0");
                    OrderTableController controller = new OrderTableController();
                    ArrayList<OrderTable> result = controller.getTaskList(-1, listType, startPlace, endPlace, distributorName, enterpriseID, startTime, endTime, nowPage, pageSize);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "订单记录为空", null);
                    }
                    break;
                }
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="orderDetail">
                case "orderDetail": {
                    String taskSerial = paramsJson.getString("taskSerial");
                    OrderTableController controller = new OrderTableController();
                    OrderTable result = controller.getTaskDetailWithSerial(taskSerial, -1);
//                    System.out.println("orderDetail");
                    if (null != result) {
                        json = Units.objectToJson(0, "", result);
                    } else {
                        json = Units.objectToJson(1, "该订单编号不存在!", null);
                    }
//                    System.out.println(json);
                    break;
                }
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="grabOrder(抢单)">
                case "grabOrder": {
                    int userID = Integer.valueOf((null != paramsJson.getString("userID")) ? paramsJson.getString("userID") : "0");
                    int driverID = Integer.valueOf((null != paramsJson.getString("driverID")) ? paramsJson.getString("driverID") : "0");
                    int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "0");
                    float oPrice = Float.valueOf((null != paramsJson.getString("oPrice")) ? paramsJson.getString("oPrice") : "0");
                    float taskNum = Float.valueOf((null != paramsJson.getString("taskNum")) ? paramsJson.getString("taskNum") : "0");
                    int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "0");
                    int versionNum = Integer.valueOf((null != paramsJson.getString("versionNum")) ? paramsJson.getString("versionNum") : "-1");
                    long grabTime = Long.valueOf((null != paramsJson.getString("grabTime")) ? paramsJson.getString("grabTime") : "0");
                    AccessFrequency grabFrequency = isHasHostIP(ipAddress, grabFrequencys);
                    if (grabFrequency != null) {
                        if (grabTime <= grabFrequency.getAccessTime()) {
                            LOG.error("grabTime:" + grabTime + ",previousGrabTime:" + grabFrequency.getAccessTime());
                            json = Units.objectToJson(Constants.GRAB_ORDER_STATUS_EXCEPTION, "输入参数错误!", null);
                            break;
                        } else {
                            grabFrequency.setAccessTime(grabTime);
                        }
                    } else {
                        AccessFrequency frequency = new AccessFrequency();
                        frequency.setAccessTime(grabTime);
                        frequency.setIpAddress(ipAddress);
                        frequency.setUserID(userID);
                        grabFrequencys.add(frequency);
                    }
                    
                    if (versionNum >= 10) {
                        CarTableController carTableController = new CarTableController();
                        CarLocation location = carTableController.getCarLocationWithID(carID);
                        float latitude = location.getLatitude();
                        float longitude = location.getLongitude();
                        OrderTableController tableController = new OrderTableController();
                        OrderTable orderTable = tableController.getTaskDetailWithSerial("", taskID);
                        SiteTableController siteTableController = new SiteTableController();
                        SiteTalble startSite = siteTableController.siteGetWithID(orderTable.getStartPlace());

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                        try {
                            Date sendTime = df.parse(orderTable.getSendTime());
                            if (!Units.isToday(sendTime)) {
                                json = Units.objectToJson(Constants.GRAB_ORDER_STATUS_EXPIRED, "订单已失效", null);
                            } else if (latitude < 0.1 || longitude < 0.1) {
                                json = Units.objectToJson(Constants.GRAB_ORDER_STATUS_EXCEPTION, "定位异常", null);
                            } else if (orderTable.getCarRenge() * 1000 < Units.GetDistance(longitude, latitude, startSite.getSiteLongitude(), startSite.getSiteLatitude())) {
                                json = Units.objectToJson(Constants.GRAB_ORDER_STATUS_EXCEPTION, "不在抢单区域内", null);
                            } else {
                                synchronized (this) {
                                    OrderGrabController controller = new OrderGrabController();
                                    int result = controller.addOrderGrab(userID, driverID, carID, oPrice, taskNum, taskID);
                                    switch (result) {
                                        case Constants.GRAB_ORDER_STATUS_SUCCESS: {
                                            json = Units.objectToJson(Constants.GRAB_ORDER_STATUS_SUCCESS, "抢单成功, 请尽快进厂", null);
                                            break;
                                        }
                                        case Constants.GRAB_ORDER_STATUS_EXCEPTION: {
                                            json = Units.objectToJson(Constants.GRAB_ORDER_STATUS_EXCEPTION, "抢单异常", null);
                                            break;
                                        }
                                        case Constants.GRAB_ORDER_STATUS_GRABED: {
                                            json = Units.objectToJson(Constants.GRAB_ORDER_STATUS_GRABED, "司机或车辆有正在进行的订单,不能多次抢单", null);
                                            break;
                                        }
                                        case Constants.GRAB_ORDER_STATUS_GRABED_OTHER: {
                                            json = Units.objectToJson(Constants.GRAB_ORDER_STATUS_GRABED_OTHER, "订单车辆数已满", null);
                                            break;
                                        }
                                        case Constants.GRAB_ORDER_STATUS_PASS_TIMES: {
                                            json = Units.objectToJson(Constants.GRAB_ORDER_STATUS_PASS_TIMES, "超过出价次数", null);
                                            break;
                                        }
                                        case Constants.GRAB_ORDER_STATUS_TONNAGE_LACK://定价时, 给出的数量超过订单剩余数量
                                        {
                                            json = Units.objectToJson(Constants.GRAB_ORDER_STATUS_TONNAGE_LACK, "订单数量不足", null);
                                            break;
                                        }
                                        case Constants.GRAB_ORDER_STATUS_FIRST_PASS_SUCCESS: {
                                            json = Units.objectToJson(Constants.GRAB_ORDER_STATUS_FIRST_PASS_SUCCESS, "第一次出价成功,请等待抢单结束", null);
                                            break;
                                        }
                                        case Constants.GRAB_ORDER_STATUS_SECOND_PASS_SUCCESS: {
                                            json = Units.objectToJson(Constants.GRAB_ORDER_STATUS_SECOND_PASS_SUCCESS, "第二次出价成功,请等待抢单结束", null);
                                            break;
                                        }
                                        case Constants.GRAB_ORDER_STATUS_CARTYPE_ERROR: {
                                            json = Units.objectToJson(Constants.GRAB_ORDER_STATUS_CARTYPE_ERROR, "车辆不符合抢单条件(类型不对或车长不够)", null);
                                            break;
                                        }
                                        case Constants.GRAB_ORDER_STATUS_CAR_UNAUTH: {
                                            json = Units.objectToJson(Constants.GRAB_ORDER_STATUS_CARTYPE_ERROR, "车辆未认证", null);
                                            break;
                                        }
                                        case Constants.GRAB_ORDER_STATUS_DRIVER_UNAUTH: {
                                            json = Units.objectToJson(Constants.GRAB_ORDER_STATUS_CARTYPE_ERROR, "司机未认证", null);
                                            break;
                                        }
                                        default: {
                                            json = Units.objectToJson(result, "抢单出错, 请联系管理员!", null);
                                        }
                                    }
                                }
                            }
                        } catch (ParseException ex) {
                            LOG.error("下发时间格式出错!", ex);
                            json = Units.objectToJson(-1, "下发时间格式出错!", null);
                        }
                    } else {
                        json = Units.objectToJson(Constants.GRAB_ORDER_STATUS_EXCEPTION, "App版本过时, 请更新到最新版App进行抢单!", null);
                    }
                    //System.out.println("range:" + orderTable.getCarRenge() + ",latitude:" + latitude + ",longitude:" + longitude + ",startLat:" + startSite.getSiteLatitude() + ",startLon:" + startSite.getSiteLongitude());
                    break;
                }
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="getCarGrabTimes(抢单次数)">
                case "getCarGrabTimes": {
                    int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "0");
                    int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "0");
                    OrderTableController controller = new OrderTableController();
                    int result = controller.getCarGrabTimes(taskID, carID);
                    json = Units.jsonStrToJson(0, "", "{\"grabTimes\":" + result + "}");
                    //System.out.println(json);
                    break;
                }
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="getGrabedOrder(抢单记录)">
                case "getGrabedOrder": {
                    int userID = Integer.valueOf((null != paramsJson.getString("userID")) ? paramsJson.getString("userID") : "0");
                    int driverID = Integer.valueOf((null != paramsJson.getString("driverID")) ? paramsJson.getString("driverID") : "0");
                    int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "0");
                    int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "0");
                    int nowPage = Integer.valueOf((null != paramsJson.getString("nowPage")) ? paramsJson.getString("nowPage") : "0");
                    int pageSize = Integer.valueOf((null != paramsJson.getString("pageSize")) ? paramsJson.getString("pageSize") : "0");
                    OrderGrabController controller = new OrderGrabController();
                    ArrayList<OrderGrab> result = controller.getOrderGrabList(userID, driverID, carID, -1, -1, taskID, null, null, null, nowPage, pageSize);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "订单记录为空", null);
                    }
                    break;
                }
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="revokeGrabOrder(撤销抢单)">
                case "revokeGrabOrder": {
                    int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "0");
                    int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "0");
                    String failReason = paramsJson.getString("reason");
                    OrderGrabController controller = new OrderGrabController();
                    int result = controller.revokeOrderGrab(taskID, carID, failReason);
                    String message;
                    if (result == 0) {
                        message = "申请成功!";
                    } else if (result == -1) {
                        message = "申请失败!";
                    } else if (result == 3) {
                        message = "已申请撤销!";
                    } else if (result == 1) {
                        message = "车辆已进厂或进厂已超时!";
                    } else if (result == 2) {
                        message = "抢单未成功!";
                    } else if (result == 4) {
                        message = "状态错误, 不能撤销!";
                    } else {
                        message = "申请出错!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="getBarGainAllGrab(议价所有抢单记录)">
                case "getBarGainAllGrab": {
                    int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "-1");
                    int nowPage = Integer.valueOf((null != paramsJson.getString("nowPage")) ? paramsJson.getString("nowPage") : "0");
                    int pageSize = Integer.valueOf((null != paramsJson.getString("pageSize")) ? paramsJson.getString("pageSize") : "0");
                    OrderGrabController controller = new OrderGrabController();
                    ArrayList<OrderGrab> result = controller.getOrderGrabList(-1, -1, -1, null, -1, -1, 2, taskID, null, null, null, null, nowPage, pageSize);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "订单记录为空", null);
                    }
                    break;
                }
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="getOrderGrabCarNum(获取用户已抢单车辆数目)">
                case "getOrderGrabCarNum": {
                    int userID = Integer.valueOf((null != paramsJson.getString("userID")) ? paramsJson.getString("userID") : "-1");
                    int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "-1");
                    OrderGrabController controller = new OrderGrabController();
                    int result = controller.getOrderGrabCarNum(taskID, userID);
                    json = Units.jsonStrToJson(0, "", "{\"grabedCarNum\":" + result + "}");
                    break;
                }
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="getGradedOnceCar(获取已经抢过一次的车辆信息)">
                case "getGradedOnceCar": {
                    int userID = Integer.valueOf((null != paramsJson.getString("userID")) ? paramsJson.getString("userID") : "-1");
                    int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "-1");
                    CarTableController controller = new CarTableController();
                    ArrayList<CarTable> result = controller.getGradedOnceCar(userID, taskID);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "记录为空!", null);
                    }

                    break;
                }
                // </editor-fold>

                /**
                 * 到达
                 */
                //<editor-fold  defaultstate="collapsed" desc="driverArrive">
                case "driverArrive": {
                    int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "-1");
                    int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "-1");
                    OrderGrabController controller = new OrderGrabController();
                    int result = controller.carArrive(taskID, carID, 3);
                    String message;
                    if (result == 0) {
                        message = "申请到达成功!";
                    } else if (result == -1) {
                        message = "申请到达失败!";
                    } else if (result == 1) {
                        message = "申请到达状态出错!";
                    } else {
                        message = "申请到达出错!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>
                //<editor-fold  defaultstate="collapsed" desc="distributerArrive">
                case "distributerArrive": {
                    int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "-1");
                    int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "-1");
                    OrderGrabController controller = new OrderGrabController();
                    int result = controller.carArrive(taskID, carID, 4);
                    String message;
                    if (result == 0) {
                        message = "确认到达成功!";
                    } else if (result == -1) {
                        message = "确认到达失败!";
                    } else if (result == 1) {
                        message = "确认到达状态出错!";
                    } else {
                        message = "确认到达出错!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getCarrier(获取承运商列表)">
                case "getCarrier": {
                    int carrierId = Integer.valueOf((null != paramsJson.getString("carrierId")) ? paramsJson.getString("carrierId") : "-1");
                    String carrierName = paramsJson.getString("carrierName");
                    int nowPage = Integer.valueOf((null != paramsJson.getString("nowPage")) ? paramsJson.getString("nowPage") : "0");
                    int pageSize = Integer.valueOf((null != paramsJson.getString("pageSize")) ? paramsJson.getString("pageSize") : "0");
                    CarrierController controller = new CarrierController();
                    ArrayList<CarrierInfo> result = controller.carrierGet(carrierId, carrierName, nowPage, pageSize);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "所得记录为空!", null);
                    }
                    break;
                }
                // </editor-fold>

                /**
                 * *******************************企业或个体************************************
                 */
                //<editor-fold defaultstate="collapsed" desc="login">
                case "login": {
                    //获取用户名
                    String username = paramsJson.getString("username");
                    //获取密码
                    String password = paramsJson.getString("password");
                    //获取其类型
                    String type = paramsJson.getString("type");
                    //获取手机别名
                    //String imei = paramsJson.getString("IMEI");
//                    String ipAddress = Units.getIpAddress(request);

                    LoginUser loginUser = new LoginUser();
                    int result = -1;

                    //<editor-fold defaultstate="collapsed" desc="判断不同类型的登录用户">
                    switch (type) {
                        case "distributor": {
                            //经销商
                            DistributorController controller = new DistributorController();
                            result = controller.distributorLogin(username, password, imei, ipAddress);
                            if (result == 0 || result == 2) {
                                Distributor user = controller.distributorGetWithUsername(username);
                                loginUser.setUserType(3);
                                loginUser.setUserID(user.getDistributorID());
                                loginUser.setLoginName(user.getDistributorUserName());
                                loginUser.setCompany(user.getDistributorName());
                                loginUser.setAddress(user.getDistributorAddress());
                                loginUser.setCompany(user.getDistributorCompany());
                                loginUser.setPhoneNum(user.getDistributorPhoneNumber());
                                loginUser.setImei(user.getLoginImei());
                            }
                            break;
                        }
                        case "enterprise": {
                            //企业用户
                            EnterpriseController controller = new EnterpriseController();
                            result = controller.enterpriseLogin(username, password, imei, ipAddress);
                            if (result == 0 || result == 2) {
                                Enterprise user = controller.enterpriseGetWithUserName(username);
                                loginUser.setUserType(1);
                                loginUser.setUserID(user.getEnterpriseID());
                                loginUser.setLoginName(user.getEnterpriseUserName());
                                loginUser.setAddress(user.getEnterpriseAddress());
                                loginUser.setCompany(user.getCarrierName());
                                loginUser.setPhoneNum(user.getEnterprisePhoneNumber());
                                loginUser.setAuthStatus(user.getAuthStatus());
                                loginUser.setImei(user.getLoginImei());
                            }
                            break;
                        }
                        case "driver": {
                            // 司机登录
                            DriverController controller = new DriverController();
                            result = controller.driverLogin(username, password, imei, ipAddress);
                            if (result == 0 || result == 2) {
                                Driver user = controller.driverGetWithUserName(username);
                                loginUser.setUserType(2);
                                loginUser.setUserID(user.getDriverID());
                                loginUser.setLoginName(user.getdUserName());
                                loginUser.setAddress("");
                                loginUser.setCompany(user.getEnterpriseName());
                                loginUser.setPhoneNum(user.getdPhoneNumber());
                                loginUser.setImei(user.getLoginImei());
                            }
                            break;
                        }
                        default:
                            //类型参数错误
                            break;
                    }
                    //</editor-fold>

                    if (result == 0) {
                        json = Units.objectToJson(result, "登录成功!", loginUser);
                    } else if (result == 2) {
                        json = Units.objectToJson(result, "不同设备登录!", loginUser);
                    } else if (result == 1) {
                        json = Units.objectToJson(result, "用户名或密码错误!", null);
                    } else {
                        json = Units.objectToJson(result, "登录出错!", null);
                    }
//                    System.out.println("json:" + json);
                    //<editor-fold defaultstate="collapsed" desc="登录信息验证成功">
                    /*
                     if (result == 0) {
                     System.out.println("IMEI:" + loginUser.getImei());
                     LoginUser existUser = isHasUser(loginUser);
                     if (null != existUser) {
                     //保存的用户登录信息中, IMEI号是否与当前请求相同
                     //                            LOG.info("Exist IMEI:", existUser.getImei());
                     System.out.println("Exist IMEI:" + existUser.getImei());
                     if (null == loginUser.getImei() || loginUser.getImei().equalsIgnoreCase("imei")) {
                     json = Units.objectToJson(-1, "未获取到设备IMEI, 该设备不能登录!", null);
                     } else {
                     if (existUser.getImei() == null) {
                     //                                    loginUserList.remove(imei);
                     loginUserList.put(imei, loginUser);
                     session.setAttribute("user", loginUser);
                     json = Units.objectToJson(result, "登录成功!", loginUser);
                     } else if (existUser.getImei().equalsIgnoreCase(loginUser.getImei())) {
                     loginUserList.remove(imei);
                     loginUserList.put(imei, loginUser);
                     session.setAttribute("user", loginUser);
                     json = Units.objectToJson(result, "登录成功!", loginUser);
                     } else {
                     SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                     Date operateTime = format.parse(existUser.getLastOperateTime());
                     //IMEI不同时, 判断已登录用户是否在线
                     if (offlineTime > Units.getIntervalTimeWithNow(operateTime)) {
                     JsonObject object = new JsonObject();
                     object.addProperty("imei", loginUser.getImei());
                     PushUnits.pushNotifationWithAlias(existUser.getImei(), "另一个设备请求登录!", "other_login", object);
                     json = Units.objectToJson(-99, "该用户已经在另一个设备登录, 等待对方允许...?", null);
                     } else {
                     loginUserList.remove(imei);
                     loginUserList.put(imei, loginUser);
                     session.setAttribute("user", loginUser);
                     json = Units.objectToJson(result, "登录成功!", loginUser);
                     }
                     }
                     }
                     } else {
                     loginUserList.put(imei, loginUser);
                     session.setAttribute("user", loginUser);
                     json = Units.objectToJson(result, "登录成功!", loginUser);
                     }
                     }
                     */
                    //</editor-fold>
                    break;
                }
                // </editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getUserInfo(找回密码和验证码登录时使用)">
                case "getUserInfo": {
                    String phoneCode = paramsJson.getString("phoneCode");
//                    System.out.println("phoneCode:" + phoneCode + ",session phoneCode:" + session.getAttribute("phoneCode"));
                    if (null != session && null != phoneCode && phoneCode.equalsIgnoreCase(String.valueOf(session.getAttribute("phoneCode")))) {
                        //获取其类型
                        String type = paramsJson.getString("type");
                        String phoneNum = paramsJson.getString("phoneNum");
                        LoginUser userInfo = new LoginUser();
                        switch (type) {
                            case "distributor": {
                                DistributorController controller = new DistributorController();
                                Distributor distributor = controller.distributorGetWithUsername(phoneNum);

                                if (null != distributor) {
                                    userInfo.setImei(imei);
                                    userInfo.setLoginName(Units.getNowTime());
                                    userInfo.setLastOperateTime(Units.getNowTime());
                                    userInfo.setUserType(3);
                                    userInfo.setUserID(distributor.getDistributorID());
                                    userInfo.setLoginName(distributor.getDistributorUserName());
                                    userInfo.setAddress(distributor.getDistributorAddress());
                                    userInfo.setCompany(distributor.getDistributorCompany());
                                    userInfo.setPhoneNum(distributor.getDistributorPhoneNumber());

                                    json = Units.objectToJson(0, "", userInfo);
                                } else {
                                    json = Units.objectToJson(-1, "改手机号未注册为经销商!", null);
                                }

                                break;
                            }
                            case "enterprise": {
                                EnterpriseController controller = new EnterpriseController();
                                Enterprise enterprise = controller.enterpriseGetWithUserName(phoneNum);

                                if (null != enterprise) {
                                    userInfo.setImei(imei);
                                    userInfo.setLoginName(Units.getNowTime());
                                    userInfo.setLastOperateTime(Units.getNowTime());
                                    userInfo.setUserType(1);
                                    userInfo.setUserID(enterprise.getEnterpriseID());
                                    userInfo.setLoginName(enterprise.getEnterpriseUserName());
                                    userInfo.setAddress(enterprise.getEnterpriseAddress());
                                    userInfo.setCompany(enterprise.getCarrierName());
                                    userInfo.setPhoneNum(enterprise.getEnterprisePhoneNumber());
                                    userInfo.setAuthStatus(enterprise.getAuthStatus());

                                    json = Units.objectToJson(0, "", userInfo);
                                } else {
                                    json = Units.objectToJson(-1, "改手机号未注册为车主!", null);
                                }
                                break;
                            }
                            case "driver": {
                                DriverController controller = new DriverController();
                                Driver driver = controller.driverGetWithUserName(phoneNum);
                                if (null != driver) {
                                    userInfo.setImei(imei);
                                    userInfo.setLoginName(Units.getNowTime());
                                    userInfo.setLastOperateTime(Units.getNowTime());
                                    userInfo.setUserType(2);
                                    userInfo.setUserID(driver.getDriverID());
                                    userInfo.setLoginName(driver.getdUserName());
                                    userInfo.setAddress("");
                                    userInfo.setCompany(driver.getEnterpriseName());
                                    userInfo.setPhoneNum(driver.getdPhoneNumber());
                                    userInfo.setAuthStatus(driver.getAuthStatus());

                                    json = Units.objectToJson(0, "", userInfo);
                                } else {
                                    json = Units.objectToJson(-1, "该手机号未注册为司机用户!", null);
                                }

                                break;
                            }
                            default:
                                //类型参数错误
                                json = Units.objectToJson(-1, "类型参数错误!", null);
                                break;
                        }
                    } else {
                        json = Units.objectToJson(-1, "验证码输入错误或验证码已过期!", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="allowLogin">
                case "allowLogin": {
                    String desIMEI = paramsJson.getString("desIMEI");
                    String type = paramsJson.getString("type");
                    int userId = paramsJson.getIntValue("userId");
                    LoginUser user = new LoginUser();
                    user.setUserID(userId);

                    switch (type) {
                        case "distributor": {
                            user.setUserType(3);
                            //经销商
                            break;
                        }
                        case "enterprise": {
                            user.setUserType(1);
                            //企业用户
                            break;
                        }
                        case "driver": {
                            // 司机
                            user.setUserType(2);
                            break;
                        }
                    }
                    /*
                     LoginUser exsitsUser = isHasUser(user);
                     if (exsitsUser != null) {
                     loginUserList.remove(exsitsUser.getImei());
                     PushUnits.pushMessageWithAlias(desIMEI, "另一个设备允许登录!", "allow_login", JSONObject.toJSONString(exsitsUser, features));
                     json = Units.objectToJson(0, "允许登录成功!", null);
                     } else {
                     json = Units.objectToJson(-1, "允许登录失败!", null);
                     }
                     */
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="loginOut(登出)">
                case "loginOut": {
                    int userId = Integer.valueOf((null != paramsJson.getString("userId")) ? paramsJson.getString("userId") : "-1");
                    int userType = Integer.valueOf((null != paramsJson.getString("userType")) ? paramsJson.getString("userType") : "-1");
                    LoginUser user = new LoginUser();
                    user.setUserID(userId);
                    user.setUserType(userType);
                    json = Units.objectToJson(0, "登出成功!", null);
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getSMSVerifyCode(获取短信验证码)">
                case "getSMSVerifyCode": {
                    String phoneNum = paramsJson.getString("phoneNum");
                    if (!StringUtils.isEmptyOrWhitespaceOnly(phoneNum)) {
                        String phoneCode = Units.createOnlyNumPhoneValidateCode(4);
                        json = Units.sendSMSVerificationCode(phoneNum, phoneCode);
                        if (session == null) {
                            session = request.getSession(true);
                        }
                        session.setAttribute("phoneCode", phoneCode);
                        session.setMaxInactiveInterval(5 * 60);
                    } else {
                        json = Units.objectToJson(-1, "手机号不能为空", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getDeviceSwitchSMSVerifyCode(获取设备切换短信验证码)">
                case "getDeviceSwitchSMSVerifyCode": {
                    String phoneNum = paramsJson.getString("phoneNum");
                    if (!StringUtils.isEmptyOrWhitespaceOnly(phoneNum)) {
                        String phoneCode = Units.createOnlyNumPhoneValidateCode(4);
                        json = Units.sendSMSVerificationCode(phoneNum, phoneCode, Constants.SMS_PLATFORM_NEW_DEVICE_TEMPLATE_ID, Constants.SMS_NEW_DEVICE_EXPIRED_MINUTE);
                        if (session == null) {
                            session = request.getSession(true);
                        }
                        session.setAttribute("phoneCode", phoneCode);
                        session.setMaxInactiveInterval(120 * 60);
                    } else {
                        json = Units.objectToJson(-1, "手机号不能为空", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="switchDeviceLogin(设备切换登录)">
                case "switchDeviceLogin": {
                    String phoneCode = paramsJson.getString("phoneCode");
                    int userId = paramsJson.getIntValue("userId");
                    int userType = paramsJson.getIntValue("userType");
                    String username = paramsJson.getString("userName");
                    if (null != session && null != phoneCode && phoneCode.equalsIgnoreCase(String.valueOf(session.getAttribute("phoneCode")))) {
                        LoginUserController controller = new LoginUserController();
                        ArrayList<LoginUser> tmpRes = controller.getLoginUserList(userType, userId, null, null);
                        if (tmpRes != null) {
                            LoginUser user = tmpRes.get(0);
                            if (user.getUpdateTime() != null && Units.isToday(user.getUpdateTime())) {
                                json = Units.objectToJson(-1, "一天内只允许切换一次设备, 您超过允许次数!", null);
                                break;
                            }
                        }
                        int result = controller.updateLoginUserImei(userType, userId, imei);
                        if (result == 0) {
                            LoginUser user = new LoginUser();
                            if (userType == 1) {
                                EnterpriseController controller1 = new EnterpriseController();
                                Enterprise enterprise = controller1.enterpriseGetWithUserName(username);
                                user.setUserType(1);
                                user.setUserID(enterprise.getEnterpriseID());
                                user.setLoginName(enterprise.getEnterpriseUserName());
                                user.setAddress(enterprise.getEnterpriseAddress());
                                user.setCompany(enterprise.getCarrierName());
                                user.setPhoneNum(enterprise.getEnterprisePhoneNumber());
                                user.setAuthStatus(enterprise.getAuthStatus());
                                user.setImei(enterprise.getLoginImei());
                            }
                            if (userType == 2) {
                                DriverController controller1 = new DriverController();
                                Driver driver = controller1.driverGetWithUserName(username);
                                user.setUserType(2);
                                user.setUserID(driver.getDriverID());
                                user.setLoginName(driver.getdUserName());
                                user.setAddress("");
                                user.setCompany(driver.getEnterpriseName());
                                user.setPhoneNum(driver.getdPhoneNumber());
                                user.setImei(driver.getLoginImei());
                            }
                            if (userType == 3) {
                                DistributorController controller1 = new DistributorController();
                                Distributor distributor = controller1.distributorGetWithUsername(username);
                                user.setUserType(3);
                                user.setUserID(distributor.getDistributorID());
                                user.setLoginName(distributor.getDistributorUserName());
                                user.setCompany(distributor.getDistributorName());
                                user.setAddress(distributor.getDistributorAddress());
                                user.setCompany(distributor.getDistributorCompany());
                                user.setPhoneNum(distributor.getDistributorPhoneNumber());
                                user.setImei(distributor.getLoginImei());
                            }
                            json = Units.objectToJson(result, "登录成功!", user);
                        } else {
                            json = Units.objectToJson(result, "登录失败, 请联系服务商!", null);
                        }
                    } else {
                        json = Units.objectToJson(-1, "验证码输入错误或验证码已过期!", null);
                    }
                    break;
                }
                //</editor-fold>
                
                //<editor-fold defaultstate="collapsed" desc="registerEnterpirse">
                case "registerEnterpirse": {
                    //int type = Integer.valueOf((null != paramsJson.getString("type")) ? paramsJson.getString("type") : "-1");
                    String phoneCode = paramsJson.getString("phoneCode");
//                    System.out.println("phoneCode:" + phoneCode + ",session phoneCode:" + session.getAttribute("phoneCode"));
                    if (null != session && null != phoneCode && phoneCode.equalsIgnoreCase(String.valueOf(session.getAttribute("phoneCode")))) {
                        String username = paramsJson.getString("username");
                        String password = paramsJson.getString("password");
                        String name = paramsJson.getString("name");
                        String address = paramsJson.getString("address");
                        String contact = paramsJson.getString("contact");
                        String phoneNumber = (null != paramsJson.getString("phoneNumber")) ? paramsJson.getString("phoneNumber") : paramsJson.getString("username");
                        String license = paramsJson.getString("license");
                        int carrierId = Integer.valueOf((null != paramsJson.getString("carrierId")) ? paramsJson.getString("carrierId") : "-1");
                        String carrierName = paramsJson.getString("carrierName");
                        EnterpriseController controller = new EnterpriseController();
                        int result = controller.enterpriseAdd(-1, username, password, name, address, contact, phoneNumber, license, carrierId, carrierName);
                        String message;
                        if (result == 0) {
                            message = "添加成功!";
                        } else if (result == -1) {
                            message = "添加失败!";
                        } else {
                            message = "该手机号已经注册!";
                        }
                        json = Units.objectToJson(result, message, null);
                    } else {
                        json = Units.objectToJson(-1, "验证码输入错误或验证码已过期!", null);
                    }
                    break;
                }
                // </editor-fold>
                //<editor-fold defaultstate="collapsed" desc="authEnterpirse">
                case "authEnterpirse": {
                    //int type = Integer.valueOf((null != paramsJson.getString("type")) ? paramsJson.getString("type") : "-1");
                    String phoneCode = paramsJson.getString("phoneCode");
                    if (null != phoneCode && null != session && phoneCode.equalsIgnoreCase(String.valueOf(session.getAttribute("phoneCode")))) {
                        int enterpriseId = Integer.valueOf((null != paramsJson.getString("enterpriseId")) ? paramsJson.getString("enterpriseId") : "-1");
                        String name = paramsJson.getString("name");
                        String phoneNumber = paramsJson.getString("phoneNumber");
                        String license = paramsJson.getString("cardNo");
                        String carrierName = paramsJson.getString("carrierName");
                        EnterpriseController controller = new EnterpriseController();
                        int result = controller.enterpriseAuth(enterpriseId, name, phoneNumber, license, carrierName);
                        String message;
                        if (result == 0) {
                            message = "认证成功!";
                        } else if (result == -1) {
                            message = "认证失败!";
                        } else if (result == 1) {
                            message = "承运商输入错误!";
                        } else if (result == 2) {
                            message = "该手机号已存在, 请直接使用该手机号登录!";
                        } else {
                            message = "认证出错!";
                        }
                        json = Units.objectToJson(result, message, null);
                    } else {
                        json = Units.objectToJson(-1, "手机验证码输入错误!", null);
                    }
                    break;
                }
                // </editor-fold>
                //<editor-fold defaultstate="collapsed" desc="addEnterpirse">
                case "addEnterpirse": {
                    int type = Integer.valueOf((null != paramsJson.getString("type")) ? paramsJson.getString("type") : "-1");
                    String username = paramsJson.getString("username");
                    String password = paramsJson.getString("password");
                    String name = paramsJson.getString("name");
                    String address = paramsJson.getString("address");
                    String contact = paramsJson.getString("contact");
                    String phoneNumber = paramsJson.getString("phoneNumber");
                    String license = paramsJson.getString("license");
                    int carrierId = Integer.valueOf((null != paramsJson.getString("carrierId")) ? paramsJson.getString("carrierId") : "-1");
                    String carrierName = paramsJson.getString("carrierName");
                    EnterpriseController controller = new EnterpriseController();
                    int result = controller.enterpriseAdd(type, username, password, name, address, contact, phoneNumber, license, carrierId, carrierName);
                    String message;
                    if (result == 0) {
                        message = "添加成功!";
                    } else if (result == -1) {
                        message = "添加失败!";
                    } else {
                        message = "该手机号已经注册!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                // </editor-fold>
                //<editor-fold defaultstate="collapsed" desc="updateEnterprise">
                case "updateEnterprise": {
                    int enterpriseId = Integer.valueOf((null != paramsJson.getString("enterpriseId")) ? paramsJson.getString("enterpriseId") : "-1");
                    //String username = paramsJson.getString("username");
                    String password = paramsJson.getString("password");
                    String name = paramsJson.getString("name");
                    String address = paramsJson.getString("address");
                    String contact = paramsJson.getString("contact");
                    String phoneNumber = paramsJson.getString("phoneNumber");
                    EnterpriseController controller = new EnterpriseController();
                    int result = controller.enterpriseUpdate(enterpriseId, password, name, address, contact, phoneNumber);
                    String message;
                    if (result == 0) {
                        message = "更新成功!";
                    } else if (result == -1) {
                        message = "更新失败!";
                    } else {
                        message = "更新出错!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                // </editor-fold>
                //<editor-fold defaultstate="collapsed" desc="deleteEnterprise">
                case "deleteEnterprise": {
                    int enterpriseId = Integer.valueOf((null != paramsJson.getString("enterpriseId")) ? paramsJson.getString("enterpriseId") : "-1");
                    EnterpriseController controller = new EnterpriseController();
                    int result = controller.enterpriseDelete(enterpriseId);
                    String message;
                    if (result == 0) {
                        message = "删除成功!";
                    } else if (result == -1) {
                        message = "删除失败!";
                    } else {
                        message = "删除出错!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                // </editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getEnterprise">
                case "getEnterprise": {
                    int enterpriseID = Integer.valueOf((null != paramsJson.getString("enterpriseID")) ? paramsJson.getString("enterpriseID") : "-1");
                    String enterpriseUserName = paramsJson.getString("username");
                    int nowPage = Integer.valueOf((null != paramsJson.getString("nowPage")) ? paramsJson.getString("nowPage") : "0");
                    int pageSize = Integer.valueOf((null != paramsJson.getString("pageSize")) ? paramsJson.getString("pageSize") : "0");
                    EnterpriseController controller = new EnterpriseController();
                    ArrayList<Enterprise> result = controller.enterpriseGet(enterpriseID, enterpriseUserName, nowPage, pageSize);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "所得记录为空!", null);
                    }
                    break;
                }
                // </editor-fold>

                /**
                 * *******************************车辆相关************************************
                 */
                //<editor-fold defaultstate="collapsed" desc="addCarTable">
                case "addCarTable": {
                    CarTableController controller = new CarTableController();
                    String carNumber = paramsJson.getString("carNumber");

                    String verifieRes = CarNumberVerifie.vehnofVerifie(carNumber);
                    JSONArray array = JSONObject.parseArray(verifieRes);
                    if (array != null && array.size() > 0) {// 如果存在取第一个系统编号
                        String systemNo = array.getJSONObject(0).getString("SystemNo");
                        String carType = paramsJson.getString("carType");
                        float carWeight = Float.valueOf((null != paramsJson.getString("carWeight")) ? paramsJson.getString("carWeight") : "0");
                        float carLength = Float.valueOf((null != paramsJson.getString("carLength")) ? paramsJson.getString("carLength") : "0");
//                    String maintainDate = paramsJson.getString("maintainDate");
//                    String annualDate = paramsJson.getString("annualDate");
                        String insuranceExpireDate = paramsJson.getString("insuranceExpireDate");
                        String carImagePath1 = paramsJson.getString("carImagePath1");
                        String carImagePath2 = paramsJson.getString("carImagePath2");
                        String carImagePath3 = paramsJson.getString("carImagePath3");
                        int enterpriseID = Integer.valueOf((null != paramsJson.getString("enterpriseID")) ? paramsJson.getString("enterpriseID") : "0");
                        int driverID = Integer.valueOf((null != paramsJson.getString("driverID")) ? paramsJson.getString("driverID") : "0");
                        CarTableController.AddCarResult result = controller.addCarTable(systemNo, carNumber, carType, carWeight, carLength, insuranceExpireDate, carImagePath1, carImagePath2, carImagePath3, enterpriseID, driverID);
                        String message;
                        if (result.result == 0) {
                            message = "添加成功!";
                        } else if (result.result == -1) {
                            message = "添加失败!";
                        } else if (result.result == 1) {
                            message = "车牌号已存在!";
                        } else {
                            message = "添加出错";
                        }
                        json = Units.objectToJson(result.result, message, result.registerTime);
                    } else {
                        json = Units.objectToJson(-1, "系统不存在该车牌号", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="bindDefaultDriver">
                case "bindDefaultDriver": {
                    int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "0");
                    int driverID = Integer.valueOf((null != paramsJson.getString("driverID")) ? paramsJson.getString("driverID") : "0");
                    CarTableController controller = new CarTableController();
                    int result = controller.bindDriver(carID, driverID);
                    String message;
                    if (result == 0) {
                        message = "绑定成功";
                    } else if (result == -1) {
                        message = "绑定失败";
                    } else {
                        message = "绑定出错";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getCarTable">
                case "getCarTable": {
                    int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "-1");
                    int userID = Integer.valueOf((null != paramsJson.getString("userID")) ? paramsJson.getString("userID") : "-1");
                    int driverID = Integer.valueOf((null != paramsJson.getString("driverID")) ? paramsJson.getString("driverID") : "-1");
                    int nowPage = Integer.valueOf((null != paramsJson.getString("nowPage")) ? paramsJson.getString("nowPage") : "0");
                    int pageSize = Integer.valueOf((null != paramsJson.getString("pageSize")) ? paramsJson.getString("pageSize") : "0");
                    String systemNo = paramsJson.getString("systemNo");
                    String carNumber = paramsJson.getString("carNumber");
                    String carType = paramsJson.getString("carType");
                    CarTableController controller = new CarTableController();
                    ArrayList<CarTable> result = controller.getCarList(carID, userID, carType, systemNo, carNumber, driverID, nowPage, pageSize);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "记录为空", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="authCarTable">
                case "authCarTable": {
                    int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "-1");
                    String carType = paramsJson.getString("carType");
                    float carWeight = Float.valueOf((null != paramsJson.getString("carWeight")) ? paramsJson.getString("carWeight") : "0");
                    float carLength = Float.valueOf((null != paramsJson.getString("carLength")) ? paramsJson.getString("carLength") : "0");
                    CarTableController controller = new CarTableController();
                    int result = controller.authCarTable(carID, carType, carWeight, carLength);
                    String message;
                    if (result == 0) {
                        message = "认证成功!";
                    } else if (result == 1) {
                        message = "车辆信息已认证, 不能再次认证!";
                    } else if (result == -1) {
                        message = "认证出错!";
                    } else {
                        message = "认证失败!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="updateCarTable">
                case "updateCarTable": {
                    int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "-1");
                    String carType = paramsJson.getString("carType");
                    float carWeight = Float.valueOf((null != paramsJson.getString("carWeight")) ? paramsJson.getString("carWeight") : "0");
                    float carLength = Float.valueOf((null != paramsJson.getString("carLength")) ? paramsJson.getString("carLength") : "0");
                    String insuranceExpireDate = paramsJson.getString("insuranceExpireDate");
                    String carImagePath1 = paramsJson.getString("carImagePath1");
                    String carImagePath2 = paramsJson.getString("carImagePath2");
                    String carImagePath3 = paramsJson.getString("carImagePath3");
                    CarTableController controller = new CarTableController();
                    int result = controller.updateCarTable(carID, carType, carWeight, carLength, insuranceExpireDate, carImagePath1, carImagePath2, carImagePath3, -1, -1);
                    String message;
                    if (result == 0) {
                        message = "更新成功!";
                    } else if (result == -1) {
                        message = "更新失败!";
                    } else {
                        message = "更新出错!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="applyDeleteCarTable">
                case "applyDeleteCarTable": {
                    int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "-1");
                    CarTableController controller = new CarTableController();
                    int result = controller.applyDeleteCarTable(carID);
                    String message;
                    if (result == 0) {
                        message = "申请删除成功!";
                    } else if (result == -1) {
                        message = "申请删除失败!";
                    } else {
                        message = "申请删除出错!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="deleteCarTable">
                case "deleteCarTable": {
                    int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "-1");
                    CarTableController controller = new CarTableController();
                    int result = controller.deleteCarTable(carID);
                    String message;
                    if (result == 0) {
                        message = "删除成功!";
                    } else if (result == -1) {
                        message = "删除失败!";
                    } else if (result == 1) {
                        message = "车辆有正在执行的任务!";
                    } else {
                        message = "删除出错!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getUserAllCar">
                case "getUserAllCar": {
                    int userID = Integer.valueOf((null != paramsJson.getString("userID")) ? paramsJson.getString("userID") : "-1");
                    CarTableController controller = new CarTableController();
                    ArrayList<CarTable> result = controller.getCarListWithUserID(userID, 1, Constants.MAX_RECORD);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getCarDetail">
                case "getCarDetail": {
                    int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "-1");
                    CarTableController controller = new CarTableController();
                    CarTable carTable = controller.getCarDetailWithCarID(carID);
                    if (null != carTable) {
                        json = Units.objectToJson(0, "", carTable);
                    } else {
                        json = Units.objectToJson(1, "车辆ID不存在", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getAllCarLocation">
                case "getAllCarLocation": {
                    int userID = Integer.valueOf((null != paramsJson.getString("userID")) ? paramsJson.getString("userID") : "-1");
                    CarTableController controller = new CarTableController();
                    ArrayList<CarTable> result = controller.getCarListWithUserID(userID, 1, Constants.MAX_RECORD);
                    if (null != result) {
                        ArrayList<CarLocation> locations = controller.getCarlistLocation(result);
                        json = Units.listToJson(locations, 0);
                    } else {
                        json = Units.objectToJson(-1, "车辆记录为空", null);
                    }
                    //System.out.println(json);
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getCarLocation">
                case "getCarLocation": {
                    int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "-1");
                    CarTableController controller = new CarTableController();
                    CarLocation location = controller.getCarLocationWithID(carID);
                    json = Units.objectToJson(0, "", location);
                    break;
                }
                //</editor-fold>

                /**
                 * *******************************司机相关************************************
                 */
                //<editor-fold defaultstate="collapsed" desc="addDriver">
                case "addDriver": {
                    int enterpriseID = Integer.valueOf((null != paramsJson.getString("userID")) ? paramsJson.getString("userID") : "-1");
                    String name = paramsJson.getString("name");
                    String phoneNumber = paramsJson.getString("phoneNumber");
                    String license = paramsJson.getString("license");
                    DriverController controller = new DriverController();
                    int result = controller.driverAdd(enterpriseID, name, phoneNumber, license);
                    String message;
                    if (result == 0) {
                        message = "添加成功!";
                    } else if (result == -1) {
                        message = "添加失败!";
                    } else if (result == 1) {
                        message = "手机号已经注册!";
                    } else {
                        message = "添加出错!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="authDriver">
                case "authDriver": {
                    int driverID = Integer.valueOf((null != paramsJson.getString("driverID")) ? paramsJson.getString("driverID") : "-1");
                    String name = paramsJson.getString("name");
                    String phoneNumber = paramsJson.getString("phoneNumber");
                    String license = paramsJson.getString("license");
                    DriverController controller = new DriverController();
                    int result = controller.driverAuth(driverID, name, phoneNumber, license);
                    String message;
                    if (result == 0) {
                        message = "认证成功!";
                    } else if (result == -1) {
                        message = "认证失败!";
                    } else {
                        message = "认证出错!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="updateDriver">
                case "updateDriver": {
                    int driverID = Integer.valueOf((null != paramsJson.getString("driverID")) ? paramsJson.getString("driverID") : "-1");
                    String name = paramsJson.getString("name");
                    String password = paramsJson.getString("password");
                    String phoneNumber = paramsJson.getString("phoneNumber");
                    String license = paramsJson.getString("license");
                    DriverController controller = new DriverController();
                    int result = controller.driverUpdate(driverID, name, password, phoneNumber, license);
                    String message;
                    if (result == 0) {
                        message = "更新成功!";
                    } else if (result == -1) {
                        message = "更新失败!";
                    } else {
                        message = "更新出错!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="applyDriverDelete">
                case "applyDriverDelete": {
                    int driverID = Integer.valueOf((null != paramsJson.getString("driverID")) ? paramsJson.getString("driverID") : "-1");
                    DriverController controller = new DriverController();
                    int result = controller.applyDriverDelete(driverID);
                    String message;
                    if (result == 0) {
                        message = "申请删除成功!";
                    } else if (result == -1) {
                        message = "申请删除失败!";
                    } else {
                        message = "申请删除出错!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="deleteDriver">
                case "deleteDriver": {
                    int driverID = Integer.valueOf((null != paramsJson.getString("driverID")) ? paramsJson.getString("driverID") : "-1");
                    DriverController controller = new DriverController();
                    int result = controller.driverDelete(driverID);
                    String message;
                    if (result == 0) {
                        message = "删除成功!";
                    } else if (result == -1) {
                        message = "删除失败!";
                    } else if (result == 1) {
                        message = "司机有正在执行任务, 不能删除!";
                    } else {
                        message = "删除出错!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getDriver">
                case "getDriver": {
                    int driverID = Integer.valueOf((null != paramsJson.getString("driverID")) ? paramsJson.getString("driverID") : "-1");
                    int enterpriseID = Integer.valueOf((null != paramsJson.getString("userID")) ? paramsJson.getString("userID") : "-1");
                    String dUserName = paramsJson.getString("username");
                    int nowPage = Integer.valueOf((null != paramsJson.getString("nowPage")) ? paramsJson.getString("nowPage") : "0");
                    int pageSize = Integer.valueOf((null != paramsJson.getString("pageSize")) ? paramsJson.getString("pageSize") : "0");
                    DriverController controller = new DriverController();
                    ArrayList<Driver> result = controller.driverGet(driverID, enterpriseID, dUserName, nowPage, pageSize);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "所得记录为空!", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getUserAllDriver">
                case "getUserAllDriver": {
                    int enterpriseID = Integer.valueOf((null != paramsJson.getString("userID")) ? paramsJson.getString("userID") : "-1");
                    DriverController controller = new DriverController();
                    ArrayList<Driver> result = controller.driverGet(-1, enterpriseID, null, 1, Constants.MAX_RECORD);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "所得记录为空!", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getDriverDetail">
                case "getDriverDetail": {
                    int driverID = Integer.valueOf((null != paramsJson.getString("driverID")) ? paramsJson.getString("driverID") : "-1");
                    DriverController controller = new DriverController();
                    Driver driver = controller.driverDetailWithID(driverID);
                    if (null != driver) {
                        json = Units.objectToJson(0, "", driver);
                    } else {
                        json = Units.objectToJson(1, "司机ID不存在", null);
                    }
                    break;
                }
                //</editor-fold>

                /**
                 * *******************************经销商相关************************************
                 */
                //<editor-fold defaultstate="collapsed" desc="getOrderGrabDisTopTwoNoGroup(获取经销商所有订单及对应订单前两条抢单记录,不分组)">
                case "getOrderGrabDisTopTwoNoGroup": {
                    int distributorID = Integer.valueOf((null != paramsJson.getString("distributorID")) ? paramsJson.getString("distributorID") : "-1");
                    OrderGrabController controller = new OrderGrabController();
                    ArrayList<OrderGrab> result = controller.getOrderGrabDisTopTwo(distributorID);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "记录为空!", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getOrderGrabDisTopTwo(获取经销商所有订单及对应订单前两条抢单记录)">
                case "getOrderGrabDisTopTwo": {
                    int distributorID = Integer.valueOf((null != paramsJson.getString("distributorID")) ? paramsJson.getString("distributorID") : "-1");
                    OrderGrabController controller = new OrderGrabController();
                    ArrayList<OrderGrab> result = controller.getOrderGrabDisTopTwo(distributorID);
                    ArrayList<OrderTableTwoTopGrab> twoTopGrabs = new ArrayList<>();
                    for (OrderGrab grab : result) {
                        int taskID = grab.getTaskID();

                        boolean isExist = false;
                        int index = 0;
                        for (OrderTableTwoTopGrab twoTopGrab : twoTopGrabs) {
                            if (twoTopGrab.getTaskID() == taskID) {
                                isExist = true;
                                break;
                            }
                            index++;
                        }

                        //如果存在直接添加
                        if (isExist) {
                            twoTopGrabs.get(index).getGrabList().add(grab);
                        } else {
                            OrderTableTwoTopGrab twoTopGrab = new OrderTableTwoTopGrab();
                            twoTopGrab.setTaskID(grab.getTaskID());
                            twoTopGrab.setTaskSerial(grab.getTaskSerial());
                            twoTopGrab.setEndPlace(grab.getEndPlace());
                            twoTopGrab.setEndPlaceName(grab.getEndPlaceName());
                            twoTopGrab.setStartPlace(grab.getStartPlace());
                            twoTopGrab.setStartPlaceName(grab.getStartPlaceName());
                            ArrayList<OrderGrab> grabList = new ArrayList<>();
                            grabList.add(grab);
                            twoTopGrab.setGrabList(grabList);

                            twoTopGrabs.add(twoTopGrab);
                        }
                    }

                    json = Units.listToJson(twoTopGrabs, 0);
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getDisOrderList(获取经销商的订单列表)">
                case "getDisOrderList": {
                    int distributorID = Integer.valueOf((null != paramsJson.getString("distributorID")) ? paramsJson.getString("distributorID") : "-1");
                    int listType = Integer.valueOf((null != paramsJson.getString("listType")) ? paramsJson.getString("listType") : "-1");
                    int nowPage = Integer.valueOf((null != paramsJson.getString("nowPage")) ? paramsJson.getString("nowPage") : "0");
                    int pageSize = Integer.valueOf((null != paramsJson.getString("pageSize")) ? paramsJson.getString("pageSize") : "0");
                    String startTime = paramsJson.getString("startTime");
                    String endTime = paramsJson.getString("endTime");
                    OrderTableController controller = new OrderTableController();
                    ArrayList<OrderTable> result = controller.getTaskList(-1, null, listType, -1, -1, distributorID, null, -1, startTime, endTime, null, null, nowPage, pageSize);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "订单记录为空", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getDisOrderGrabList(经销商抢单信息)">
                case "getDisOrderGrabList": {
                    int distributorID = Integer.valueOf((null != paramsJson.getString("distributorID")) ? paramsJson.getString("distributorID") : "-1");
                    int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "-1");
                    String taskSerial = paramsJson.getString("taskSerial");
                    int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "-1");
                    String carNumber = paramsJson.getString("carNumber");
                    int driverID = Integer.valueOf((null != paramsJson.getString("driverID")) ? paramsJson.getString("driverID") : "-1");
                    String driverName = paramsJson.getString("driverName");
                    String sendTime = paramsJson.getString("sendTime");
                    int nowPage = Integer.valueOf((null != paramsJson.getString("nowPage")) ? paramsJson.getString("nowPage") : "0");
                    int pageSize = Integer.valueOf((null != paramsJson.getString("pageSize")) ? paramsJson.getString("pageSize") : "0");
                    OrderGrabController controller = new OrderGrabController();
                    ArrayList<OrderGrabDis> result = controller.getDisOrderGrabList(distributorID, taskID, taskSerial, carID, carNumber, driverID, driverName, sendTime, nowPage, pageSize);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "所得记录为空!", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getOrderCarStatus(获取车辆状态记录)">
                case "getOrderCarStatus": {
                    int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "-1");
                    int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "-1");
                    OrderCarStatusController controller = new OrderCarStatusController();
                    ArrayList<OrderCarStatus> result = controller.getOrderCarStatusList(carID, taskID);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "车辆状态记录为空", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getOrderNewestCarStatus(获取最新车辆状态记录)">
                case "getOrderNewestCarStatus": {
                    int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "-1");
                    int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "-1");
                    OrderCarStatusController controller = new OrderCarStatusController();
                    ArrayList<OrderCarStatus> result = controller.getOrderCarStatusList(carID, taskID);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "车辆状态记录为空", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getCarHistory(获取轨迹回放数据)">
                case "getCarHistory": {
                    int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "-1");
                    int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "-1");
                    OrderCarStatusController controller = new OrderCarStatusController();
                    HashMap<String, String> hashMap = controller.getHistoryTime(carID, taskID);
                    String systemNo = hashMap.get("SystemNo");
                    String beginTime = hashMap.get("InFactoryTime");
                    String endTime = hashMap.get("SignInTime");
                    int result = Integer.valueOf(hashMap.get("result"));
                    if (result == 0) {
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            format.setLenient(false);
                            format.parse(beginTime);
                            format.parse(endTime);
                            json = new GpsWebService().getGpsWebServiceSoap().getHistory(systemNo, beginTime, endTime);
                            if (null != json && json.length() > 0) {
                                json = Units.jsonStrToJson(0, "", json);
                            } else {
                                json = Units.objectToJson(-1, "该时间段数据为空", null);
                            }
                        } catch (Exception e) {
                            json = Units.objectToJson(-1, "日期格式不对", e);
                        }
                    } else if (result == 1) {
                        json = Units.objectToJson(-1, "车辆未签收或车辆状态错误!", null);
                    } else {
                        json = Units.objectToJson(-1, "服务器出现异常, 请联系管理员!", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getCarHistoryWithTime(获取轨迹回放数据)">
                case "getCarHistoryWithTime": {
//                int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "-1");
//                int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "-1");
//                OrderCarStatusController controller = new OrderCarStatusController();
//                HashMap<String, String> hashMap = controller.getHistoryTime(carID, taskID);
                    String systemNo = paramsJson.getString("systemNo");
                    String beginTime = paramsJson.getString("beginTime");
                    String endTime = paramsJson.getString("endTime");

                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        format.setLenient(false);
                        format.parse(beginTime);
                        format.parse(endTime);
                        json = new GpsWebService().getGpsWebServiceSoap().getHistory(systemNo, beginTime, endTime);
                        if (null != json && json.length() > 0) {
                            json = Units.jsonStrToJson(0, "", json);
                        } else {
                            json = Units.objectToJson(-1, "该时间段数据为空", null);
                        }
                    } catch (Exception e) {
                        json = Units.objectToJson(-1, "日期格式不对", e);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="updateDistributor(更新经销商信息)">
                case "updateDistributor": {
                    int distributorID = Integer.valueOf((null != paramsJson.getString("distributorID")) ? paramsJson.getString("distributorID") : "-1");
                    //String username = paramsJson.getString("username");
                    String password = paramsJson.getString("password");
                    String name = paramsJson.getString("name");
                    String phoneNumber = paramsJson.getString("phoneNumber");
                    String address = paramsJson.getString("address");
                    String company = paramsJson.getString("company");
                    DistributorController controller = new DistributorController();
                    int result = controller.distributorUpdate(distributorID, password, name, phoneNumber, address, company);
                    String message;
                    if (result == 0) {
                        message = "更新成功!";
                    } else if (result == -1) {
                        message = "更新失败!";
                    } else {
                        message = "更新出错!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="authDistributor(经销商认证)">
                case "authDistributor": {
                    int distributorID = Integer.valueOf((null != paramsJson.getString("distributorID")) ? paramsJson.getString("distributorID") : "-1");
                    String phoneNumber = paramsJson.getString("phoneNumber");
                    String address = paramsJson.getString("address");
                    String company = paramsJson.getString("company");
                    DistributorController controller = new DistributorController();
                    int result = controller.distributorUpdate(distributorID, null, null, phoneNumber, address, company);
                    String message;
                    if (result == 0) {
                        message = "更新成功!";
                    } else if (result == -1) {
                        message = "更新失败!";
                    } else {
                        message = "更新出错!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>

                /**
                 * *******************************公告相关************************************
                 */
                //<editor-fold defaultstate="collapsed" desc="getNoticeList">
                case "getNoticeList": {
                    int noticeID = Integer.valueOf((null != paramsJson.getString("noticeID")) ? paramsJson.getString("noticeID") : "-1");
                    String startTime = paramsJson.getString("startTime");
                    String endTime = paramsJson.getString("endTime");
                    int nowPage = Integer.valueOf((null != paramsJson.getString("nowPage")) ? paramsJson.getString("nowPage") : "0");
                    int pageSize = Integer.valueOf((null != paramsJson.getString("pageSize")) ? paramsJson.getString("pageSize") : "0");
                    NoticeController controller = new NoticeController();
                    ArrayList<Notice> result = controller.getNoticeList(noticeID, startTime, endTime, nowPage, pageSize);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "记录为空!", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getNoticeDetail">
                case "getNoticeDetail": {
                    int noticeID = Integer.valueOf((null != paramsJson.getString("noticeID")) ? paramsJson.getString("noticeID") : "-2");
                    NoticeController controller = new NoticeController();
                    Notice notice = controller.getNoticeDetail(noticeID);
                    if (null != notice) {
                        json = Units.objectToJson(0, "", notice);
                    } else {
                        json = Units.objectToJson(-1, "ID不存在", null);
                    }
                    break;
                }
                //</editor-fold>

                /**
                 * *******************************地点************************************
                 */
                //<editor-fold defaultstate="collapsed" desc="addSite">
                case "addSite": {
                    float siteLat = Float.valueOf((null != paramsJson.getString("siteLon")) ? paramsJson.getString("siteLon") : "-1");
                    float siteLon = Float.valueOf((null != paramsJson.getString("siteLat")) ? paramsJson.getString("siteLat") : "-1");
                    String siteName = paramsJson.getString("siteName");
                    int siteType = Integer.valueOf((null != paramsJson.getString("siteType")) ? paramsJson.getString("siteType") : "-1");
                    SiteTableController controller = new SiteTableController();
                    SiteTableController.SiteAddResult result = controller.siteAdd(siteLat, siteLon, siteName, siteType);
                    String message;
                    if (result.getResult() == 0) {
                        message = "添加成功!";
                    } else if (result.getResult() == -1) {
                        message = "添加失败!";
                    } else {
                        message = "添加出错!";
                    }
                    json = Units.jsonStrToJson(result.getResult(), message, "{\"siteID\":" + result.getSite().getSiteID() + "}");
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getSite">
                case "getSite": {
                    String siteName = paramsJson.getString("siteName");
                    int siteType = Integer.valueOf((null != paramsJson.getString("siteType")) ? paramsJson.getString("siteType") : "-1");
                    int nowPage = Integer.valueOf((null != paramsJson.getString("nowPage")) ? paramsJson.getString("nowPage") : "0");
                    int pageSize = Integer.valueOf((null != paramsJson.getString("pageSize")) ? paramsJson.getString("pageSize") : "0");
                    SiteTableController controller = new SiteTableController();
                    ArrayList<SiteTalble> result = controller.siteGet(siteName, siteType, nowPage, pageSize);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "记录为空!", null);
                    }
                    break;
                }
            //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="getNewestVersion">
                case "getNewestVersion": {
                    int updateType = Integer.valueOf((null != paramsJson.getString("updateType")) ? paramsJson.getString("updateType") : "-1");
                    VersionController controller = new VersionController();
                    Version v = controller.getNewestVersion(updateType);
                    if (null != v) {
                        json = Units.objectToJson(0, "", v);
                    } else {
                        json = Units.objectToJson(-1, "服务器无版本更新", null);
                    }
                    break;
                }
                //</editor-fold>

                default:
                    break;
            }
        } catch (Exception e) {
            LOG.error(subUri + "输入参数错误", e);
            json = Units.objectToJson(-1, "输入参数错误!", e.toString());
        }

//        System.out.println(response.toString());
        PrintWriter out = response.getWriter();
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            if (DESFlag) {
                out.print(EncryptUtil.encryptDES(json));
            } else {
                out.print(json);
            }
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 判断已登录用户列表中(loginUserList), 是否存在指定的用户 (用户类型和用户ID相同的用户)
     *
     * @param user
     * @return 如果存在, 返回对应的用户登录信息; 如果不存在, 返回null
     */
    private LoginUser isHasUser(HashMap<String, LoginUser> loginUserList, LoginUser user) {
        Iterator<Map.Entry<String, LoginUser>> iterable = loginUserList.entrySet().iterator();
        while (iterable.hasNext()) {
            Map.Entry<String, LoginUser> entry = iterable.next();
            LoginUser tmp = entry.getValue();
            if (tmp.getUserType() == user.getUserType()
                    && tmp.getUserID() == user.getUserID()) {
                return tmp;
            }
        }
        return null;
    }

    private boolean isCanAccess(String hostIP, String interfaceName) {

        return false;
    }

    private AccessFrequency isHasHostIP(String hostIP, List<AccessFrequency> desList) {
        for (AccessFrequency frequency : desList) {
//            System.out.println("ip:" + frequency.getIpAddress() + ",count:" + frequency.getOverLimitcount() + ",time:" + frequency.getAccessTime());
            if (frequency.getIpAddress().compareTo(hostIP) == 0) {
                return frequency;
            }
        }
        return null;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String params = request.getParameter("params");
        processRequest(request, response, params);

        /*
         String uri = request.getRequestURI();
         String subUri = uri.substring(uri.lastIndexOf("/") + 1,
         uri.lastIndexOf("."));
         String params = request.getParameter("params");
         try {
         if (subUri.equalsIgnoreCase("grabOrder")) {
         queue.put(new RequestQueueItem(request, response, params));
         while (!queue.isEmpty()) {
         RequestQueueItem item = (RequestQueueItem) queue.poll();
         processRequest(item.getRequest(), item.getResponse(), item.getParamsJson());
         }
         } else {
         processRequest(request, response, params);
         }
         } catch (InterruptedException ex) {
         LOG.info("order detail", ex);
         }
         */
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String params = getPostParameter(request);
        processRequest(request, response, params);
        /*
         try {
         if (subUri.equalsIgnoreCase("grabOrder")) {
         queue.put(new RequestQueueItem(request, response, params));

         while (!queue.isEmpty()) {
         RequestQueueItem item = (RequestQueueItem) queue.poll();
         processRequest(item.getRequest(), item.getResponse(), item.getParamsJson());
         }
         } else {
         processRequest(request, response, params);
         }
         } catch (InterruptedException ex) {
         LOG.info("order detail", ex);
         }
         */
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

    /**
     * 根据request获取Post参数
     *
     * @param request
     * @return
     * @throws IOException
     */
    private String getPostParameter(HttpServletRequest request) throws IOException {
        BufferedInputStream buf = null;
        int iContentLen = request.getContentLength();
        if (iContentLen > 0) {
            byte sContent[] = new byte[iContentLen];
            String sContent2 = null;
            try {
                buf = new BufferedInputStream(request.getInputStream());
                buf.read(sContent, 0, sContent.length);
                sContent2 = new String(sContent, 0, iContentLen, "UTF-8");
            } catch (IOException e) {
                throw new IOException("Parse data error!", e);
            } finally {
                if (null != buf) {
                    buf.close();
                }
            }
            return sContent2;
        }
        return null;
    }// </editor-fold>
}
