package com.hanclouds.cloud.sample;

/**
 * @program: cloud-access
 * @description:
 * @author: liujj
 * @create: 2019-09-24 14:46
 **/
public enum PushServiceDataTypeEnum {
    /**
     * 数据类型
     */
    DEVICE_DATA(1),
    /**
     * 事件类型
     */
    DEVICE_CONN(2),
    /**
     * 命令类型
     */
    DEVICE_CMD(3);


    private final int value;

     PushServiceDataTypeEnum(int value) {
        this.value = value;
    }

    public Integer intValue() {
        return value;
    }
}
