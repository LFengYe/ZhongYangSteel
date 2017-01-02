/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.util;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.google.gson.JsonObject;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author LFeng
 */
public class PushUnits {

    protected static final Logger LOG = LoggerFactory.getLogger(PushUnits.class);

    private static final String appKey = "024335bbb7ecae6a66a3e36b";
    private static final String masterSecret = "b6e0db9714a06e1fbb4bf017";

    /**
     * 使用标签发送通知
     *
     * @param tag 发送目标
     * @param title 标题
     * @param type 类型
     * @param data 数据
     */
    public static void pushNotifation(String tag, String title, String type, JsonObject data) {
        JPushClient jpushClient = new JPushClient(masterSecret, appKey);
        try {
            PushResult result = jpushClient.sendPush(buildNotification_android_and_ios_withTag(tag, title, type, data));
            LOG.info("Got result - " + result);
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
        } catch (Exception e) {
            LOG.info(e.toString());
        }
    }
    
    /**
     * 使用别名发送通知
     * @param alias
     * @param title
     * @param type
     * @param data 
     */
    public static void pushNotifationWithAlias(String alias, String title, String type, JsonObject data) {
        JPushClient jpushClient = new JPushClient(masterSecret, appKey);
        try {
            PushResult result = jpushClient.sendPush(buildNotification_android_and_ios_withAlias(alias, title, type, data));
            LOG.info("Got result - " + result);
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
        } catch (Exception e) {
            LOG.info(e.toString());
        }
    }
    
    public static void pushNotifationWithAlias(Collection<String> alias, String title, String type, JsonObject data) {
        JPushClient jpushClient = new JPushClient(masterSecret, appKey);
        try {
            PushResult result = jpushClient.sendPush(buildNotification_android_and_ios_withAlias(alias, title, type, data));
            LOG.info("Got result - " + result);
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
        } catch (Exception e) {
            LOG.info(e.toString());
        }
    }
    
    /**
     * 发送消息
     * @param tag
     * @param title
     * @param type
     * @param data 
     */
    public static void pushMessage(String tag, String title, String type, String data) {
        JPushClient jpushClient = new JPushClient(masterSecret, appKey);
        try {
            PushResult result = jpushClient.sendPush(buildMessage_android_and_ios_withTag(tag, title, type, data));
            LOG.info("Got result - " + result);
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);

        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
        }
    }
    
    /**
     * 使用别名发送消息
     * @param alias
     * @param title
     * @param type
     * @param data 
     */
    public static void pushMessageWithAlias(String alias, String title, String type, String data) {
        JPushClient jpushClient = new JPushClient(masterSecret, appKey);
        try {
            PushResult result = jpushClient.sendPush(buildMessage_android_and_ios_withAlias(alias, title, type, data));
            LOG.info("Got result - " + result);
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);

        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
        }
    }

    /**
     * 按标签给Android和IOS设备发送通知, 
     * 并且不保留离线消息, 只有推送当前在线的用户可以收到。
     * @param tags 标签
     * @param title 标题
     * @param type 类型
     * @param data 数据
     * @return
     */
    public static PushPayload buildNotification_android_and_ios_withTag(String tags, String title, String type, JsonObject data) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.tag(tags))
                .setOptions(Options.newBuilder().setTimeToLive(0).build())
                .setNotification(Notification.newBuilder()
                        .setAlert(title)
                        .addPlatformNotification(
                                AndroidNotification.newBuilder().addExtra("type", type).addExtra("data", data).build()
                        )
                        .addPlatformNotification(
                                IosNotification.newBuilder().addExtra("type", type).addExtra("data", data).incrBadge(1).build()
                        )
                        .build())
                .build();
    }
    
    /**
     * 按别名给Android和IOS设备发送通知
     * @param alias 别名
     * @param title 标题
     * @param type 类型
     * @param data 数据
     * @return
     */
    public static PushPayload buildNotification_android_and_ios_withAlias(String alias, String title, String type, JsonObject data) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .setAlert(title)
                        .addPlatformNotification(
                                AndroidNotification.newBuilder().addExtra("type", type).addExtra("data", data).build()
                        )
                        .addPlatformNotification(
                                IosNotification.newBuilder().addExtra("type", type).addExtra("data", data).incrBadge(1).build()
                        )
                        .build())
                .build();
    }
    
    public static PushPayload buildNotification_android_and_ios_withAlias(Collection<String> alias, String title, String type, JsonObject data) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .setAlert(title)
                        .addPlatformNotification(
                                AndroidNotification.newBuilder().addExtra("type", type).addExtra("data", data).build()
                        )
                        .addPlatformNotification(
                                IosNotification.newBuilder().addExtra("type", type).addExtra("data", data).incrBadge(1).build()
                        )
                        .build())
                .build();
    }

    /**
     * 按标签给android和IOS设备发送消息
     * @param tag
     * @param title
     * @param type
     * @param data
     * @return 
     */
    public static PushPayload buildMessage_android_and_ios_withTag(String tag, String title, String type, String data) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.tag(tag))
                .setMessage(
                        Message.newBuilder().setTitle(title).setMsgContent(data).setContentType(type).build())
                .build();
    }
    
    /**
     * 按别名给android和IOS设备发送消息
     * @param alias
     * @param title
     * @param type
     * @param data
     * @return 
     */
    public static PushPayload buildMessage_android_and_ios_withAlias(String alias, String title, String type, String data) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(alias))
                .setMessage(
                        Message.newBuilder().setTitle(title).setMsgContent(data).setContentType(type).build())
                .build();
    }
}
