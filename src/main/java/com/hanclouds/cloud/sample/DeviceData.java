package com.hanclouds.cloud.sample;

/**
 * 云接入返回给用户的设备数据
 *
 * @author hanclouds
 * @date 2018/9/3
 */
public class DeviceData {
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
     * 数据值，视类型不同，形式有所不同
     * <p>
     * json: 对象{...}
     * <p>
     * int: 整数
     * <p>
     * double: 浮点数
     * <p>
     * string: 字符串
     * <p>
     * bin: 二进制字节数组的Base64编码后的字符串
     */
    private Object data;


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

    @Override
    public String toString() {
        return "DeviceData{" +
                "userKey='" + userKey + '\'' +
                ", productKey='" + productKey + '\'' +
                ", deviceKey='" + deviceKey + '\'' +
                ", time=" + time +
                ", stream='" + stream + '\'' +
                ", type=" + type +
                ", data=" + data +
                '}';
    }
}
