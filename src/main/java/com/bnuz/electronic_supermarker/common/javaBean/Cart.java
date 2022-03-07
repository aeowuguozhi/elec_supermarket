/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: Cart
 * Author:   Mr.WuGuoZhi
 * Date:     2022/2/22 21:30
 * Description: 购物车
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarker.common.javaBean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class Cart {
    private Integer id;
    private String account;
    private Integer productId;
    private String productImage;
    private String productName;
    private Double productSellPrice;
    private Integer productNumber;
}