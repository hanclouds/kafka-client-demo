package com.hanclouds.cloud.sample;

/**
 * 云接入返回给用户的设备命令
 *
 * @author hanclouds
 * @date 2018/9/3
 */
public class CmdData {
    /**
     * 命令下发目标设备所属的userKey
     */
    private String userKey;
    /**
     * 命令下发目标设备所属的productKey
     */
    private String productKey;
    /**
     * 命令下发目标的deviceKey
     */
    private String deviceKey;
    /**
     * 命令Id，每个命令都有一个独一无二的命令Id
     */
    private String cmdId;
    /**
     * 命令的状态，具体编码参见 CommandStateEnum
     */
    private int state;
    /**
     * 命令状态发生的时间
     */
    private long time;
    /**
     * 命令到期时间
     */
    private long timeDue;

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

    public String getCmdId() {
        return cmdId;
    }

    public void setCmdId(String cmdId) {
        this.cmdId = cmdId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTimeDue() {
        return timeDue;
    }

    public void setTimeDue(long timeDue) {
        this.timeDue = timeDue;
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
        return "CmdData{" +
                "userKey='" + userKey + '\'' +
                ", productKey='" + productKey + '\'' +
                ", deviceKey='" + deviceKey + '\'' +
                ", cmdId='" + cmdId + '\'' +
                ", state=" + state +
                ", time=" + time +
                ", timeDue=" + timeDue +
                ", type=" + type +
                ", data=" + data +
                '}';
    }
}
