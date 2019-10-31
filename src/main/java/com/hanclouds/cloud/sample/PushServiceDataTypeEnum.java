package com.hanclouds.cloud.sample;

import com.alibaba.fastjson.JSON;

/**
 *  推送服务数据类型
 * @author: hanclouds
 * @create: 2019-09-24 14:46
 **/
public enum PushServiceDataTypeEnum {
    /**
     * 数据类型
     */
    DEVICE_DATA(1,DeviceData.class),
    /**
     * 日志类型
     */
    DEVICE_CONN(2,ConnData.class),
    /**
     * 命令类型
     */
    DEVICE_CMD(3,CmdData.class),
    /**
     * 大数据计算结果
     */
    ACCESS_CALCULATE(4,CalculateData.class),
    /**
     * 事件类型
     */
    ACCESS_EVENT(5,EventData.class);


    private final int value;
    private final Class<?> targetClass;

     PushServiceDataTypeEnum(int value,Class<?> targetClass) {
        this.value = value;
        this.targetClass = targetClass;
    }

    public Integer intValue() {
        return value;
    }

    public static Object getJsonDataByType(int type,String value){
         for (PushServiceDataTypeEnum pushServiceDataType: PushServiceDataTypeEnum.values()){
             if (pushServiceDataType.value == type){
                   return JSON.parseObject(value, pushServiceDataType.targetClass);
             }
         }
         return null;
    }
}
