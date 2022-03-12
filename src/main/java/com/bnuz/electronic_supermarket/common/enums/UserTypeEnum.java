package com.bnuz.electronic_supermarket.common.enums;

public enum UserTypeEnum {
    ADMIN("管理员",1024),
    BUSINESSMAN("商家",0),
    USER("用户",1),
    DELIVERYSTAFF("配送员",2);

    private String name;
    private int index;

    UserTypeEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for(UserTypeEnum e:UserTypeEnum.values()) {
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
