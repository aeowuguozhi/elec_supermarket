package com.bnuz.electronic_supermarket.product.enums;

import com.bnuz.electronic_supermarket.common.enums.StateEnum;
import io.swagger.models.auth.In;

public enum ProductStateEnum {
    ONSHELF("上架",1),
    OFFSHELF("下架",0),
    Promotion("促销商品",2);
    private String name;
    private int index;

    ProductStateEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for(ProductStateEnum e:ProductStateEnum.values()) {
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
