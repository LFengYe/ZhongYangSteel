/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.controller;

import com.alibaba.fastjson.JSONObject;
import com.cn.entity.BargainSelectResult;
import com.cn.entity.CarLocation;
import com.cn.entity.OrderGrab;
import com.cn.entity.OrderGrabDetail;
import com.cn.entity.OrderGrabDis;
import com.cn.util.DatabaseOpt;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author LFeng
 */
public class OrderGrabController {

    protected static final org.slf4j.Logger LOG = LoggerFactory.getLogger(OrderGrabController.class);

    /**
     * 抢单 若是司机登录, 则抢单用户ID和分配司机ID相同
     *
     * @param userID -- 抢单用户ID
     * @param driverID -- 抢单分配司机ID
     * @param carID
     * @param oPrice
     * @param taskNum
     * @param taskID
     * @return -- 0 异常 | 1 已抢过 | 2 数量不足 | 3 已被他人抢 | 4 超过抢单次数 | -1 抢单成功
     */
    public int addOrderGrab(int userID, int driverID, int carID, float oPrice, float taskNum, int taskID) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbOrderGrabAdd(?, ?, ?, ?, ?, ?, ?)}");
            statement.setInt("_enterpriseID", userID);
            statement.setInt("_driverID", driverID);
            statement.setInt("_carID", carID);
            statement.setFloat("_oPrice", oPrice);
            statement.setFloat("_taskNum", taskNum);
            statement.setInt("_taskID", taskID);
            statement.registerOutParameter("_grabStatus", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("_grabStatus");
        } catch (Exception e) {
            Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * 再次更新订单结束时间
     *
     * @param taskID
     * @return
     */
    public int updateOrderEndTime(int taskID) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbOrderUpdateEndTime(?, ?)}");
            statement.setInt("_taskID", taskID);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
            LOG.info("订单" + taskID + "结束时间更新成功");
        } catch (SQLException ex) {
            Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * 撤销订单
     *
     * @param taskID
     * @param carID
     * @param failReason
     * @return
     */
    public int revokeOrderGrab(int taskID, int carID, String failReason) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbOrderGrabRevoke(?, ?, ?, ?)}");
            statement.setInt("_taskID", taskID);
            statement.setInt("_carID", carID);
            statement.setString("_failReason", failReason);
            statement.registerOutParameter("_addStatus", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("_addStatus");
        } catch (Exception e) {
            Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * 车辆进厂
     *
     * @param taskSerial
     * @param carID
     * @return
     */
    public int carInFactory(String taskSerial, int carID) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbOrderGrabCarInFactory(?, ?, ?, ?)}");
            statement.setString("_taskSerial", taskSerial);
            statement.setInt("_carID", carID);
            statement.registerOutParameter("_addStatus", Types.INTEGER);
            statement.registerOutParameter("t_error", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("_addStatus");
        } catch (Exception e) {
            Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * 车辆进厂_中阳接口
     *
     * @param taskSerial
     * @param carNumber
     * @param resultResult -- 验证结果: 0 失败 | 1 成功
     * @param resultReason -- 失败原因
     * @return
     */
    public int carInFactoryZhongYang(String taskSerial, String carNumber, int resultResult, String resultReason) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbZhongYang_CarInFactory(?, ?, ?, ?, ?, ?)}");
            statement.setString("_taskSerial", taskSerial);
            statement.setString("_carNumber", carNumber);
            statement.setInt("_resultStatus", resultResult);
            statement.setString("_resultReason", resultReason);
            statement.registerOutParameter("_addStatus", Types.INTEGER);
            statement.registerOutParameter("t_error", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("_addStatus");
        } catch (Exception e) {
            Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 车辆出厂
     *
     * @param taskSerial
     * @param carID
     * @return
     */
    public int carOutFactory(String taskSerial, int carID) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbOrderGrabCarOutFactory(?, ?, ?, ?)}");
            statement.setString("_taskSerial", taskSerial);
            statement.setInt("_carID", carID);
            statement.registerOutParameter("_addStatus", Types.INTEGER);
            statement.registerOutParameter("t_error", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("_addStatus");
        } catch (Exception e) {
            Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * 车辆出厂_中阳接口
     * @param taskSerial
     * @param carNumber
     * @return 
     */
    public int carOutFactoryZhongYang(String taskSerial, String carNumber) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbZhongYang_CarOutFactory(?, ?, ?, ?)}");
            statement.setString("_taskSerial", taskSerial);
            statement.setString("_carNumber", carNumber);
            statement.registerOutParameter("_addStatus", Types.INTEGER);
            statement.registerOutParameter("t_error", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("_addStatus");
        } catch (Exception e) {
            Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 确认实际运输重量
     *
     * @param taskSerial
     * @param carID
     * @param actualNum
     * @return
     */
    public int confirmActualNum(String taskSerial, int carID, float actualNum) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbOrderGrabConfrimActualNum(?, ?, ?, ?)}");
            statement.setString("_taskSerial", taskSerial);
            statement.setInt("_carID", carID);
            statement.setFloat("_actualNum", actualNum);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * 车辆到达
     *
     * @param taskID
     * @param carID
     * @param arriveStatus 3 -- 企业用户/司机到达 | 4 -- 经销商确认达到
     * @return
     */
    public int carArrive(int taskID, int carID, int arriveStatus) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbOrderGrabArriveStatus(?, ?, ?, ?)}");
            statement.setInt("_taskID", taskID);
            statement.setInt("_carID", carID);
            statement.setInt("_arriveStatus", arriveStatus);
            statement.registerOutParameter("_addStatus", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("_addStatus");
        } catch (Exception e) {
            Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * 审核撤销抢单申请
     *
     * @param taskSerial
     * @param carID
     * @param auditStatus 2 -- 通过 | 4 -- 不通过
     * @param auditOpinion
     * @return
     */
    public int revokeOrderGrabAudit(String taskSerial, int carID, int auditStatus, String auditOpinion) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbOrderGrabRevokeAudit(?, ?, ?, ?, ?)}");
            statement.setString("_taskSerial", taskSerial);
            statement.setInt("_carID", carID);
            statement.setInt("_auditStatus", auditStatus);
            statement.setString("_auditOpinion", auditOpinion);
            statement.registerOutParameter("_addStatus", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("_addStatus");
        } catch (Exception e) {
            Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * 判断定价模式订单是否完成, 议价模式另外处理
     *
     * @param taskID
     * @return
     */
    public int orderGrabIsComplete(int taskID) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbOrderGrabIsComplete(?, ?)}");
            statement.setInt("_taskID", taskID);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (Exception e) {
            Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * 议价模式选择抢单记录, 并返回选中记录对应的企业用户ID
     *
     * @param taskID
     * @return 出错返回空
     */
    public BargainSelectResult orderGrabBargainSelect(int taskID) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        BargainSelectResult selectResult = new BargainSelectResult();
        try {
            statement = conn.prepareCall("{call tbOrderGrabBargainSelect(?, ?)}");
            statement.setInt("_taskID", taskID);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.execute();
            result = statement.getInt("result");
            selectResult.setResult(result);

            ResultSet set = statement.getResultSet();
            while (set.next()) {
                selectResult.getListIMEI().add(set.getString("IMEI"));
            }
            return selectResult;
        } catch (Exception e) {
            Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    /**
     * 获取用户车辆抢单数目
     *
     * @param taskID
     * @param userID
     * @return
     */
    public int getOrderGrabCarNum(int taskID, int userID) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbOrderGrabGetGrabedCarNum(?, ?, ?)}");
            statement.setInt("_taskID", taskID);
            statement.setInt("_userID", userID);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (Exception e) {
            Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * 修改进厂过时抢单记录的状态
     *
     * @return
     */
    public int orderGrabTimeout() {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbOrderGrabTimeoutUpdate(?)}");
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (Exception e) {
            Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * 打印订单信息
     *
     * @param taskSerial
     * @param carID
     * @return
     */
    public OrderGrabDetail printOrderGrab(String taskSerial, int carID) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        try {
            statement = conn.prepareCall("{call tbOrderGrabPrint(?, ?, ?)}");
            statement.setString("_taskSerial", taskSerial);
            statement.setInt("_carID", carID);
            statement.registerOutParameter("t_error", Types.INTEGER);
            if (statement.execute()) {
                ResultSet set = statement.getResultSet();
                OrderGrabDetail grab = null;
                while (set.next()) {
                    grab = new OrderGrabDetail();
                    grab.setEnterpriseID(set.getString("EnterpriseID"));
                    grab.setEnterpriseName(set.getString("EnterpriseName"));
                    grab.setEnterpriseType(set.getInt("EnterpriseType"));
                    grab.setEnterpriseAddress(set.getString("EnterpriseAddress"));
                    grab.setEnterpriseContact(set.getString("EnterpriseContact"));
                    grab.setEnterprisePhoneNumber(set.getString("EnterprisePhoneNumber"));
                    grab.setCarrierName(set.getString("EnterpriseCarrierName"));
                    grab.setDriverID(set.getInt("DriverID"));
                    grab.setDriverName(set.getString("DriverName"));
                    grab.setDLicense(set.getString("DLicense"));
                    grab.setDPhoneNumber(set.getString("DPhoneNumber"));
                    grab.setCarID(set.getInt("CarID"));
                    grab.setSystemNo(set.getString("SystemNo"));
                    grab.setCarNumber(set.getString("CarNumber"));
                    grab.setCarType(set.getString("CarType"));
                    grab.setCarWeight(set.getString("CarWeight"));
                    grab.setTaskID(set.getInt("TaskID"));
                    grab.setTaskSerial(set.getString("TaskSerial"));
//                    grab.setGoodsType(set.getString("GoodsType"));
                    OrderDetailController controller = new OrderDetailController();
                    grab.setGoodsType(JSONObject.toJSONString(controller.getOrderGoodsInfo(grab.getTaskID(), 1, 99)));
                    
                    grab.setPrice(set.getFloat("Price"));
                    grab.setTimeCountDown(set.getString("TimeCountDown"));
                    grab.setFactoryTime(set.getString("FactoryTime"));
                    grab.setTaskType(set.getInt("TaskType"));
                    grab.setStartPlace(set.getInt("StartPlace"));
                    grab.setStartPlaceName(set.getString("StartPlaceName"));
                    grab.setEndPlace(set.getInt("EndPlace"));
                    grab.setEndPlaceName(set.getString("EndPlaceName"));
                    grab.setSendTime(set.getString("SendTime"));
                    grab.setTLenth(set.getFloat("TLenth"));
                    grab.setTaskCarNum(set.getInt("TaskCarNum"));
                    grab.setTaskTonnage(set.getFloat("TaskTonnage"));
                    grab.setSendTime(set.getString("SendTime"));
                    grab.setCarType(set.getString("CarType"));
                    grab.setOPrice(set.getFloat("OPrice"));
                    grab.setGrabTimes(set.getInt("GrabTimes"));
                    grab.setGrabStatus(set.getInt("GrabStatus"));
                    grab.setGrabTime(set.getString("GrabTime"));
                    grab.setFailReason(set.getString("FailReason"));
                    grab.setDistributorName(set.getString("DistributorName"));
                    grab.setDistributorContact(set.getString("DistributorContact"));
                    grab.setDistributorPhoneNumber(set.getString("DistributorPhoneNumber"));
                    grab.setDestinationDis(set.getString("DestinationDis"));
                    grab.setTaxRate(set.getInt("TaxRate"));
                    grab.setPickupType(set.getInt("PickupType"));
                }
                return grab;
            }
        } catch (Exception e) {
            Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    /**
     * 获取抢单信息列表
     *
     * @param userID
     * @param driverID
     * @param carID
     * @param carNumber
     * @param grabStatus
     * @param grabTimes
     * @param priceModel
     * @param taskID
     * @param taskSerial
     * @param distributorName
     * @param startTime
     * @param endTime
     * @param nowPage
     * @param pageSize
     * @return
     */
    public ArrayList<OrderGrab> getOrderGrabList(int userID, int driverID, int carID, String carNumber, int grabStatus, int grabTimes,
            int priceModel, int taskID, String taskSerial, String distributorName, String startTime, String endTime, int nowPage, int pageSize) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        ArrayList<OrderGrab> result = new ArrayList<>();
        try {
            statement = conn.prepareCall("{call tbOrderGrabGet(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            statement.setInt("_enterpriseID", userID);
            statement.setInt("_driverID", driverID);
            statement.setInt("_carID", carID);
            statement.setString("_carNumber", carNumber);
            statement.setInt("_grabStatus", grabStatus);
            statement.setInt("_grabTimes", grabTimes);
            statement.setInt("_priceModel", priceModel);
            statement.setInt("_taskID", taskID);
            statement.setString("_taskSerial", taskSerial);
            statement.setString("_distributorName", distributorName);
            statement.setString("_startTime", startTime);
            statement.setString("_endTime", endTime);
            statement.setInt("nowPage", nowPage);
            statement.setInt("pageSize", pageSize);
            ResultSet set = statement.executeQuery();
            CarTableController controller = new CarTableController();
            while (set.next()) {
                OrderGrab task = new OrderGrab();

                task.setEnterpriseID(set.getString("EnterpriseID"));
                task.setEnterpriseName(set.getString("EnterpriseName"));
                task.setCarrierName(set.getString("EnterpriseCarrierName"));
                task.setDriverID(set.getInt("DriverID"));
                task.setDriverName(set.getString("DriverName"));
                task.setDLicense(set.getString("DLicense"));

                task.setCarID(set.getInt("CarID"));
                task.setSystemNo(set.getString("SystemNo"));
                task.setCarNumber(set.getString("CarNumber"));

                task.setTaskID(set.getInt("TaskID"));
                task.setTaskSerial(set.getString("TaskSerial"));
//                task.setGoodsType(set.getString("GoodsType"));
                OrderDetailController controller1 = new OrderDetailController();
                task.setGoodsType(JSONObject.toJSONString(controller1.getOrderGoodsInfo(task.getTaskID(), 1, 99)));
                task.setTaskStatus(set.getInt("TaskStatus"));
                task.setPriceModel(set.getInt("PriceModel"));
                task.setFactoryTime(set.getInt("FactoryTime"));
                task.setInFactoryStatus(set.getInt("InFactoryStatus"));
                task.setTaskType(set.getInt("TaskType"));
                task.setStartPlace(set.getInt("StartPlace"));
                task.setStartPlaceName(set.getString("StartPlaceName"));
                task.setStartPlaceLatitude(set.getFloat("StartPlaceLatitude"));
                task.setStartPlaceLongitude(set.getFloat("StartPlaceLongitude"));
                task.setEndPlace(set.getInt("EndPlace"));
                task.setEndPlaceName(set.getString("EndPlaceName"));
                task.setEndPlaceLatitude(set.getFloat("EndPlaceLatitude"));
                task.setEndPlaceLongitude(set.getFloat("EndPlaceLongitude"));

                if (null != task.getSystemNo()) {
                    CarLocation location = controller.getCarLocationWithSystemNo(task.getSystemNo());
                    task.setCurLatitude(location.getLatitude());
                    task.setCurLongitude(location.getLongitude());
                }

                task.setDistributorName(set.getString("DistributorName"));
                task.setDistributorContact(set.getString("DistributorContact"));
                task.setDistributorPhoneNumber(set.getString("DistributorPhoneNumber"));
                task.setDestinationDis(set.getString("DestinationDis"));
                task.setTaxRate(set.getInt("TaxRate"));
                task.setPickupType(set.getInt("PickupType"));

                task.setCarMinLength(set.getFloat("minCarLength"));
                task.setSendTime(set.getString("SendTime"));
                task.setTLenth(set.getFloat("TLenth"));
                task.setTaskNum(set.getFloat("TaskNum"));
                task.setOPrice(set.getFloat("OPrice"));
                task.setGrabTimes(set.getInt("GrabTimes"));
                task.setGrabStatus(set.getInt("GrabStatus"));
                task.setGrabTime(set.getString("GrabTime"));
                task.setFailReason(set.getString("FailReason"));
                task.setRevokeStatus(set.getInt("RevokeStatus"));
                task.setServerCurTime(set.getString("ServerCurTime"));

                result.add(task);
            }
        } catch (Exception e) {
            Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public ArrayList<OrderGrab> getOrderGrabList(int userID, int driverID, int carID, int grabStatus, int grabTimes, int taskID, String distributorName, String startTime, String endTime, int nowPage, int pageSize) {
        return getOrderGrabList(userID, driverID, carID, null, grabStatus, grabTimes, -1, taskID, null, distributorName, startTime, endTime, nowPage, pageSize);
    }

    /**
     * 获取撤销的抢单记录
     *
     * @param revokeStatus
     * @param userID
     * @param taskID
     * @param taskSerial
     * @param nowPage
     * @param pageSize
     * @return
     */
    public ArrayList<OrderGrab> getRevokeOrderGrabList(int revokeStatus, int userID, int taskID, String taskSerial, int nowPage, int pageSize) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        ArrayList<OrderGrab> result = new ArrayList<>();
        try {
            statement = conn.prepareCall("{call tbOrderGrabRevokeList(?, ?, ?, ?, ?, ?)}");
            statement.setInt("_enterpriseID", userID);
            statement.setInt("_revokeStatus", revokeStatus);
            statement.setInt("_taskID", taskID);
            statement.setString("_taskSerial", taskSerial);
            statement.setInt("nowPage", nowPage);
            statement.setInt("pageSize", pageSize);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                OrderGrab task = new OrderGrab();

                task.setDriverID(set.getInt("DriverID"));
                task.setDriverName(set.getString("DriverName"));
                task.setDLicense(set.getString("DLicense"));

                task.setCarID(set.getInt("CarID"));
                task.setSystemNo(set.getString("SystemNo"));
                task.setCarNumber(set.getString("CarNumber"));

                task.setTaskID(set.getInt("TaskID"));
                task.setTaskSerial(set.getString("TaskSerial"));
//                task.setGoodsType(set.getString("GoodsType"));
                OrderDetailController controller1 = new OrderDetailController();
                task.setGoodsType(JSONObject.toJSONString(controller1.getOrderGoodsInfo(task.getTaskID(), 1, 99)));
                task.setTaskStatus(set.getInt("TaskStatus"));
                task.setPriceModel(set.getInt("PriceModel"));
                task.setFactoryTime(set.getInt("FactoryTime"));
                task.setInFactoryStatus(set.getInt("InFactoryStatus"));
                task.setTaskType(set.getInt("TaskType"));
                task.setStartPlace(set.getInt("StartPlace"));
                task.setStartPlaceName(set.getString("StartPlaceName"));
                task.setStartPlaceLatitude(set.getFloat("StartPlaceLatitude"));
                task.setStartPlaceLongitude(set.getFloat("StartPlaceLongitude"));
                task.setEndPlace(set.getInt("EndPlace"));
                task.setEndPlaceName(set.getString("EndPlaceName"));
                task.setEndPlaceLatitude(set.getFloat("EndPlaceLatitude"));
                task.setEndPlaceLongitude(set.getFloat("EndPlaceLongitude"));

                task.setSendTime(set.getString("SendTime"));
                task.setOPrice(set.getFloat("OPrice"));
                task.setGrabTimes(set.getInt("GrabTimes"));
                task.setGrabTime(set.getString("GrabTime"));
                task.setRevokeStatus(set.getInt("RevokeStatus"));
                task.setFailReason(set.getString("FailReason"));
                task.setAuditOpinion(set.getString("AuditOpinion"));

                result.add(task);
            }
        } catch (Exception e) {
            Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * 获取超时未进厂的抢单记录
     *
     * @param taskID
     * @param startTime
     * @param endTime
     * @param taskSerial
     * @param nowPage
     * @param pageSize
     * @return
     */
    public ArrayList<OrderGrab> getTimeoutInFactoryList(int taskID, String startTime, String endTime, String taskSerial, int nowPage, int pageSize) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        ArrayList<OrderGrab> result = new ArrayList<>();
        try {
            statement = conn.prepareCall("{call tbOrderGrabTimeoutInFactory(?, ?, ?, ?, ?, ?)}");
            statement.setInt("_taskID", taskID);
            statement.setString("_startTime", startTime);
            statement.setString("_endTime", endTime);
            statement.setString("_taskSerial", taskSerial);
            statement.setInt("nowPage", nowPage);
            statement.setInt("pageSize", pageSize);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                OrderGrab task = new OrderGrab();

                task.setDriverID(set.getInt("DriverID"));
                task.setDriverName(set.getString("DriverName"));
                task.setDLicense(set.getString("DLicense"));

                task.setCarID(set.getInt("CarID"));
                task.setSystemNo(set.getString("SystemNo"));
                task.setCarNumber(set.getString("CarNumber"));

                task.setTaskID(set.getInt("TaskID"));
                task.setTaskSerial(set.getString("TaskSerial"));
//                task.setGoodsType(set.getString("GoodsType"));
                OrderDetailController controller1 = new OrderDetailController();
                task.setGoodsType(JSONObject.toJSONString(controller1.getOrderGoodsInfo(task.getTaskID(), 1, 99)));
                task.setTaskStatus(set.getInt("TaskStatus"));
                task.setPriceModel(set.getInt("PriceModel"));
                task.setFactoryTime(set.getInt("FactoryTime"));
                task.setInFactoryStatus(set.getInt("InFactoryStatus"));
                task.setTaskType(set.getInt("TaskType"));
                task.setStartPlace(set.getInt("StartPlace"));
                task.setStartPlaceName(set.getString("StartPlaceName"));
                task.setStartPlaceLatitude(set.getFloat("StartPlaceLatitude"));
                task.setStartPlaceLongitude(set.getFloat("StartPlaceLongitude"));
                task.setEndPlace(set.getInt("EndPlace"));
                task.setEndPlaceName(set.getString("EndPlaceName"));
                task.setEndPlaceLatitude(set.getFloat("EndPlaceLatitude"));
                task.setEndPlaceLongitude(set.getFloat("EndPlaceLongitude"));

                task.setSendTime(set.getString("SendTime"));
                task.setOPrice(set.getFloat("OPrice"));
                task.setGrabTimes(set.getInt("GrabTimes"));
                task.setGrabTime(set.getString("GrabTime"));
                task.setRevokeStatus(set.getInt("RevokeStatus"));
                task.setFailReason(set.getString("FailReason"));
                task.setAuditOpinion(set.getString("AuditOpinion"));

                result.add(task);
            }
        } catch (Exception e) {
            Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * 获取经销商所有订单前两条
     *
     * @param distributorID
     * @return
     */
    public ArrayList<OrderGrab> getOrderGrabDisTopTwo(int distributorID) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        ArrayList<OrderGrab> result = new ArrayList<>();
        try {
            statement = conn.prepareCall("{call tbOrderGrabGetDisTopTwo(?)}");
            statement.setInt("_distributorID", distributorID);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                OrderGrab task = new OrderGrab();

                task.setEnterpriseID(set.getString("EnterpriseID"));
                task.setEnterpriseName(set.getString("EnterpriseName"));
                task.setCarrierName(set.getString("EnterpriseCarrierName"));
//                task.setEnterpriseType(set.getInt("EnterpriseType"));
//                task.setEnterpriseAddress(set.getString("EnterpriseAddress"));
//                task.setEnterpriseContact(set.getString("EnterpriseContact"));
//                task.setEnterprisePhoneNumber(set.getString("EnterprisePhoneNumber"));

                task.setDriverID(set.getInt("DriverID"));
//                task.setDriverName(set.getString("DriverName"));
//                task.setDLicense(set.getString("DLicense"));
//                task.setDPhoneNumber(set.getString("DPhoneNumber"));

                task.setCarID(set.getInt("CarID"));
                task.setSystemNo(set.getString("SystemNo"));
                task.setCarNumber(set.getString("CarNumber"));
//                task.setCarType(set.getString("CarType"));
//                task.setCarWeight(set.getString("CarWeight"));

                task.setTaskID(set.getInt("TaskID"));
                task.setTaskSerial(set.getString("TaskSerial"));
//                task.setGoodsType(set.getString("GoodsType"));
                OrderDetailController controller1 = new OrderDetailController();
                task.setGoodsType(JSONObject.toJSONString(controller1.getOrderGoodsInfo(task.getTaskID(), 1, 99)));
//                task.setPrice(set.getFloat("Price"));
//                task.setTimeCountDown(set.getString("TimeCountDown"));
                task.setFactoryTime(set.getInt("FactoryTime"));
//                task.setTaskType(set.getInt("TaskType"));
                task.setStartPlace(set.getInt("StartPlace"));
                task.setStartPlaceName(set.getString("StartPlaceName"));
                task.setEndPlace(set.getInt("EndPlace"));
                task.setEndPlaceName(set.getString("EndPlaceName"));
                task.setTLenth(set.getFloat("TLenth"));
//                task.setTaskCarNum(set.getInt("TaskCarNum"));
//                task.setTaskTonnage(set.getFloat("TaskTonnage"));
//                task.setSendTime(set.getString("SendTime"));
//                task.setCarType(set.getString("CarType"));

                task.setOPrice(set.getFloat("OPrice"));
                task.setGrabTimes(set.getInt("GrabTimes"));
                task.setGrabStatus(set.getInt("GrabStatus"));
                task.setGrabTime(set.getString("GrabTime"));
                task.setFailReason(set.getString("FailReason"));

                result.add(task);
            }
        } catch (Exception e) {
            Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * 获取经销商抢单记录
     *
     * @param distributorID
     * @param taskID
     * @param taskSerial
     * @param carID
     * @param carNumber
     * @param driverID
     * @param driverName
     * @param sendTime
     * @param nowPage
     * @param pageSize
     * @return
     */
    public ArrayList<OrderGrabDis> getDisOrderGrabList(int distributorID, int taskID, String taskSerial, int carID, String carNumber,
            int driverID, String driverName, String sendTime, int nowPage, int pageSize) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        ArrayList<OrderGrabDis> result = new ArrayList<>();
        try {
            statement = conn.prepareCall("{call tbOrderGrabDisGet(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            statement.setInt("_distributorID", distributorID);
            statement.setInt("_driverID", driverID);
            statement.setString("_driverName", driverName);
            statement.setInt("_carID", carID);
            statement.setString("_carNumber", carNumber);
            statement.setInt("_taskID", taskID);
            statement.setString("_taskSerial", taskSerial);
            statement.setString("_sendTime", sendTime);
            statement.setInt("nowPage", nowPage);
            statement.setInt("pageSize", pageSize);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                OrderGrabDis grab = new OrderGrabDis();

                grab.setDriverID(set.getInt("DriverID"));
                grab.setDriverName(set.getString("DriverName"));
                grab.setdLicense(set.getString("DLicense"));
                grab.setdPhoneNumber(set.getString("DPhoneNumber"));

                grab.setCarID(set.getInt("CarID"));
                grab.setSystemNo(set.getString("SystemNo"));
                grab.setCarNumber(set.getString("CarNumber"));

                grab.setTaskID(set.getInt("TaskID"));
                grab.setTaskSerial(set.getString("TaskSerial"));
                grab.setGrabStatus(set.getInt("GrabStatus"));
                grab.setRevokeStatus(set.getInt("RevokeStatus"));
                grab.setInFactoryStatus(set.getInt("InFactoryStatus"));

                result.add(grab);
            }
        } catch (Exception e) {
            Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderGrabController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
}
