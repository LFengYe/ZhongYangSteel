/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.task;

import com.cn.Web.PCOperateServlet;
import com.cn.controller.LoginUserController;
import com.cn.controller.OrderGrabController;
import com.cn.controller.OrderTableController;
import com.cn.entity.BargainSelectResult;
import com.cn.entity.LoginUser;
import com.cn.entity.OrderTable;
import com.cn.util.PushUnits;
import com.cn.util.Units;
import com.google.gson.JsonObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author LFeng
 */
public class SendDownOrder implements Runnable {

    protected static final Logger LOG = LoggerFactory.getLogger(SendDownOrder.class);

    private final PCOperateServlet controller;
    private String taskSerial;

    public SendDownOrder(String taskSerial, PCOperateServlet controller) {
        this.taskSerial = taskSerial;
        this.controller = controller;
    }

    @Override
    public void run() {
        LOG.info(taskSerial + ", 下发订单定时任务");

        OrderTableController tableController = new OrderTableController();
        OrderTable order = tableController.getTaskDetailWithSerial(this.taskSerial, -1);

        if (null != order) {
            /************验证订单是否是今天下发*************/
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            try {
                Date sendTime = df.parse(order.getSendTime());
                if (!Units.isToday(sendTime)) {
                    LOG.info(taskSerial + "订单失效, 取消下发任务!");
                    controller.shutdownTimerTask(taskSerial);
                    return;
                }
            } catch (ParseException ex) {
                LOG.info(taskSerial + "订单未下发!");
                controller.shutdownTimerTask(taskSerial);
                return;
            }
            /************验证订单是否是今天下发*************/
            
            OrderGrabController grabController = new OrderGrabController();
            grabController.updateOrderEndTime(order.getTaskID());//更新订单结束时间
            
            //<editor-fold defaultstate="collapsed" desc="定价处理">
            if (order.getPriceModel() == 1) {
                int orderCompleteStatus = grabController.orderGrabIsComplete(order.getTaskID());
                LOG.info(taskSerial + ", orderCompleteStatus:" + orderCompleteStatus);
                
                if (orderCompleteStatus == 0) {
                    LOG.info("定价抢单完成");
                    controller.shutdownTimerTask(taskSerial);
                } else {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("taskSerial", order.getTaskSerial());
//                    PushUnits.pushNotifation("enterprise_debug", "有新的可抢订单", "newOrder", jsonObject);
//                    PushUnits.pushNotifation("enterprise_release", "有新的可抢订单", "newOrder", jsonObject);
                    
                    LoginUserController userController = new LoginUserController();
                    ArrayList<LoginUser> list = userController.getCanUseUserList();
                    System.out.println("user size:" + list.size());
                    
                    HashSet<String> userImeiList = new HashSet<>();
                    int count = 0;
                    for (LoginUser user : list) {
                        if (count < 500) {
                            userImeiList.add(user.getImei());
                            count ++;
                        } else {
                            PushUnits.pushNotifationWithAlias(userImeiList, "有新的可抢订单", "newOrder", jsonObject);
                            count = 0;
                            userImeiList.clear();
                        }
                    }
                    if (userImeiList.size() < 500) {
                        PushUnits.pushNotifationWithAlias(userImeiList, "有新的可抢订单", "newOrder", jsonObject);
                    }
                }
            }
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="议价处理">
            if (order.getPriceModel() == 2) {
                BargainSelectResult result = grabController.orderGrabBargainSelect(order.getTaskID());
                LOG.info(taskSerial + ", orderCompleteStatus:" + result.getResult() + ",imei size:" + result.getListIMEI().size());
                /*
                 for (String imei : result.getListIMEI()) {
                 JsonObject jsonObject = new JsonObject();
                 jsonObject.addProperty("taskID", order.getTaskID());
                 PushUnits.pushNotifation(imei, "您的竞价成功, 请在规定时间内进厂", "grabSuccess", jsonObject);
                 System.out.println("竞价成功imei:" + imei);
                 }
                 */
                if (result.getResult() == 0) {
                    LOG.info("议价抢单完成");
                    controller.shutdownTimerTask(taskSerial);
                } else {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("taskSerial", order.getTaskSerial());
//                    PushUnits.pushNotifation("enterprise_debug", "有新的可抢订单", "newOrder", jsonObject);
//                    PushUnits.pushNotifation("enterprise_release", "有新的可抢订单", "newOrder", jsonObject);
                    LoginUserController userController = new LoginUserController();
                    ArrayList<LoginUser> list = userController.getCanUseUserList();
                    System.out.println("user size:" + list.size());
                    
                    HashSet<String> userImeiList = new HashSet<>();
                    int count = 0;
                    for (LoginUser user : list) {
                        if (count < 500) {
                            userImeiList.add(user.getImei());
                            count ++;
                        } else {
                            PushUnits.pushNotifationWithAlias(userImeiList, "有新的可抢订单", "newOrder", jsonObject);
                            count = 0;
                            userImeiList.clear();
                        }
                    }
                    if (userImeiList.size() < 500) {
                        PushUnits.pushNotifationWithAlias(userImeiList, "有新的可抢订单", "newOrder", jsonObject);
                    }
                }
            }
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="指定车辆">
            if (order.getPriceModel() == 3) {
                controller.shutdownTimerTask(taskSerial);
            }
            //</editor-fold>
            
        } else {
            LOG.info("订单不存在了!");
            controller.shutdownTimerTask(taskSerial);
        }
    }

    public String getTaskSerial() {
        return taskSerial;
    }

    public void setTaskSerial(String taskSerial) {
        this.taskSerial = taskSerial;
    }

}
