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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")           //注解就是user表，mybatis-plus的默认规则是：JavaBean的类名跟数据库表名一致，不一致就用这个写表名。
public class User {
    private String id;
    @TableField("wechatId")
    private String wechatId;
    private String account;
    private String password;
    @TableField("headImage")
    private String headImage;
    @TableField("nickName")
    private String nickName;
    @TableField("phoneNumber")
    private String phoneNumber;
    @TableField("realName")
    private String realName;
    @TableField("idCard")
    private String idCard;
    private String address;
    private Integer state;
    private String note;
    private String email;
    @TableField("createTime")
    private String createTime;
    @TableField("updateTime")
    private String updateTime;
}
