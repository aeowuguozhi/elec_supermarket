/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: Orderr
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/20 10:29
 * Description: 订单
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.common.javaBean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * Lombok mybatis-plus Api Validation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("orderr")
@ApiModel("订单模型")
public class Orderr {
    @ApiModelProperty("订单ID")
    private String id;
    @TableField("storeId")
    @ApiModelProperty(value = "商店ID",required = true)
    @NotBlank
    private String storeId;
    @TableField("productIds")
    private String productIds;
    @ApiModelProperty(value = "用户账号",required = true)
    @NotBlank
    private String account;
    @ApiModelProperty("下单时间")
    @TableField("orderTime")
    @NotBlank
    private String orderTime;
    @ApiModelProperty("总价")
    @TableField("totalPrice")
    @NotBlank
    private Float totalPrice;
    @ApiModelProperty("支付方式")
    @TableField("payMethod")
    private String payMethod;
    @TableField("payId")
    @ApiModelProperty("支付单号")
    private String payId;
    @TableField("payNumber")
    @ApiModelProperty("支付流水号")
    private String payNumber;
    @TableField("diliveryMethod")
    @ApiModelProperty("配送方式")
    private String diliveryMethod;
    @TableField("packageId")
    @ApiModelProperty("包裹ID")
    private String packageId;
    @ApiModelProperty("订单状态")
    private Integer state;
}