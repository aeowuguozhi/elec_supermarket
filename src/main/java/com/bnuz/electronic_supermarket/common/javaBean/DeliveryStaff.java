/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: DeliveryStaff
 * Author:   Mr.WuGuoZhi
 * Date:     2022/2/22 21:47
 * Description: 配送员/外卖员
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.common.javaBean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class DeliveryStaff {
    private String id;
    private Integer companyId;
    private String account;
    private String password;
    private String headImage;
    private String wechatId;
    private String phoneNumber;
    private String realName;
    private String idCard;
    private String idCardFontPict;
    private String idCardBackPict;
    private String createTime;
    private String updateTime;
    private Integer state;
}