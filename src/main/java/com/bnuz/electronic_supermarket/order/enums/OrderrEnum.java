package com.bnuz.electronic_supermarket.order.enums;

import com.bnuz.electronic_supermarket.common.enums.SysResultEnum;
import lombok.Data;
import lombok.Getter;

/**
 *  定时任务，用户确认订单后（order.create，set state NotPay）进入支付页面，
 *  1,完成支付了生成订单 2,退出支付页面订单24小时后取消订单（delete order）:
 *                        set orderr.state =  from orderr where orderr.state = 0 AND currentTime - createTime > 24h
 */
@Getter
public enum OrderrEnum {
    NotPay("订单确认未支付",2),
    Payed("订单确认已支付",1),
    Cancel("订单取消",0),
    FINISHED("订单已完成(用户已收货)",3);

    private String name;
    private int index;

    OrderrEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for(OrderrEnum e:OrderrEnum.values()) {
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
