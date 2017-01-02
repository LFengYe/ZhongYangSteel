/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.entity.GoodsType;
import com.cn.entity.OrderTable;
import com.cn.util.DatabaseOpt;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
public class OrderTableController {

//    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(8);
//    private HashMap<String, ExecutorService> services;
    /**
     *
     * @param goodsType - 货物类型
     * @param isMixed
     * @param priceModel - 定价或议价: 0 定价 | 1 议价
     * @param timeCountDown - 抢单时间(分钟)
     * @param taxRate
     * @param pickupType
     * @param price
     * @param taskType - 1 按重量数量 | 2 按车辆数量 | 3 指定车辆
     * @param startPlace
     * @param endPlace
     * @param carRange - 范围(单位Km)
     * @param tonnage
     * @param carNum
     * @param selectedCar
     * @param enterpriseID
     * @param carType
     * @param carLength
     * @param length
     * @param factoryTime - 规定最迟进厂时间
     * @param transportID - 运输处ID
     * @param destinationDis
     * @param distributorContact
     * @param distributorID - 经销商ID
     * @param distributorPhoneNum
     * @return
     */
    public OrderAddResult taskAdd(String goodsType, int isMixed, int priceModel, float price, int taxRate, int pickupType, int timeCountDown, int taskType,
            int startPlace, int endPlace, float carRange, float tonnage, int carNum, String selectedCar, int enterpriseID, String carType, float carLength,
            float length, int factoryTime, int transportID, int distributorID, String destinationDis, String distributorContact, String distributorPhoneNum) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        OrderAddResult result;
        try {
            statement = conn.prepareCall("{call tbOrderAdd(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            statement.setString("_goodsType", goodsType);
            statement.setInt("_isMixed", isMixed);
            statement.setInt("_priceModel", priceModel);
            statement.setFloat("_price", price);
            statement.setInt("_taxRate", taxRate);
            statement.setInt("_pickupType", pickupType);
            statement.setInt("_timeCountDown", timeCountDown);
//            statement.setString("_taskEndTime", taskEndTime);
            statement.setInt("_taskType", taskType);
            statement.setInt("_startPlace", startPlace);
            statement.setInt("_endPlace", endPlace);
            statement.setFloat("_carRange", carRange);
            statement.setFloat("_taskTonnage", tonnage);
            statement.setInt("_taskCarNum", carNum);
            statement.setString("_selectedCar", selectedCar);
            statement.setString("_carType", carType);
            statement.setFloat("_carLength", carLength);
            statement.setInt("_enterpriseID", enterpriseID);
            statement.setFloat("_tLenth", length);
            statement.setInt("_factoryTime", factoryTime);
            statement.setInt("_distributorID", distributorID);
            statement.setString("_destinationDis", destinationDis);
            statement.setString("_distributorContact", distributorContact);
            statement.setString("_distributorPhoneNum", distributorPhoneNum);
            statement.setInt("_transportID", transportID);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.registerOutParameter("_taskSerial", Types.VARCHAR);
            statement.executeUpdate();

            result = new OrderAddResult();
            result.setResult(statement.getInt("result"));
            result.setTaskSerial(statement.getString("_taskSerial"));

            return result;
        } catch (SQLException ex) {
            Logger.getLogger(OrderTableController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    /**
     * 改派订单
     * @param taskID
     * @param startPlace
     * @param endPlace
     * @param distributorID
     * @param destinationDis
     * @param distributorContact
     * @param distributorPhoneNum
     * @return 
     */
    public int taskReassignment(int taskID, int startPlace, int endPlace, int distributorID, String destinationDis, String distributorContact, String distributorPhoneNum) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbOrderReassignment(?, ?, ?, ?, ?, ?, ?, ?)}");
            statement.setInt("_taskID", taskID);
            statement.setInt("_startPlace", startPlace);
            statement.setInt("_endPlace", endPlace);
            statement.setInt("_distributorID", distributorID);
            statement.setString("_destinationDis", destinationDis);
            statement.setString("_distributorContact", distributorContact);
            statement.setString("_distributorPhoneNumber", distributorPhoneNum);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(OrderTableController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 撤销订单
     * @param taskID
     * @return 
     */
    public OrderAddResult taskRevoke(int taskID) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        OrderAddResult result;
        try {
            statement = conn.prepareCall("{call tbOrderRevoke(?, ?, ?)}");
            statement.setInt("_taskID", taskID);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.registerOutParameter("_taskSerial", Types.VARCHAR);
            statement.executeUpdate();
            
            result = new OrderAddResult();
            result.setResult(statement.getInt("result"));
            result.setTaskSerial(statement.getString("_taskSerial"));
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(OrderTableController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    /**
     *
     * @param taskSerial
     * @param types
     * @return
     */
    public int taskAddGoodsDetail(String taskSerial, ArrayList<GoodsType> types) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        PreparedStatement statement = null;
        int result = -1;
        try {
            conn.setAutoCommit(false);
            statement = conn.prepareStatement("{call tbOrderAddGoods(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            for (GoodsType type : types) {
                statement.setString(1, taskSerial);
                statement.setString(2, type.getGoodsID());
                statement.setString(3, type.getGoodsName());
                statement.setString(4, type.getGoodsModel());
                statement.setString(5, type.getGoodsType());
                statement.setInt(6, type.getGoodsNum());
                statement.setFloat(7, type.getGoodsWeight());
                statement.setInt(8, type.getGoodsSequence());
                statement.setString(9, type.getGateSentryId());
                statement.setString(10, type.getGateSentryName());
                statement.addBatch();
            }
            
            statement.executeBatch();
            conn.commit();
            result = 0;
        } catch (SQLException ex) {
            Logger.getLogger(OrderTableController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * 更新货物明细
     * @param taskID
     * @param types
     * @return 
     */
    public int taskUpdateGoodsDetail(int taskID, ArrayList<GoodsType> types) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        PreparedStatement statement = null;
        int result = -1;
        try {
            conn.setAutoCommit(false);
            statement = conn.prepareStatement("{call tbOrderUpdateGoods(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            for (GoodsType type : types) {
                statement.setInt(1, taskID);
                statement.setString(2, type.getGoodsID());
                statement.setString(3, type.getGoodsName());
                statement.setString(4, type.getGoodsModel());
                statement.setString(5, type.getGoodsType());
                statement.setInt(6, type.getGoodsNum());
                statement.setFloat(7, type.getGoodsWeight());
                statement.setInt(8, type.getGoodsSequence());
                statement.setString(9, type.getGateSentryId());
                statement.setString(10, type.getGateSentryName());
                statement.addBatch();
            }
            statement.executeBatch();
            conn.commit();
            result = 0;
        } catch (SQLException ex) {
            Logger.getLogger(OrderTableController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 下发订单
     *
     * @param taskSerial
     * @return
     */
    public int taskSend(String taskSerial) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbOrderSend(?, ?)}");
            statement.setString("_taskSerial", taskSerial);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            Logger.getLogger(OrderTableController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * 获取车辆抢单次数
     *
     * @param taskID
     * @param carID
     * @return 返回抢单次数, 若为-1则执行出错
     */
    public int getCarGrabTimes(int taskID, int carID) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbCarGrabTimes(?, ?, ?)}");
            statement.setInt("_taskID", taskID);
            statement.setInt("_carID", carID);
            statement.registerOutParameter("times", Types.INTEGER);
            statement.executeQuery();
            result = statement.getInt("times");
        } catch (SQLException ex) {
            Logger.getLogger(OrderTableController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * 获取任务列表
     *
     * @param taskID
     * @param taskSerial
     * @param listType -- -1 全部订单 | 1 未完成订单 | 2 已抢单完成订单 | 0 未下发订单 | 3 已完成订单 | 99
     * 已下发所有订单
     * @param startPlace
     * @param endPlace
     * @param distributorID -- 经销商ID
     * @param distributorName
     * @param enterpriseID
     * @param startTime
     * @param endTime
     * @param carNumber
     * @param destinationDis
     * @param nowPage
     * @param pageSize
     * @return
     */
    public ArrayList<OrderTable> getTaskList(int taskID, String taskSerial, int listType, int startPlace, int endPlace, int distributorID, String distributorName,
            int enterpriseID, String startTime, String endTime, String carNumber, String destinationDis, int nowPage, int pageSize) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        ArrayList<OrderTable> result = new ArrayList<>();
        try {
            statement = conn.prepareCall("{call tbOrderGet(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            statement.setInt("taskID_in", taskID);
            statement.setString("_taskSerial", taskSerial);
            statement.setInt("_listType", listType);
            statement.setInt("_startPlace", startPlace);
            statement.setInt("_endPlace", endPlace);
            statement.setInt("_distributorID", distributorID);
            statement.setString("_distributorName", distributorName);
            statement.setInt("_enterpriseID", enterpriseID);
            statement.setString("_startTime", startTime);
            statement.setString("_endTime", endTime);
            statement.setString("_carNumber", carNumber);
            statement.setString("_destinationDis", destinationDis);
            statement.setInt("nowPage", nowPage);
            statement.setInt("pageSize", pageSize);
            ResultSet set = statement.executeQuery();
            
            OrderDetailController controller = new OrderDetailController();
            while (set.next()) {
                OrderTable task = new OrderTable();
                task.setTaskID(set.getInt("TaskID"));
                task.setTaskSerial(set.getString("TaskSerial"));
                task.setTaskEndTime(set.getString("TaskEndTime"));
                task.setTaskCreateTime(set.getString("TaskCreateTime"));
                task.setTaskStatus(set.getInt("TaskStatus"));
//                task.setGoodsType(set.getString("GoodsType"));
                
                task.setGoodsType(JSONObject.toJSONString(controller.getOrderGoodsInfo(task.getTaskID(), 1, 99)));
                task.setPriceModel(set.getInt("PriceModel"));
                task.setPrice(set.getFloat("Price"));
                task.setTimeCountDown(set.getInt("TimeCountDown"));
                task.setStartPlace(set.getInt("StartPlace"));
                task.setEndPlace(set.getInt("EndPlace"));
                task.setStartPlaceName(set.getString("StartPlaceName"));
                task.setEndPlaceName(set.getString("EndPlaceName"));
                task.setGrabedNum(set.getInt("GrabedNum"));

                task.setTaskType(set.getInt("TaskType"));
                task.setCarRenge(set.getFloat("CarRange"));
                task.setTaskCarNum(set.getInt("TaskCarNum"));
                task.setTaskTonnage(set.getFloat("TaskTonnage"));
                task.setSendTime(set.getString("SendTime"));
                task.setEnterpriseID(set.getInt("EnterpriseID"));
                task.setSelectedCar(set.getString("SelectedCar"));
                task.setCarType(set.getString("CarType"));
                task.setCarLength(set.getFloat("CarLength"));
                task.settLenth(set.getFloat("TLenth"));
                task.setFactoryTime(set.getInt("FactoryTime"));
                task.setTransportID(set.getInt("TransportID"));
                
                task.setDistributorID(set.getInt("DistributorID"));
                task.setDistributorName(set.getString("DistributorName"));
                task.setDistributorContact(set.getString("DistributorContact"));
                task.setDistributorPhoneNumber(set.getString("DistributorPhoneNumber"));
                task.setDestinationDis(set.getString("DestinationDis"));
                task.setTaxRate(set.getInt("TaxRate"));
                task.setPickupType(set.getInt("PickupType"));
                
                task.setRemainTonnage(set.getFloat("remainTonnage"));
                task.setRemainCarNum(set.getInt("remainCarNum"));
                task.setServerCurTime(set.getString("ServerCurTime"));
                
                getTaskNum(conn, task);
                result.add(task);
            }
        } catch (Exception e) {
            Logger.getLogger(OrderTableController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public ArrayList<OrderTable> getTaskList(int taskID, int listType, int startPlace, int endPlace, String distributorName,
            int enterpriseID, String startTime, String endTime, int nowPage, int pageSize) {
        return getTaskList(taskID, null, listType, startPlace, endPlace, -1, distributorName, enterpriseID, startTime, endTime, null, null, nowPage, pageSize);
    }

    /**
     * 获取订单的已抢单车辆数、剩余车辆数、剩余重量
     * @param connection
     * @param order 
     */
    public void getTaskNum(Connection connection, OrderTable order) {
        CallableStatement statement = null;
        try {
            statement = connection.prepareCall("{call tbOrderGetNum(?, ?)}");
            statement.setInt("_taskID_in", order.getTaskID());
            statement.registerOutParameter("result", Types.INTEGER);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                order.setGrabedNum(set.getInt("GradedNum"));
                order.setRemainCarNum(set.getInt("remainCarNum"));
                order.setRemainTonnage(set.getFloat("remainTonnage"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderTableController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 获取订单详情
     *
     * @param taskSerial
     * @param taskID
     * @return
     */
    public OrderTable getTaskDetailWithSerial(String taskSerial, int taskID) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        OrderTable result = null;
        try {
            statement = conn.prepareCall("{call tbOrderDetail(?, ?, ?)}");
            statement.setString("_taskSerial", taskSerial);
            statement.setInt("_taskID_in", taskID);
            statement.registerOutParameter("result", Types.INTEGER);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                result = new OrderTable();
                result.setTaskID(set.getInt("TaskID"));
                result.setTaskSerial(set.getString("TaskSerial"));
                result.setTaskEndTime(set.getString("TaskEndTime"));
                result.setTaskCreateTime(set.getString("TaskCreateTime"));
                result.setTaskStatus(set.getInt("TaskStatus"));
                OrderDetailController controller = new OrderDetailController();
                result.setGoodsType(JSONObject.toJSONString(controller.getOrderGoodsInfo(result.getTaskID(), 1, 99)));
                result.setPriceModel(set.getInt("PriceModel"));
                result.setPrice(set.getFloat("Price"));
                result.setTimeCountDown(set.getInt("TimeCountDown"));
                result.setStartPlace(set.getInt("StartPlace"));
                result.setEndPlace(set.getInt("EndPlace"));
                result.setStartPlaceName(set.getString("StartPlaceName"));
                result.setEndPlaceName(set.getString("EndPlaceName"));
                result.setTaskType(set.getInt("TaskType"));
                result.setCarRenge(set.getFloat("CarRange"));
                result.setTaskCarNum(set.getInt("TaskCarNum"));
                result.setTaskTonnage(set.getFloat("TaskTonnage"));
                result.setSendTime(set.getString("SendTime"));
                result.setEnterpriseID(set.getInt("EnterpriseID"));
                
                result.setDistributorID(set.getInt("DistributorID"));
                result.setDistributorName(set.getString("DistributorName"));
                result.setDistributorContact(set.getString("DistributorContact"));
                result.setDistributorPhoneNumber(set.getString("DistributorPhoneNumber"));
                result.setDestinationDis(set.getString("DestinationDis"));
                result.setTaxRate(set.getInt("TaxRate"));
                result.setPickupType(set.getInt("PickupType"));
                
                result.setSelectedCar(set.getString("SelectedCar"));
                result.setCarType(set.getString("CarType"));
                result.setCarLength(set.getFloat("CarLength"));
                result.settLenth(set.getFloat("TLenth"));
                result.setFactoryTime(set.getInt("FactoryTime"));
                result.setTransportID(set.getInt("TransportID"));

                result.setRemainTonnage(set.getFloat("remainTonnage"));
                result.setRemainCarNum(set.getInt("remainCarNum"));
                result.setServerCurTime(set.getString("ServerCurTime"));
            }
            return result;
        } catch (Exception e) {
            Logger.getLogger(OrderTableController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public OrderTable getTaskDetailWithID(int taskID) {
        ArrayList<OrderTable> result = getTaskList(taskID, null, -1, -1, -1, -1,null, -1, null, null, null, null, 1, 10);
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    public class OrderAddResult {

        private int result;
        private String taskSerial;

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public String getTaskSerial() {
            return taskSerial;
        }

        public void setTaskSerial(String taskSerial) {
            this.taskSerial = taskSerial;
        }

    }

    /*
     public static void main(String[] args) {
     OrderTableController controller = new OrderTableController();
     OrderTable order = controller.getTaskDetailWithSerial("2016031814445300001");
     controller.taskSend(order);
     System.out.println("已完成");
     }
     */
}
