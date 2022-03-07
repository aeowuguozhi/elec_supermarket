/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: Administrator
 * Author:   Mr.WuGuoZhi
 * Date:     2022/2/22 21:37
 * Description: 管理员
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
public class Administrator {
    private Integer id;
    private Integer deptId;
    private String account;
    private String password;
    private String headImage;
    private String nickName;
    private String phoneNumber;
    private String realName;
    private String idCard;
    private String idCardFontPict;
    private String idCardBackPict;
    private Integer state;
    private String createTime;
    private String updateTime;
    private String note;

}