/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.Web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.controller.CarTableController;
import com.cn.controller.DestinationDisController;
import com.cn.controller.DistributorController;
import com.cn.controller.DriverController;
import com.cn.controller.EnterpriseController;
import com.cn.controller.GateSentryController;
import com.cn.controller.GoodsInfoController;
import com.cn.controller.NoticeController;
import com.cn.controller.OrderGrabController;
import com.cn.controller.OrderTableController;
import com.cn.controller.OrderTableController.OrderAddResult;
import com.cn.controller.RouteTableController;
import com.cn.controller.SiteTableController;
import com.cn.controller.TransportController;
import com.cn.entity.CarTable;
import com.cn.entity.DestinationDis;
import com.cn.entity.Distributor;
import com.cn.entity.Driver;
import com.cn.entity.Enterprise;
import com.cn.entity.GateSentry;
import com.cn.entity.GoodsInfo;
import com.cn.entity.GoodsType;
import com.cn.entity.Notice;
import com.cn.entity.OrderGrab;
import com.cn.entity.OrderGrabDetail;
import com.cn.entity.OrderTable;
import com.cn.entity.SiteTalble;
import com.cn.entity.Transport;
import com.cn.task.ScanTimeoutInFactory;
import com.cn.task.SendDownOrder;
import com.cn.util.CarNumberVerifie;
import com.cn.util.Constants;
import com.cn.util.Units;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class PCOperateServlet extends HttpServlet {

    protected static final org.slf4j.Logger LOG = LoggerFactory.getLogger(PCOperateServlet.class);
    private static final ScheduledExecutorService timeOutScheduler = Executors.newSingleThreadScheduledExecutor();
    private static final ScheduledExecutorService sendOrderscheduler = Executors.newScheduledThreadPool(7);
    private HashMap<String, ScheduledFuture> futures;

    @Override
    public void init() throws ServletException {
        super.init(); //To change body of generated methods, choose Tools | Templates.
        futures = new HashMap<>();
        timeOutScheduler.scheduleWithFixedDelay(new ScanTimeoutInFactory(), 10, 60, TimeUnit.SECONDS);
    }

    @Override
    public void destroy() {
        super.destroy(); //To change body of generated methods, choose Tools | Templates.
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

        String json = "";
        HttpSession session = request.getSession();

        /*验证是否登陆*/
        if (!"loginTransport".equals(subUri) && session.getAttribute("user") == null) {
            session.invalidate();
            json = Units.objectToJson(-1, "未登陆", null);
            PrintWriter out = response.getWriter();
            try {
                response.setContentType("text/html;charset=UTF-8");
                response.setHeader("Cache-Control", "no-store");
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);
                out.print(json);
            } finally {
                out.close();
            }
            return;
        }

//        System.out.println(subUri + ", params is:" + params);
        //将字符串转换为json
        JSONObject paramsJson = JSONObject.parseObject(params);

        try {
            switch (subUri) {
                case "test": {
                    Date date = new Date();
                    json = JSONObject.toJSONString(date);
                    break;
                }

                /**
                 * *******************************运输处**********************************
                 */
                //<editor-fold defaultstate="collapsed" desc="addTransport">
                case "addTransport": {
                    String username = paramsJson.getString("username");
                    String password = paramsJson.getString("password");
                    String name = paramsJson.getString("name");
                    String phoneNumber = paramsJson.getString("phoneNumber");
                    TransportController controller = new TransportController();
                    int result = controller.transportAdd(username, password, name, phoneNumber);
                    if (result == 0) {
                        json = Units.objectToJson(result, "添加成功", null);
                    } else if (result == -1) {
                        json = Units.objectToJson(result, "添加失败", null);
                    } else if (result > 0) {
                        json = Units.objectToJson(result, "用户名已存在", null);
                    } else {
                        json = Units.objectToJson(result, "", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="updateTransport">
                case "updateTransport": {
                    int transportID = Integer.valueOf((null != paramsJson.getString("transportID")) ? paramsJson.getString("transportID") : "-1");
                    String username = paramsJson.getString("username");
                    String password = paramsJson.getString("password");
                    String name = paramsJson.getString("name");
                    String phoneNumber = paramsJson.getString("phoneNumber");
                    TransportController controller = new TransportController();
                    int result = controller.transportUpdate(transportID, username, password, name, phoneNumber);
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
                //<editor-fold defaultstate="collapsed" desc="deleteTransport">
                case "deleteTransport": {
                    int transportID = Integer.valueOf((null != paramsJson.getString("transportID")) ? paramsJson.getString("transportID") : "-1");
                    TransportController controller = new TransportController();
                    int result = controller.transportDelete(transportID);
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
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="loginTransport">
                case "loginTransport": {
                    String username = paramsJson.getString("username");
                    String password = paramsJson.getString("password");
                    TransportController controller = new TransportController();
                    int result = controller.transportLogin(username, password);
                    if (result == -1) {
                        json = Units.objectToJson(result, "登录出错", null);
                    } else if (result == 0) {
                        Transport transport = controller.transportGetWithUserName(username);
                        json = Units.objectToJson(0, "登录成功", transport);
                        session.setAttribute("user", transport);
                    } else if (result == 1) {
                        json = Units.objectToJson(1, "用户名或密码错误, 登录失败", null);
                    } else {
                        json = Units.objectToJson(result, "未知错误", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getTransport">
                case "getTransport": {
                    int transportID = Integer.valueOf((null != paramsJson.getString("transportID")) ? paramsJson.getString("transportID") : "-1");
                    String transportUserName = paramsJson.getString("username");
                    String transportName = paramsJson.getString("name");
                    int nowPage = Integer.valueOf((null != paramsJson.getString("nowPage")) ? paramsJson.getString("nowPage") : "0");
                    int pageSize = Integer.valueOf((null != paramsJson.getString("pageSize")) ? paramsJson.getString("pageSize") : "0");
                    TransportController controller = new TransportController();
                    ArrayList<Transport> result = controller.transportGet(transportID, transportName, transportUserName, nowPage, pageSize);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "所得记录为空!", null);
                    }
                    break;
                }
                //</editor-fold>

                /**
                 * *******************************经销商**********************************
                 */
                //<editor-fold defaultstate="collapsed" desc="addDistributor">
                case "addDistributor": {
                    String username = paramsJson.getString("username");
                    String password = paramsJson.getString("password");
                    String name = paramsJson.getString("name");
                    String phoneNumber = paramsJson.getString("phoneNumber");
                    String address = paramsJson.getString("address");
                    String company = paramsJson.getString("company");
                    DistributorController controller = new DistributorController();
                    int result = controller.distributorAdd(username, password, name, phoneNumber, address, company);
                    String message;
                    if (result == 0) {
                        message = "添加成功!";
                    } else if (result == -1) {
                        message = "添加失败!";
                    } else if (result == 1) {
                        message = "用户名已存在!";
                    } else {
                        message = "添加出错";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="updateDistributor">
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
                //<editor-fold defaultstate="collapsed" desc="deleteDistributor">
                case "deleteDistributor": {
                    int distributorID = Integer.valueOf((null != paramsJson.getString("distributorID")) ? paramsJson.getString("distributorID") : "-1");
                    DistributorController controller = new DistributorController();
                    int result = controller.distributorDelete(distributorID);
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
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getDistributor">
                case "getDistributor": {
                    int distributorID = Integer.valueOf((null != paramsJson.getString("distributorID")) ? paramsJson.getString("distributorID") : "-1");
                    String distributorName = paramsJson.getString("name");
                    String distributorUserName = paramsJson.getString("username");
                    int nowPage = Integer.valueOf((null != paramsJson.getString("nowPage")) ? paramsJson.getString("nowPage") : "0");
                    int pageSize = Integer.valueOf((null != paramsJson.getString("pageSize")) ? paramsJson.getString("pageSize") : "0");
                    DistributorController controller = new DistributorController();
                    ArrayList<Distributor> result = controller.distributorGet(distributorID, distributorName, distributorUserName, nowPage, pageSize);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "所得记录为空!", null);
                    }
                    break;
                }
                //</editor-fold>

                /**
                 * *******************************企业或个体************************************
                 */
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
                    } else {
                        message = "删除出错!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getApplyDeleteDriver">
                case "getApplyDeleteDriver": {
                    int nowPage = Integer.valueOf((null != paramsJson.getString("nowPage")) ? paramsJson.getString("nowPage") : "0");
                    int pageSize = Integer.valueOf((null != paramsJson.getString("pageSize")) ? paramsJson.getString("pageSize") : "0");
                    DriverController controller = new DriverController();
                    ArrayList<Driver> result = controller.driverGetApplyDelete(1, nowPage, pageSize);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "记录为空", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getApplyDeleteCar">
                case "getApplyDeleteCar": {
                    int nowPage = Integer.valueOf((null != paramsJson.getString("nowPage")) ? paramsJson.getString("nowPage") : "0");
                    int pageSize = Integer.valueOf((null != paramsJson.getString("pageSize")) ? paramsJson.getString("pageSize") : "0");
                    CarTableController controller = new CarTableController();
                    ArrayList<CarTable> result = controller.getApplyDeleteCarList(1, nowPage, pageSize);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "记录为空", null);
                    }
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

                /**
                 * *******************************地点************************************
                 */
                //<editor-fold defaultstate="collapsed" desc="addRoute">
                case "addRoute": {
                    float startLat = Float.valueOf((null != paramsJson.getString("startLat")) ? paramsJson.getString("startLat") : "-1");
                    float startLon = Float.valueOf((null != paramsJson.getString("startLon")) ? paramsJson.getString("startLon") : "-1");
                    String startName = paramsJson.getString("startName");
                    float endLat = Float.valueOf((null != paramsJson.getString("endLat")) ? paramsJson.getString("endLat") : "-1");
                    float endLon = Float.valueOf((null != paramsJson.getString("endLon")) ? paramsJson.getString("endLon") : "-1");
                    String endName = paramsJson.getString("endName");
                    float length = Float.valueOf((null != paramsJson.getString("length")) ? paramsJson.getString("length") : "-1");
                    RouteTableController controller = new RouteTableController();
                    int result = controller.routeAdd(startLat, startLon, startName, endLat, endLon, endName, length);
                    String message;
                    if (result == 0) {
                        message = "添加成功!";
                    } else if (result == -1) {
                        message = "添加失败!";
                    } else {
                        message = "添加出错!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getRoute">
                case "getRoute": {
                    break;
                }
                //</editor-fold>
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
//                    json = Units.objectToJson(result.getResult(), message, result.getSite());
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

                /**
                 * *******************************订单************************************
                 */
                //<editor-fold defaultstate="collapsed" desc="addOrder">
                case "addOrder": {
                    int isMixed = Integer.valueOf((null != paramsJson.getString("isMixed")) ? paramsJson.getString("isMixed") : "0");
                    String goodsType = paramsJson.getString("goodsType");
                    ArrayList<GoodsType> types = (ArrayList<GoodsType>) JSONArray.parseArray(goodsType, GoodsType.class);
                    int priceModel = Integer.valueOf((null != paramsJson.getString("priceModel")) ? paramsJson.getString("priceModel") : "-1");
                    float price = Float.valueOf((null != paramsJson.getString("price")) ? paramsJson.getString("price") : "-1");
                    int taxRate = Integer.valueOf((null != paramsJson.getString("taxRate")) ? paramsJson.getString("taxRate") : "0");//税率,默认不含税
                    int pickupType = Integer.valueOf((null != paramsJson.getString("pickupType")) ? paramsJson.getString("pickupType") : "0");//提货方式,默认代付
                    int timeCountDown = Integer.valueOf((null != paramsJson.getString("timeCountDown")) ? paramsJson.getString("timeCountDown") : "-1");
//                    String taskEndTime = paramsJson.getString("taskEndTime");
                    int taskType = Integer.valueOf((null != paramsJson.getString("taskType")) ? paramsJson.getString("taskType") : "-1");
                    int startPlace = Integer.valueOf((null != paramsJson.getString("startPlace")) ? paramsJson.getString("startPlace") : "-1");
                    int endPlace = Integer.valueOf((null != paramsJson.getString("endPlace")) ? paramsJson.getString("endPlace") : "-1");
                    float carRange = Float.valueOf((null != paramsJson.getString("carRange")) ? paramsJson.getString("carRange") : "-1");
                    float tonnage = Float.valueOf((null != paramsJson.getString("tonnage")) ? paramsJson.getString("tonnage") : "-1");
                    int carNum = Integer.valueOf((null != paramsJson.getString("carNum")) ? paramsJson.getString("carNum") : "-1");
                    String selectedCar = paramsJson.getString("selectedCar");
                    int enterpriseID = Integer.valueOf((null != paramsJson.getString("enterpriseID")) ? paramsJson.getString("enterpriseID") : "-1");
                    String carType = paramsJson.getString("carType");
                    float carLength = Float.valueOf((null != paramsJson.getString("carLength") && paramsJson.getString("carLength").equalsIgnoreCase("不限长")) ? "0" : paramsJson.getString("carLength"));
                    float length = Float.valueOf((null != paramsJson.getString("length")) ? paramsJson.getString("length") : "-1");
                    int factoryTime = Integer.valueOf((null != paramsJson.getString("factoryTime")) ? paramsJson.getString("factoryTime") : "-1");
                    int transportID = Integer.valueOf((null != paramsJson.getString("transportID")) ? paramsJson.getString("transportID") : "-1");
                    int distributorID = Integer.valueOf((null != paramsJson.getString("distributorID")) ? paramsJson.getString("distributorID") : "-1");
                    String destinationDis = paramsJson.getString("destinationDis");
                    String distributorContact = paramsJson.getString("distributorContact");
                    String distributorPhoneNum = paramsJson.getString("distributorPhoneNum");

                    OrderTableController controller = new OrderTableController();
                    OrderAddResult result = controller.taskAdd(null, isMixed, priceModel, price, taxRate, pickupType, timeCountDown, taskType, startPlace, endPlace,
                            carRange, tonnage, carNum, selectedCar, enterpriseID, carType, carLength, length, factoryTime, transportID,
                            distributorID, destinationDis, distributorContact, distributorPhoneNum);
                    String message;
                    if (null != result) {
                        if (result.getResult() == 0) {
                            result.setResult(controller.taskAddGoodsDetail(result.getTaskSerial(), types));
                            if (result.getResult() == 0) {
                                message = "订单添加成功!";
                            } else {
                                message = "货物明细添加失败!";
                            }
                            json = Units.objectToJson(result.getResult(), message, result.getTaskSerial());
                        } else if (result.getResult() == -1) {
                            message = "订单添加失败!";
                            json = Units.objectToJson(result.getResult(), message, null);
                        } else {
                            message = "订单添加出错!";
                            json = Units.objectToJson(result.getResult(), message, null);
                        }
                    } else {
                        json = Units.objectToJson(-1, "订单添加异常!", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getOrderList">
                case "getOrderList": {
                    int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "-1");
                    int listType = Integer.valueOf((null != paramsJson.getString("listType")) ? paramsJson.getString("listType") : "-1");
                    int startPlace = Integer.valueOf((null != paramsJson.getString("startPlace")) ? paramsJson.getString("startPlace") : "-1");
                    int endPlace = Integer.valueOf((null != paramsJson.getString("endPlace")) ? paramsJson.getString("endPlace") : "-1");
//                String startPlace = paramsJson.getString("startPlace");
//                String endPlace = paramsJson.getString("endPlace");
                    String taskSerial = paramsJson.getString("taskSerial");
                    String startTime = paramsJson.getString("startTime");
                    String endTime = paramsJson.getString("endTime");
                    String carNumber = paramsJson.getString("carNumber");
                    String destinationDis = paramsJson.getString("destinationDis");
                    String distributorName = paramsJson.getString("distributorName");
                    int nowPage = Integer.valueOf((null != paramsJson.getString("nowPage")) ? paramsJson.getString("nowPage") : "0");
                    int pageSize = Integer.valueOf((null != paramsJson.getString("pageSize")) ? paramsJson.getString("pageSize") : "0");
                    OrderTableController controller = new OrderTableController();
                    ArrayList<OrderTable> result = controller.getTaskList(taskID, taskSerial, listType, startPlace, endPlace, -1, distributorName, -1, startTime, endTime, carNumber, destinationDis, nowPage, pageSize);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "订单记录为空", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="sendOrder">
                case "sendOrder": {
                    String taskSerial = paramsJson.getString("taskSerial");
                    OrderTableController controller = new OrderTableController();

                    OrderTable order = controller.getTaskDetailWithSerial(taskSerial, -1);
                    if (null != order) {
                        int sendRes = controller.taskSend(taskSerial);//发送状态写入数据库
                        String message;
                        if (sendRes == 0) {
                            ScheduledFuture future = sendOrderscheduler.scheduleWithFixedDelay(new SendDownOrder(taskSerial, this), 0, order.getTimeCountDown(), TimeUnit.MINUTES);
                            if (null == futures) {
                                futures = new HashMap<>();
                            }
                            futures.put(taskSerial, future);
                            message = "订单下发成功!";
                        } else if (sendRes == 1) {
                            message = "订单已经下发!";
                        } else {
                            message = "订单下发失败!";
                        }
                        json = Units.objectToJson(sendRes, message, null);
                    } else {
                        json = Units.objectToJson(-1, "该订单编号不存在!", null);
                    }

                    break;
                }
            //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="addAndSendOrder">
            /*
                 case "addAndSendOrder": {
                 String goodsType = paramsJson.getString("goodsType");
                 int priceModel = Integer.valueOf((null != paramsJson.getString("priceModel")) ? paramsJson.getString("priceModel") : "-1");
                 float price = Float.valueOf((null != paramsJson.getString("price")) ? paramsJson.getString("price") : "-1");
                 int timeCountDown = Integer.valueOf((null != paramsJson.getString("timeCountDown")) ? paramsJson.getString("timeCountDown") : "-1");
                 String taskEndTime = paramsJson.getString("taskEndTime");
                 int taskType = Integer.valueOf((null != paramsJson.getString("taskType")) ? paramsJson.getString("taskType") : "-1");
                 String startPlace = paramsJson.getString("startPlace");
                 String endPlace = paramsJson.getString("endPlace");
                 float carRange = Float.valueOf((null != paramsJson.getString("carRange")) ? paramsJson.getString("carRange") : "-1");
                 float tonnage = Float.valueOf((null != paramsJson.getString("tonnage")) ? paramsJson.getString("tonnage") : "-1");
                 int carNum = Integer.valueOf((null != paramsJson.getString("carNum")) ? paramsJson.getString("carNum") : "-1");
                 String selectedCar = paramsJson.getString("selectedCar");
                 int enterpriseID = Integer.valueOf((null != paramsJson.getString("enterpriseID")) ? paramsJson.getString("enterpriseID") : "-1");
                 String carType = paramsJson.getString("carType");
                 int length = Integer.valueOf((null != paramsJson.getString("length")) ? paramsJson.getString("length") : "-1");
                 String factoryTime = paramsJson.getString("factoryTime");
                 int transportID = Integer.valueOf((null != paramsJson.getString("transportID")) ? paramsJson.getString("transportID") : "-1");
                 int distributorID = Integer.valueOf((null != paramsJson.getString("distributorID")) ? paramsJson.getString("distributorID") : "-1");
                 OrderTableController controller = new OrderTableController();
                 OrderAddResult result = controller.taskAdd(goodsType, priceModel, price, timeCountDown, taskEndTime, taskType, startPlace, endPlace, carRange, tonnage, carNum, selectedCar, enterpriseID, carType, length, factoryTime, transportID, distributorID);
                 String message;
                 if (result.getResult() == 0) {
                 int sendRes = controller.taskSend(result.getTaskSerial());
                 if (sendRes == 0) {
                 message = "订单下发成功!";
                 } else if (sendRes == 1) {
                 message = "订单已经下发!";
                 } else {
                 message = "订单下发失败!";
                 }
                 json = Units.objectToJson(sendRes, message, null);
                 } else if (result.getResult() == -1) {
                 message = "订单添加失败!";
                 json = Units.objectToJson(result.getResult(), message, null);
                 } else {
                 message = "订单添加出错!";
                 json = Units.objectToJson(result.getResult(), message, null);
                 }
                 break;
                 }
                 */
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="orderDetail">
                case "orderDetail": {
                    String taskSerial = paramsJson.getString("taskSerial");
                    OrderTableController controller = new OrderTableController();
                    OrderTable result = controller.getTaskDetailWithSerial(taskSerial, -1);
                    if (null != result) {
                        json = Units.objectToJson(0, "", result);
                    } else {
                        json = Units.objectToJson(1, "该订单编号不存在!", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="orderVerify(订单校验)">
                case "orderVerify": {
                    int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "-1");
                    String goodsType = paramsJson.getString("goodsType");
                    ArrayList<GoodsType> types = (ArrayList<GoodsType>) JSONArray.parseArray(goodsType, GoodsType.class);
                    OrderTableController controller = new OrderTableController();
                    int result = controller.taskUpdateGoodsDetail(taskID, types);
                    String message;
                    if (result == 0) {
                        message = "订单校验成功!";
                    } else if (result == -1) {
                        message = "服务器异常!";
                    } else {
                        message = "订单校验失败!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="orderRevoke(撤销订单)">
                case "orderRevoke": {
                    int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "-1");
                    OrderTableController controller = new OrderTableController();
                    OrderAddResult result = controller.taskRevoke(taskID);
                    String message;
                    if (null != result) {
                        if (result.getResult() == 0) {
                            message = "订单撤销成功!";
                            shutdownTimerTask(result.getTaskSerial());
                        } else if (result.getResult() == -1) {
                            message = "服务器异常!";
                        } else {
                            message = "订单撤销失败!";
                        }
                        json = Units.objectToJson(result.getResult(), message, null);
                    } else {
                        json = Units.objectToJson(-1, "订单添加异常!", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="orderReassignment(改派订单)">
                case "orderReassignment": {
                    int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "-1");
                    int distributorID = Integer.valueOf((null != paramsJson.getString("distributorID")) ? paramsJson.getString("distributorID") : "-1");
                    int endPlace = Integer.valueOf((null != paramsJson.getString("endPlace")) ? paramsJson.getString("endPlace") : "-1");
                    String destinationDis = paramsJson.getString("destinationDis");
                    String distributorContact = paramsJson.getString("distributorContact");
                    String distributorPhoneNumber = paramsJson.getString("distributorPhoneNumber");
                    OrderTableController controller = new OrderTableController();
                    int result = controller.taskReassignment(taskID, -1, endPlace, distributorID, destinationDis, distributorContact, distributorPhoneNumber);
                    String message;
                    if (result == 0) {
                        message = "订单改派成功!";
                    } else if (result == -1) {
                        message = "服务器异常!";
                    } else {
                        message = "订单改派失败!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getAllCars">
                case "getAllCars": {
                    CarTableController controller = new CarTableController();
                    ArrayList<CarTable> result = controller.getCarList(-1, -1, null, null, null, -1, 1, Constants.MAX_RECORD);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "记录为空!", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getAllEnterprise">
                case "getAllEnterprise": {
                    EnterpriseController controller = new EnterpriseController();
                    ArrayList<Enterprise> result = controller.enterpriseGet(-1, null, 1, Constants.MAX_RECORD);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "记录为空!", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getEnterpriseCars">
                case "getEnterpriseCars": {
                    int userID = Integer.valueOf((null != paramsJson.getString("enterpriseID")) ? paramsJson.getString("enterpriseID") : "-1");
                    CarTableController controller = new CarTableController();
                    ArrayList<CarTable> result = controller.getCarListWithUserID(userID, 1, Constants.MAX_RECORD);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "记录为空", null);
                    }
                    break;
                }
                //</editor-fold>

                /**
                 * *******************************物料************************************
                 */
                //<editor-fold defaultstate="collapsed" desc="getGoodsList">
                case "getGoodsList": {
                    String goodsId = paramsJson.getString("goodsId");
                    String goodsName = paramsJson.getString("goodsName");
                    int nowPage = Integer.valueOf((null != paramsJson.getString("nowPage")) ? paramsJson.getString("nowPage") : "0");
                    int pageSize = Integer.valueOf((null != paramsJson.getString("pageSize")) ? paramsJson.getString("pageSize") : "0");
                    GoodsInfoController controller = new GoodsInfoController();
                    ArrayList<GoodsInfo> result = controller.goodsInfoGet(goodsId, goodsName, nowPage, pageSize);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "记录为空", null);
                    }
                    break;
                }
                //</editor-fold>

                /**
                 * *******************************目的地描述************************************
                 */
                //<editor-fold defaultstate="collapsed" desc="getDestinationDis">
                case "getDestinationDis": {
                    int disID = Integer.valueOf((null != paramsJson.getString("disID")) ? paramsJson.getString("disID") : "-1");
                    String disSerial = paramsJson.getString("disSerial");
                    String disName = paramsJson.getString("disName");
                    int nowPage = Integer.valueOf((null != paramsJson.getString("nowPage")) ? paramsJson.getString("nowPage") : "0");
                    int pageSize = Integer.valueOf((null != paramsJson.getString("pageSize")) ? paramsJson.getString("pageSize") : "0");
                    DestinationDisController controller = new DestinationDisController();
                    ArrayList<DestinationDis> result = controller.destinationDisGet(disID, disSerial, disName, nowPage, pageSize);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "记录为空", null);
                    }
                    break;
                }
                //</editor-fold>

                /**
                 * *******************************返回门岗列表************************************
                 */
                //<editor-fold defaultstate="collapsed" desc="getDestinationDis">
                case "getGateSentry": {
                    GateSentryController controller = new GateSentryController();
                    ArrayList<GateSentry> result = controller.getGateSentryList();
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "记录为空", null);
                    }
                    break;
                }
                //</editor-fold>

                /**
                 * *******************************抢单************************************
                 */
                // <editor-fold defaultstate="collapsed" desc="getGrabedOrder(抢单记录)">
                case "getGrabedOrder": {
                    int userID = Integer.valueOf((null != paramsJson.getString("userID")) ? paramsJson.getString("userID") : "-1");
                    int driverID = Integer.valueOf((null != paramsJson.getString("driverID")) ? paramsJson.getString("driverID") : "-1");
                    int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "-1");
                    int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "-1");
                    String carNumber = paramsJson.getString("carNumber");
                    String taskSerial = paramsJson.getString("taskSerial");
                    String startTime = paramsJson.getString("startTime");
                    String endTime = paramsJson.getString("endTime");
                    String distributorName = paramsJson.getString("distributorName");
                    int nowPage = Integer.valueOf((null != paramsJson.getString("nowPage")) ? paramsJson.getString("nowPage") : "0");
                    int pageSize = Integer.valueOf((null != paramsJson.getString("pageSize")) ? paramsJson.getString("pageSize") : "0");
//                    System.out.println("userID:" + userID + ",driverID:" + driverID + ",taskID:" + taskID + ",carID:" + carID + ",carNumber:" + carNumber + ",taskSerial:" + taskSerial + ",nowPage:" + nowPage + ",pageSize:" + pageSize);
                    OrderGrabController controller = new OrderGrabController();
                    ArrayList<OrderGrab> result = controller.getOrderGrabList(userID, driverID, carID, carNumber, -1, -1, -1, taskID, taskSerial, distributorName, startTime, endTime, nowPage, pageSize);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "订单记录为空", null);
                    }
                    break;
                }
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="getCarStatus(获取抢单车辆状态)">
                case "getCarStatus": {
                    int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "-1");
                    String taskSerial = paramsJson.getString("taskSerial");
                    OrderGrabController controller = new OrderGrabController();
//                CarTableController carController = new CarTableController();
                    ArrayList<OrderGrab> result = controller.getOrderGrabList(-1, -1, -1, null, -1, -1, -1, taskID, taskSerial, null, null, null, 1, Constants.MAX_RECORD);
                    for (OrderGrab grab : result) {
                        if (grab.getGrabStatus() == 1 && grab.getInFactoryStatus() == 2) {
                            //CarLocation location = carController.getCarLocationWithSystemNo(grab.getSystemNo());
//                        System.out.println("start:" + grab.getStartPlaceLatitude() + "," + grab.getStartPlaceLongitude() + ",Cur:" + grab.getCurLatitude() + "," + grab.getCurLongitude());
                            if (grab.getStartPlaceLatitude() < 0.1 || grab.getStartPlaceLongitude() < 0.1 || grab.getCurLatitude() < 0.1 || grab.getCurLongitude() < 0.1) {
                                grab.setMileageScale(0);
                            } else {
                                double arrived = Units.GetDistance(grab.getStartPlaceLongitude(), grab.getStartPlaceLatitude(), grab.getCurLongitude(), grab.getCurLatitude());
                                grab.setMileageScale((float) ((arrived / 1000) / grab.getTLenth()));
//                            System.out.println("arrived:" + arrived + ",meleageScale:" + grab.getMileageScale());
                            }
                        }
                    }

                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "订单记录为空", null);
                    }
                    break;
                }
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="printGrabOrder(打印)">
                case "printGrabOrder": {
//                int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "0");
                    int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "-1");
                    String taskSerial = paramsJson.getString("taskSerial");
                    OrderGrabController controller = new OrderGrabController();
                    OrderGrabDetail detail = controller.printOrderGrab(taskSerial, carID);
                    if (null != detail) {
                        json = Units.objectToJson(0, "", detail);
                    } else {
                        json = Units.objectToJson(1, "", null);
                    }
                    break;
                }
                //</editor-fold>
                // <editor-fold defaultstate="collapsed" desc="scanGrabOrder(扫描)">
                case "scanGrabOrder": {
//                int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "0");
                    int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "-1");
                    String taskSerial = paramsJson.getString("taskSerial");
                    OrderGrabController controller = new OrderGrabController();
                    int result = controller.carInFactory(taskSerial, carID);
                    String message;
                    if (result == 0) {
                        message = "申请进厂成功!";
                    } else if (result == -1) {
                        message = "申请进厂失败!";
                    } else if (result == 1) {
                        message = "超过规定进厂时间!";
                    } else if (result == 2) {
                        message = "已经扫描进厂, 不能重复进厂!";
                    } else if (result == 3) {
                        message = "订单状态错误, 不能进厂!";
                    } else {
                        message = "申请进厂出错!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>
                // <editor-fold defaultstate="collapsed" desc="scanGrabOrderOut(扫描出厂)">
                case "scanGrabOrderOut": {
//                int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "0");
                    int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "-1");
                    String taskSerial = paramsJson.getString("taskSerial");
                    OrderGrabController controller = new OrderGrabController();
                    int result = controller.carOutFactory(taskSerial, carID);
                    String message;
                    if (result == 0) {
                        message = "申请出厂成功!";
                    } else {
                        message = "申请出厂出错!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>
                // <editor-fold defaultstate="collapsed" desc="confirmActualNum(确认重量)">
                case "confirmActualNum": {
                    int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "-1");
                    String taskSerial = paramsJson.getString("taskSerial");
                    float actualNum = Float.valueOf((null != paramsJson.getString("actualNum")) ? paramsJson.getString("actualNum") : "-1");
                    OrderGrabController controller = new OrderGrabController();
                    int result = controller.confirmActualNum(taskSerial, carID, actualNum);
                    String message;
                    if (result == 0) {
                        message = "确认重量成功!";
                    } else {
                        message = "确认重量出错!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>
                // <editor-fold defaultstate="collapsed" desc="revokeOrderGrabAudit(撤销订单审核)">
                case "revokeOrderGrabAudit": {
//                int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "-1");
                    String taskSerial = paramsJson.getString("taskSerial");
                    int carID = Integer.valueOf((null != paramsJson.getString("carID")) ? paramsJson.getString("carID") : "-1");
                    int auditStatus = Integer.valueOf((null != paramsJson.getString("auditStatus")) ? paramsJson.getString("auditStatus") : "-1");
                    String auditOpinion = paramsJson.getString("auditOpinion");
                    OrderGrabController controller = new OrderGrabController();
                    int result = controller.revokeOrderGrabAudit(taskSerial, carID, auditStatus, auditOpinion);
                    String message;
                    if (result == 0) {
                        message = "审核成功!";
                    } else if (result == 1) {
                        message = "审核状态错误!";
                    } else if (result == 2) {
                        message = "订单已审核或未申请撤销!";
                    } else {
                        message = "审核出错!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getRevokeGrabOrderList(获取撤销记录)">
                case "getRevokeGrabOrderList": {
                    int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "-1");
                    int revokeStatus = Integer.valueOf((null != paramsJson.getString("revokeStatus")) ? paramsJson.getString("revokeStatus") : "-1");
                    int userID = Integer.valueOf((null != paramsJson.getString("userID")) ? paramsJson.getString("userID") : "-1");
                    String taskSerial = paramsJson.getString("taskSerial");
                    int nowPage = Integer.valueOf((null != paramsJson.getString("nowPage")) ? paramsJson.getString("nowPage") : "0");
                    int pageSize = Integer.valueOf((null != paramsJson.getString("pageSize")) ? paramsJson.getString("pageSize") : "0");
                    OrderGrabController controller = new OrderGrabController();
                    ArrayList<OrderGrab> result = controller.getRevokeOrderGrabList(revokeStatus, userID, taskID, taskSerial, nowPage, pageSize);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "记录为空!", null);
                    }
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getTimeoutInFactoryList(超时未进厂记录)">
                case "getTimeoutInFactoryList": {
                    int taskID = Integer.valueOf((null != paramsJson.getString("taskID")) ? paramsJson.getString("taskID") : "-1");
                    String taskSerial = paramsJson.getString("taskSerial");
                    String startTime = paramsJson.getString("startTime");
                    String endTime = paramsJson.getString("endTime");
                    int nowPage = Integer.valueOf((null != paramsJson.getString("nowPage")) ? paramsJson.getString("nowPage") : "0");
                    int pageSize = Integer.valueOf((null != paramsJson.getString("pageSize")) ? paramsJson.getString("pageSize") : "0");
                    OrderGrabController controller = new OrderGrabController();
                    ArrayList<OrderGrab> result = controller.getTimeoutInFactoryList(taskID, startTime, endTime, taskSerial, nowPage, pageSize);
                    if (null != result) {
                        json = Units.listToJson(result, 0);
                    } else {
                        json = Units.objectToJson(-1, "记录为空!", null);
                    }
                    break;
                }
                //</editor-fold>

                /**
                 * *******************************公告相关************************************
                 */
                //<editor-fold defaultstate="collapsed" desc="addNotice">
                case "addNotice": {
                    String noticeTitle = paramsJson.getString("noticeTitle");
                    String noticeContent = paramsJson.getString("noticeContent");
                    NoticeController controller = new NoticeController();
                    int result = controller.noticeAdd(noticeTitle, noticeContent);
                    String message;
                    if (result == 0) {
                        message = "添加成功!";
                    } else if (result == -1) {
                        message = "添加失败!";
                    } else {
                        message = "添加出错!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="updateNotice">
                case "updateNotice": {
                    int noticeID = Integer.valueOf((null != paramsJson.getString("noticeID")) ? paramsJson.getString("noticeID") : "-1");
                    String noticeTitle = paramsJson.getString("noticeTitle");
                    String noticeContent = paramsJson.getString("noticeContent");
                    NoticeController controller = new NoticeController();
                    int result = controller.noticeUpdate(noticeID, noticeTitle, noticeContent);
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
                //<editor-fold defaultstate="collapsed" desc="deleteNotice">
                case "deleteNotice": {
                    int noticeID = Integer.valueOf((null != paramsJson.getString("noticeID")) ? paramsJson.getString("noticeID") : "-1");
                    NoticeController controller = new NoticeController();
                    int result = controller.noticeDelete(noticeID);
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
                //</editor-fold>
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
                default:
                    break;
            }
        } catch (Exception e) {
            LOG.error(subUri, e);
            json = Units.objectToJson(-1, "输入参数格式错误!", e.toString());
        }

        PrintWriter out = response.getWriter();
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            out.print(json);
        } finally {
            out.close();
        }
    }

    /**
     * 取消指定定时任务
     *
     * @param taskSerial
     * @return
     */
    public boolean shutdownTimerTask(String taskSerial) {
        try {
            ScheduledFuture future = futures.get(taskSerial);
            if (null != future) {
                future.cancel(false);
                futures.remove(taskSerial);
            }
            return true;
        } catch (Exception e) {
            Logger.getLogger(OrderTableController.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
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
