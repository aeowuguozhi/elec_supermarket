/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: PaymentDto
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/20 17:41
 * Description: 支付信息Dto
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel("支付单号Dto")
public class PaymentDto {
    @ApiModelProperty(value = "交易单号",required = true)
    private String payId;
    @ApiModelProperty(value = "交易方式",required = true)
    private String payMethod;
    @ApiModelProperty("交易流水号（可以不填）")
    private String payNumber;

}