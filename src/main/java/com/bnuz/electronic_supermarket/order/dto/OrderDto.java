/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: orderDto
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/20 11:37
 * Description: input orderDto
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.order.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "NotPay(\"订单确认未支付\",2),\n" +
        "    Payed(\"订单确认已支付\",1),\n" +
        "    Cancel(\"订单取消\",0);")
public class OrderDto {
    @ApiModelProperty(value = "商店ID",required = true)
    @NotBlank
    private String storeId;
    @ApiModelProperty(value = "商品ID和对应数量",required = true)
    private HashMap<String,Integer> products;
    @ApiModelProperty("总价")
    private Float totalPrice;
    @ApiModelProperty("支付方式(微信，支付宝，银行卡)")
    private String payMethod;
    @ApiModelProperty("支付单号")
    private String payId;
    @ApiModelProperty("支付流水号")
    private String payNumber;
    @ApiModelProperty("配送方式")
    private String diliveryMethod;
}