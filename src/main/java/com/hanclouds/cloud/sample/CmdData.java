package com.hanclouds.cloud.sample;


/**
 * 推送服务返回给用户的设备命令
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
     * 命令状态发生的时间
     */
    private long time;

    /**
     * 命令的有效截止时间
     */
    private long timeExpire;

    /**
     * 命令Id，每个命令都有一个独一无二的命令Id
     */
    private String cmdId;

    /**
     * 命令标识符
     */
    private String identifier;

    /**
     * 命令的状态，具体编码参见 {@link }
     */
    private int state;

    /**
     * 输入数据类型
     */
    private int inputType;

    /**
     * 输入数据
     */
    private Object input;

    /**
     * 输出数据
     */
    private String output;

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

    public long getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(long timeExpire) {
        this.timeExpire = timeExpire;
    }

    public String getCmdId() {
        return cmdId;
    }

    public void setCmdId(String cmdId) {
        this.cmdId = cmdId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getInputType() {
        return inputType;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }

    public Object getInput() {
        return input;
    }

    public void setInput(Object input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "CmdData{" +
                "userKey='" + userKey + '\'' +
                ", productKey='" + productKey + '\'' +
                ", deviceKey='" + deviceKey + '\'' +
                ", time=" + time +
                ", timeExpire=" + timeExpire +
                ", cmdId='" + cmdId + '\'' +
                ", identifier='" + identifier + '\'' +
                ", state=" + state +
                ", inputType=" + inputType +
                ", input=" + input +
                ", output='" + output + '\'' +
                '}';
    }
}
