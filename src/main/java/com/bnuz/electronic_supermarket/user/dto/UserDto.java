/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: UserDto
 * Author:   Mr.WuGuoZhi
 * Date:     2022/2/23 10:39
 * Description: 用户dto，用来更新用户信息
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class UserDto {
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
}