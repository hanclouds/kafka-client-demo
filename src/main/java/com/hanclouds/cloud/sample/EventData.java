package com.hanclouds.cloud.sample;


/**
 * 推送服务返回给用户的设备事件
 * @author: hanclouds
 * @create: 2019-10-29 16:31
 **/
public class EventData {
    /**
     * userKey
     */
    private String userKey;

    /**
     * productKey
     */
    private String productKey;

    /**
     * deviceKey
     */
    private String deviceKey;

    /**
     * 数据值
     */
    private Object data;
    /**
     * 时间戳
     */
    private long time;

    /**
     * 事件类型(1: info （信息）2: Alert （告警）3: Error（故障）)
     */
    private Integer eventType;

    /**
     * 输出数据
     */
    private Object output;
    /**
     * 命令标识符
     */
    private String identifier;


    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public Object getOutput() {
        return output;
    }

    public void setOutput(Object output) {
        this.output = output;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "EventData{" +
                "userKey='" + userKey + '\'' +
                ", productKey='" + productKey + '\'' +
                ", deviceKey='" + deviceKey + '\'' +
                ", data=" + data +
                ", time=" + time +
                ", eventType=" + eventType +
                ", output=" + output +
                ", identifier='" + identifier + '\'' +
                '}';
    }
}
