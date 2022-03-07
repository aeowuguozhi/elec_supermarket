/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: UserRegisDto
 * Author:   Mr.WuGuoZhi
 * Date:     2022/2/24 13:59
 * Description: 用户注册dto【
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarker.user.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {
    private String account;
    private String password;
    private String phoneNumber;

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}