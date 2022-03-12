package com.bnuz.electronic_supermarket.user.enums;

import lombok.Getter;

@Getter
public enum UserStateEnum {
    UNUSED("注销",0),
    USING("使用中",1),
    ERROR("异常",2);

    private String name;
    private int index;

    UserStateEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for(UserStateEnum e:UserStateEnum.values()) {
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
