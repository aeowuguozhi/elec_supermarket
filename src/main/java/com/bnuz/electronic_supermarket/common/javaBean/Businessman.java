/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: Businessman
 * Author:   Mr.WuGuoZhi
 * Date:     2022/2/22 21:44
 * Description: 商家
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.common.javaBean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Data
public class Businessman {
    private Integer id;
    private String account;
    private String password;
    private String headImage;
    private String realName;
    private String phoneNumber;
    private String idCard;
    private String idCardFontPict;
    private String idCardBackPict;
    private Integer state;
    private String createTime;
    private String updateTime;
    private String wechatId;
}