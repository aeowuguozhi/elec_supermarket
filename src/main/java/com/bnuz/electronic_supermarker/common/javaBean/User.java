/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: User
 * Author:   Mr.WuGuoZhi
 * Date:     2022/2/22 21:02
 * Description: 用户
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarker.common.javaBean;

import com.sun.istack.internal.NotNull;
import lombok.*;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String wechatId;
    private String account;
    private String password;
    private String headImage;
    private String nickName;
    private String phoneNumber;
    private String realName;
    private String idCard;
    private String address;
    private Integer state;
    private String note;
    private String email;
    private String createTime;
    private String updateTime;
}
