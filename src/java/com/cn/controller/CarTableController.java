/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.controller;

import com.alibaba.fastjson.JSON;
import com.cn.entity.CarLocation;
import com.cn.entity.CarTable;
import com.cn.entity.TempLocalModel;
import com.cn.util.DatabaseOpt;
import com.cn.util.RedisAPI;
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
public class CarTableController {

    /**
     * 获取车辆列表
     *
     * @param carID
     * @param userID
     * @param carType
     * @param systemNo
     * @param carNumber
     * @param driverID
     * @param applyStatus
     * @param nowPage
     * @param pageSize
     * @return
     */
    public ArrayList<CarTable> getCarList(int carID, int userID, String carType, String systemNo, String carNumber, int driverID, int applyStatus, int nowPage, int pageSize) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        ArrayList<CarTable> result = new ArrayList<>();
        try {
            statement = conn.prepareCall("{call tbCarTableGet(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            statement.setInt("_carID", carID);
            statement.setInt("_userID", userID);
            statement.setString("_carType", carType);
            statement.setString("_systemNo", systemNo);
            statement.setString("_carNumber", carNumber);
            statement.setInt("_driverID", driverID);
            statement.setInt("_ApplyStatus", applyStatus);
            statement.setInt("nowPage", nowPage);
            statement.setInt("pageSize", pageSize);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                CarTable car = new CarTable();
                car.setCarID(set.getInt("CarID"));
                car.setCarNumber(set.getString("CarNumber"));
                car.setSystemNo(set.getString("SystemNo"));
                car.setEnterpriseID(set.getInt("EnterpriseID"));
                car.setDriverID(set.getInt("DriverID"));
                car.setDriverName(set.getString("DriverName"));

                car.setCarType(set.getString("CarType"));
                car.setCarWeight(set.getFloat("CarWeight"));
                car.setCarLength(set.getFloat("CarLength"));
                car.setApplyStatus(set.getInt("ApplyStatus"));
                car.setAuthStatus(set.getInt("AuthenStatus"));
                
                /*
                CarTableController controller = new CarTableController();
                CarLocation location = controller.getCarLocationWithID(car.getCarID());
                car.setLatitude(location.getLatitude());
                car.setLongitude(location.getLongitude());
                */
//                car.setMaintainDate(set.getString("MaintainDate"));
//                car.setAnnualDate(set.getString("AnnualDate"));
//                car.setMaintainExpireDate(set.getString("MaintainExpireDate"));
                car.setRegisterTime(set.getString("RegisterTime"));
                car.setInsuranceExpireTime(set.getString("InsuranceExpireTime"));
                car.setCarImagepath1(set.getString("CarImagePath1"));
                car.setCarImagepath2(set.getString("CarImagePath2"));
                car.setCarImagepath3(set.getString("CarImagePath3"));
                car.setRemark(set.getString("Remark"));

                result.add(car);
            }
        } catch (Exception e) {
            Logger.getLogger(CarTableController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CarTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public ArrayList<CarTable> getCarList(int carID, int userID, String carType, String systemNo, String carNumber, int driverID, int nowPage, int pageSize) {
        return getCarList(carID, userID, carType, systemNo, carNumber, driverID, -1, nowPage, pageSize);
    }
    
    public ArrayList<CarTable> getApplyDeleteCarList(int applyStatus, int nowPage, int pageSize) {
        return getCarList(-1, -1, null, null, null, -1, 1, nowPage, pageSize);
    }
    
    /**
     * 根据用户ID获取该用户下所属车辆列表
     *
     * @param userID
     * @param nowPage
     * @param pageSize
     * @return
     */
    public ArrayList<CarTable> getCarListWithUserID(int userID, int nowPage, int pageSize) {
        return getCarList(-1, userID, null, null, null, -1, nowPage, pageSize);
    }

    /**
     * 根据车辆ID, 获取车辆详细信息
     *
     * @param carID
     * @return
     */
    public CarTable getCarDetailWithCarID(int carID) {
        ArrayList<CarTable> result = getCarList(carID, -1, null, null, null, -1, 1, 11);
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    /**
     * 获取抢单一次的车辆信息
     *
     * @param userID
     * @param taskID
     * @return
     */
    public ArrayList<CarTable> getGradedOnceCar(int userID, int taskID) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        ArrayList<CarTable> result = new ArrayList<>();
        try {
            statement = conn.prepareCall("{call tbOrderGrabGetGrabedOnceCar(?, ?, ?)}");
            statement.setInt("_taskID", taskID);
            statement.setInt("_userID", userID);
            statement.registerOutParameter("result", Types.INTEGER);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                CarTable car = new CarTable();
                car.setCarID(set.getInt("CarID"));
                car.setCarNumber(set.getString("CarNumber"));
                car.setSystemNo(set.getString("SystemNo"));
                /*
                 car.setEnterpriseID(set.getInt("EnterpriseID"));
                 car.setDriverID(set.getInt("DriverID"));
                 car.setDriverName(set.getString("DriverName"));

                 car.setCarType(set.getString("CarType"));
                 car.setCarWeight(set.getFloat("CarWeight"));
                 car.setCarLength(set.getFloat("CarLength"));
                 car.setMaintainDate(set.getString("MaintainDate"));
                 car.setAnnualDate(set.getString("AnnualDate"));
                 car.setMaintainExpireDate(set.getString("MaintainExpireDate"));
                 car.setCarImagepath1(set.getString("CarImagePath1"));
                 car.setCarImagepath2(set.getString("CarImagePath2"));
                 car.setCarImagepath3(set.getString("CarImagePath3"));
                 car.setRemark(set.getString("Remark"));
                 */
                result.add(car);
            }
        } catch (Exception e) {
            Logger.getLogger(CarTableController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CarTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * 添加车辆信息
     *
     * @param systemNo
     * @param carNumber
     * @param carType
     * @param carWeight
     * @param carLength
     * @param insuranceExpireDate
     * @param carImagePath1
     * @param carImagePath2
     * @param carImagePath3
     * @param enterpriseID
     * @param driverID
     * @return
     */
    public AddCarResult addCarTable(String systemNo, String carNumber, String carType, float carWeight, float carLength, String insuranceExpireDate,
            String carImagePath1, String carImagePath2, String carImagePath3, int enterpriseID, int driverID) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        AddCarResult result = new AddCarResult();
        result.result = -1;
        try {
            statement = conn.prepareCall("{call tbCartableAdd(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            statement.setString("_systemNo", systemNo);
            statement.setString("_carNumber", carNumber);
            statement.setInt("_enterpriseID", enterpriseID);
            statement.setString("_carType", carType);
            statement.setFloat("_carWeight", carWeight);
            statement.setFloat("_carLength", carLength);
//            statement.setString("_maintainDate", maintainDate);
//            statement.setString("_annualDate", annualDate);
//            statement.setString("_maintainExpireDate", maintainExpireDate);
            statement.setString("_insuranceExpireDate", insuranceExpireDate);
            statement.setString("_carImagePath1", carImagePath1);
            statement.setString("_carImagePath2", carImagePath2);
            statement.setString("_carImagePath3", carImagePath3);
            statement.setInt("_driverID", driverID);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.execute();
            
            result.result = statement.getInt("result");
            if (result.result == 1) {
                ResultSet set = statement.getResultSet();
                while (set.next()) {
                    result.registerTime = set.getString("RegisterTime");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CarTableController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CarTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * 认证车辆信息
     * @param carID
     * @param carType
     * @param carWeight
     * @param carLength
     * @return 
     */
    public int authCarTable(int carID, String carType, float carWeight, float carLength) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbCarTableAuth(?, ?, ?, ?, ?)}");
            statement.setInt("_carID", carID);
            statement.setString("_carType", carType);
            statement.setFloat("_carWeight", carWeight);
            statement.setFloat("_carLength", carLength);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            Logger.getLogger(CarTableController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CarTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 更新车辆信息
     *
     * @param carID
     * @param carType
     * @param carWeight
     * @param carLength
     * @param insuranceExpireDate
     * @param carImagePath1
     * @param carImagePath2
     * @param carImagePath3
     * @param enterpriseID
     * @param driverID
     * @return
     */
    public int updateCarTable(int carID, String carType, float carWeight, float carLength, String insuranceExpireDate,
            String carImagePath1, String carImagePath2, String carImagePath3, int enterpriseID, int driverID) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbCarTableUpdate(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            statement.setInt("_carID", carID);
            statement.setInt("_enterpriseID", enterpriseID);
            statement.setString("_carType", carType);
            statement.setFloat("_carWeight", carWeight);
            statement.setFloat("_carLength", carLength);
            statement.setString("_insuranceExpireDate", insuranceExpireDate);
            statement.setString("_carImagePath1", carImagePath1);
            statement.setString("_carImagePath2", carImagePath2);
            statement.setString("_carImagePath3", carImagePath3);
            statement.setInt("_driverID", driverID);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            Logger.getLogger(CarTableController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CarTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * 车辆绑定默认司机
     *
     * @param carID
     * @param driverID
     * @return
     */
    public int bindDriver(int carID, int driverID) {
        return updateCarTable(carID, null, -1, -1, null, null, null, null, -1, driverID);
    }

    /**
     * 申请删除车辆信息
     * @param carID
     * @return 
     */
    public int applyDeleteCarTable(int carID) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbCartableApplyDelete(?, ?)}");
            statement.setInt("_carID", carID);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            Logger.getLogger(CarTableController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CarTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 删除车辆信息
     *
     * @param carID
     * @return
     */
    public int deleteCarTable(int carID) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbCarTableDelete(?, ?)}");
            statement.setInt("_carID", carID);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            Logger.getLogger(CarTableController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CarTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * 修改车辆使用状态
     * @param carID
     * @param useStatus
     * @return 
     */
    public int updateCarUseStatus(int carID, int useStatus) {
        DatabaseOpt opt = new DatabaseOpt();
        Connection conn = opt.getConnect();
        CallableStatement statement = null;
        int result = -1;
        try {
            statement = conn.prepareCall("{call tbCarTableUseStatusUpdate(?, ?, ?)}");
            statement.setInt("_carID", carID);
            statement.setInt("_useStatus", useStatus);
            statement.registerOutParameter("result", Types.INTEGER);
            statement.executeUpdate();
            result = statement.getInt("result");
        } catch (SQLException ex) {
            Logger.getLogger(CarTableController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CarTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * 根据车辆ID, 获取车辆位置信息
     *
     * @param carID
     * @return
     */
    public CarLocation getCarLocationWithID(int carID) {
//        DatabaseOpt databaseOpt = new DatabaseOpt();
//        Jedis jedis = databaseOpt.getRedisConnect();
        CarTable car = getCarDetailWithCarID(carID);
        CarLocation location = new CarLocation();
        String tempJson = RedisAPI.get(car.getSystemNo());
        location.setCarID(car.getCarID());
        location.setCarNO(car.getCarNumber());
        location.setSystemNo(car.getSystemNo());
        if (null != tempJson) {
            String jsonStr = tempJson.substring(1, tempJson.length() - 1).replace("\\", "");
            TempLocalModel temp = JSON.parseObject(jsonStr, TempLocalModel.class);
            location.setAngle(temp.getAngle());
            location.setVelocity(temp.getVelocity());
            location.setLatitude(temp.getLatitude());
            location.setLongitude(temp.getLongitude());
            location.setGpsTime(temp.getGpstime());
        } else {
            location.setAngle(0);
            location.setVelocity(0);
            location.setLatitude(0);
            location.setLongitude(0);
        }
        return location;
    }

    /**
     * 根据车辆系统编号, 获取车辆位置
     *
     * @param systemNo
     * @return
     */
    public CarLocation getCarLocationWithSystemNo(String systemNo) {
//        DatabaseOpt databaseOpt = new DatabaseOpt();
//        Jedis jedis = databaseOpt.getRedisConnect();
        CarLocation location = new CarLocation();
        String tempJson = RedisAPI.get(systemNo);
        location.setSystemNo(systemNo);
        if (null != tempJson) {
            String jsonStr = tempJson.substring(1, tempJson.length() - 1).replace("\\", "");
            TempLocalModel temp = JSON.parseObject(jsonStr, TempLocalModel.class);
            location.setAngle(temp.getAngle());
            location.setVelocity(temp.getVelocity());
            location.setLatitude(temp.getLatitude());
            location.setLongitude(temp.getLongitude());
            location.setGpsTime(temp.getGpstime());
        } else {
            location.setAngle(0);
            location.setVelocity(0);
            location.setLatitude(0);
            location.setLongitude(0);
        }
        return location;
    }

    /**
     * 获取指定车辆列表的车辆位置数据
     *
     * @param carList
     * @return
     */
    public ArrayList<CarLocation> getCarlistLocation(ArrayList<CarTable> carList) {
        if (carList.size() > 0) {
            ArrayList<CarLocation> result = new ArrayList<>();
//            DatabaseOpt databaseOpt = new DatabaseOpt();
//            Jedis jedis = databaseOpt.getRedisConnect();
            for (CarTable car : carList) {
                CarLocation location = new CarLocation();
                location.setCarID(car.getCarID());
                location.setCarNO(car.getCarNumber());
                location.setSystemNo(car.getSystemNo());

                String tempJson = RedisAPI.get(car.getSystemNo());
                //System.out.println(car.getSystemNo() + ":" + jsonStr);
                if (null != tempJson) {
                    String jsonStr = tempJson.substring(1, tempJson.length() - 1).replace("\\", "");
                    TempLocalModel temp = JSON.parseObject(jsonStr, TempLocalModel.class);
                    location.setAngle(temp.getAngle());
                    location.setVelocity(temp.getVelocity());
                    location.setLatitude(temp.getLatitude());
                    location.setLongitude(temp.getLongitude());
                    location.setGpsTime(temp.getGpstime());
                }
                result.add(location);
            }
            return result;
        }
        return null;
    }

    public class AddCarResult {

        public int result;
        public String registerTime;
    }
}
