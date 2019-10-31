package com.hanclouds.cloud.sample;


/**
 * 推送服务新版数据格式
 *
 * @author hanclouds
 * @date 2018/9/3
 */
public class PushServiceData {
    private int dataType;
    private String data;

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
