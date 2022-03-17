package com.bnuz.electronic_supermarket.common.enums;

import com.bnuz.electronic_supermarket.user.enums.UserStateEnum;
import lombok.Getter;

@Getter
public enum StateEnum {
    UNUSED("注销",0),
    USING("使用中",1),
    ERROR("异常",2);

    private String name;
    private int index;

    StateEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for(StateEnum e:StateEnum.values()) {
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
