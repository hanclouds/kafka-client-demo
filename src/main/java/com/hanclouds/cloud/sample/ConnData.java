package com.hanclouds.cloud.sample;

/**
 * 推送服务返回给用户的设备日志
 *
 * @author hanclouds
 * @date 2018/9/3
 */
public class ConnData {
    /**
     * deviceKey
     */
    private String deviceKey;
    /**
     * 设备事件
     */
    private int event;
    /**
     * productKey
     */
    private String productKey;
    /**
     * 事件时间
     */
    private long time;
    /**
     * userKey
     */
    private String userKey;


    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }


    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    @Override
    public String toString() {
        return "ConnData{" +
                "deviceKey='" + deviceKey + '\'' +
                ", event=" + event +
                ", productKey='" + productKey + '\'' +
                ", time=" + time +
                ", userKey='" + userKey + '\'' +
                '}';
    }
}
