package com.hanclouds.cloud.sample;


/**
 * 推送服务返回给用户的计算结果数据
 * @author: hanclouds
 * @create: 2019-10-31
 **/
public class CalculateData {
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
     * 时间戳
     */
    private long time;
    /**
     * 数据流名称
     */
    private String stream;
    /**
     * 数据类型
     */
    private int type;
    /**
     * 数据值
     */
    private Object data;
    /**
     * 从低位开始，每一位为真表示不同的推送类型，可任意排列组合
     * 0001  云接入
     * 0010  mqtt
     */
    private int pushType;

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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getPushType() {
        return pushType;
    }

    public void setPushType(int pushType) {
        this.pushType = pushType;
    }

    @Override
    public String toString() {
        return "CalculateData{" +
                "userKey='" + userKey + '\'' +
                ", productKey='" + productKey + '\'' +
                ", deviceKey='" + deviceKey + '\'' +
                ", time=" + time +
                ", stream='" + stream + '\'' +
                ", type=" + type +
                ", data=" + data +
                ", pushType=" + pushType +
                '}';
    }
}
