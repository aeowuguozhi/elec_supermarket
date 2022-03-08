package com.bnuz.electronic_supermarker.common.enums;

import lombok.Getter;

@Getter
public enum SysResultEnum {
    SUCCESS("请求成功",200),      //一般用于GET与POST请求
    SYS_ERROR("服务器错误",500),
    Created("已创建",201),
    Client_ERROR("客户端错误",400);



    private String name;
    private int index;

    SysResultEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for(SysResultEnum e:SysResultEnum.values()) {
            if(e.getIndex() == index) {
                return e.name;
            }
        }
        return null;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setName(String name) {
        this.name = name;
    }
}
