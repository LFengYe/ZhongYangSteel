/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.task;

import com.cn.controller.OrderGrabController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author LFeng
 */
public class ScanTimeoutInFactory implements Runnable {

    protected static final Logger LOG = LoggerFactory.getLogger(ScanTimeoutInFactory.class);

    public ScanTimeoutInFactory() {
    }

    @Override
    public void run() {
        try {
            OrderGrabController controller = new OrderGrabController();
            int result = controller.orderGrabTimeout();
            if (result == 0) {
//                LOG.info("进厂超时扫描成功!");
            } else {
                LOG.info("进厂超时扫描出错", result);
            }
        } catch (Exception e) {
            LOG.info("进厂超时扫描异常", e);
        }
    }

}
