/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.Web;

import com.alibaba.fastjson.JSONObject;
import com.cn.controller.DestinationDisController;
import com.cn.controller.DistributorController;
import com.cn.controller.GoodsInfoController;
import com.cn.controller.OrderGrabController;
import com.cn.controller.WeightGoodsController;
import com.cn.entity.OrderGrab;
import com.cn.util.Units;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
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
public class ZhongYangOperateServlet extends HttpServlet {

    protected static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ZhongYangOperateServlet.class);

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
        try {
            HttpSession session = request.getSession();
            System.out.println(subUri + ", params is:" + params);
            //将字符串转换为json
            JSONObject paramsJson = JSONObject.parseObject(params);

            switch (subUri) {
                case "test": {
                    Date date = new Date();
                    json = JSONObject.toJSONString(date);
                    break;
                }

                //<editor-fold defaultstate="collapsed" desc="distributorAdd(添加经销商)">
                case "distributorAdd": {
                    String code = paramsJson.getString("distributorId");
                    String name = paramsJson.getString("distributorName");
                    DistributorController controller = new DistributorController();
                    int result = controller.distributorAddZhongYang(name, code);
                    if (result == 0) {
                        json = Units.objectToJson(0, "添加成功!", null);
                    } else if (result > 0) {
                        json = Units.objectToJson(result, "经销商名称已存在!", null);
                    } else if (result == -1) {
                        json = Units.objectToJson(-1, "执行添加出错!", null);
                    } else {
                        json = Units.objectToJson(result, "执行添加出现异常!", null);
                    }
                    break;
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="goodsInfoAdd(添加物料)">
                case "goodsInfoAdd": {
                    String goodsId = paramsJson.getString("goodsId");
                    String goodsName = paramsJson.getString("goodsName");
                    String goodsType = paramsJson.getString("goodsType");
                    String goodsCoefficients = paramsJson.getString("goodsCoefficients");
                    String goodsKind = paramsJson.getString("goodsKind");
                    GoodsInfoController controller = new GoodsInfoController();
                    int result = controller.goodsInfoAddZhongYang(goodsId, goodsName, goodsType, goodsCoefficients, goodsKind);
                    if (result == 0) {
                        json = Units.objectToJson(0, "添加成功!", null);
                    } else if (result > 0) {
                        json = Units.objectToJson(result, "物料名称已存在!", null);
                    } else if (result == -1) {
                        json = Units.objectToJson(-1, "执行添加出错!", null);
                    } else {
                        json = Units.objectToJson(result, "执行添加出现异常!", null);
                    }
                    break;
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="goodsInfoUpdate(修改物料信息)">
                case "goodsInfoUpdate": {
                    String goodsId = paramsJson.getString("goodsId");
                    String goodsName = paramsJson.getString("goodsName");
                    String goodsType = paramsJson.getString("goodsType");
                    String goodsCoefficients = paramsJson.getString("goodsCoefficients");
                    String goodsKind = paramsJson.getString("goodsKind");
                    GoodsInfoController controller = new GoodsInfoController();
                    int result = controller.goodsInfoUpdateZhongYang(goodsId, goodsName, goodsType, goodsCoefficients, goodsKind);
                    if (result == 0) {
                        json = Units.objectToJson(0, "添加成功!", null);
                    } else if (result > 0) {
                        json = Units.objectToJson(result, "物料名称已存在!", null);
                    } else if (result == -1) {
                        json = Units.objectToJson(-1, "执行添加出错!", null);
                    } else {
                        json = Units.objectToJson(result, "执行添加出现异常!", null);
                    }
                    break;
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="destinationDisAdd(添加目的地描述)">
                case "destinationDisAdd": {
                    String disId = paramsJson.getString("desDisId");
                    String disName = paramsJson.getString("desDisName");
                    DestinationDisController controller = new DestinationDisController();
                    int result = controller.destinationDisAddZhongYang(disId, disName);
                    if (result == 0) {
                        json = Units.objectToJson(0, "添加成功!", null);
                    } else if (result == 1) {
                        json = Units.objectToJson(result, "目的地描述ID已存在!", null);
                    } else if (result == 2) {
                        json = Units.objectToJson(result, "目的地描述名称已存在!", null);
                    } else if (result == -1) {
                        json = Units.objectToJson(-1, "执行添加出错!", null);
                    } else {
                        json = Units.objectToJson(result, "执行添加出现异常!", null);
                    }
                    break;
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="storeHouseAdd(添加仓库)">
                case "storeHouseAdd": {
                    String storeHouseId = paramsJson.getString("storeHouseId");
                    String storeHouseName = paramsJson.getString("storeHouseName");
                    json = Units.objectToJson(0, "添加成功!", null);
                    break;
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="(添加承运商)">
                case "": {
                    
                    break;
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="weighingGoods(称重)">
                case "weighingGoods": {
                    String orderSerial = paramsJson.getString("orderSerial");
                    String serialNumber = paramsJson.getString("serialNumber");
                    String carNumber = paramsJson.getString("carNumber");
                    String weightHouseId = paramsJson.getString("weightHouseId");
                    String weightTime = paramsJson.getString("weightTime");
                    String goodsId = paramsJson.getString("goodsId");
                    int goodsNum = Integer.valueOf((null != paramsJson.getString("goodsNum")) ? paramsJson.getString("goodsNum") : "0");
                    float goodsWeight = Float.valueOf((null != paramsJson.getString("goodsWeight")) ? paramsJson.getString("goodsWeight") : "0");
                    WeightGoodsController controller = new WeightGoodsController();
                    int result = controller.addWeightGoods(orderSerial, serialNumber, carNumber, weightHouseId, weightTime, goodsId, goodsNum, goodsWeight);
                    String message;
                    if (result == 0) {
                        message = "称重信息添加成功!";
                    } else if (result == 1) {
                        message = "物料已经称重!";
                    } else if (result == 2) {
                        message = "物料数量与订单中物料数量存在差异!";
                    } else {
                        message = "称重失败!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="orderDetail(二维码订单数据查询)">
                case "orderDetail": {
                    String swiftNumber = paramsJson.getString("swiftNumber");
                    String orderSerial = paramsJson.getString("orderSerial");
                    String carNumber = paramsJson.getString("carNumber");
                    int carId = Integer.valueOf((null != paramsJson.getString("carId")) ? paramsJson.getString("carId") : "-1");
                    String gateSentryId = paramsJson.getString("gateSentryId");
                    OrderGrabController controller = new OrderGrabController();
                    ArrayList<OrderGrab> result = controller.getOrderGrabList(-1, -1, carId, null, -1, -1, -1, -1, orderSerial, null, null, null, 1, 11);
                    if (null != result && result.size() > 0) {
                        json = Units.objectToJson(0, "", result.get(0));
                    } else {
                        json = Units.objectToJson(-1, "输入参数错误, 订单信息为空", null);
                    }
                    break;
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="orderVerifyResult(二维码订单数据回执)">
                case "orderVerifyResult": {
                    String orderSerial = paramsJson.getString("orderSerial");
                    String carNumber = paramsJson.getString("carNumber");
                    int carId = Integer.valueOf((null != paramsJson.getString("carId")) ? paramsJson.getString("carId") : "-1");
                    String driverName = paramsJson.getString("driverName");
                    String driverLicense = paramsJson.getString("driverLicense");
                    int distributorId = Integer.valueOf((null != paramsJson.getString("distributorId")) ? paramsJson.getString("distributorId") : "-1");
                    String gateSentryId = paramsJson.getString("gateSentryId");
                    int resultStatus = Integer.valueOf((null != paramsJson.getString("resultStatus")) ? paramsJson.getString("resultStatus") : "-1");
                    String resultReason = paramsJson.getString("resultReason");

                    OrderGrabController controller = new OrderGrabController();
                    int result = controller.carInFactoryZhongYang(orderSerial, carNumber, resultStatus, resultReason);
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
                    } else if (result == 4) {
                        message = "回写状态错误!";
                    } else if (result == 5) {
                        message = "验证失败回写成功!";
                    } else {
                        message = "申请进厂出错!";
                    }
                    json = Units.objectToJson(result, message, null);
                    break;
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="factoryStatusUpdate(车辆出厂门)">
                case "factoryStatusUpdate": {
                    String swiftNumber = paramsJson.getString("swiftNumber");
                    String orderSerial = paramsJson.getString("orderSerial");
                    String carNumber = paramsJson.getString("carNumber");
                    String updateTime = paramsJson.getString("updateTime");
                    int updateStatus = Integer.valueOf((null != paramsJson.getString("updateStatus")) ? paramsJson.getString("updateStatus") : "-1");
                    OrderGrabController controller = new OrderGrabController();
                    int result = controller.carOutFactoryZhongYang(orderSerial, carNumber);
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

                //<editor-fold defaultstate="collapsed">
                //</editor-fold>
            }
        } catch (Exception e) {
            LOG.error(subUri, e);
            json = Units.objectToJson(-1, "输入参数格式错误!", e.toString());
        }

        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        try (PrintWriter out = response.getWriter()) {
            out.print(json);
        }
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
