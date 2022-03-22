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


package com.bnuz.electronic_supermarket.common.javaBean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")           //注解就是user表，mybatis-plus的默认规则是：JavaBean的类名跟数据库表名一致，不一致就用这个写表名。
public class User implements Serializable {
    public static final String Role = "用户";

    public static final String[] myPermission = {
            "user-*",                                                              //修改个人信息
            "cart-add","cart-update","cart-delete","cart-query",                  //管理自己的购物车
            "orderr-*",                                                             //管理自己的订单
            "store-query",                                                         //查看店铺
            "product-query",                                                       //查看商品，分类，品牌
            "category-query","brand-query",                                         //查看配送包裹
            "package-query","comment-*","reply-*",                                  //商品评论，回复某条商品评论
            "specification-query","specifictemplate-query"                         //查看规格和查看规格模板

    };

    public static final String myPrefix = User.class.getSimpleName();

    private String id;
    @TableField("wechatId")
    private String wechatId;
    @NotBlank(message = "账号不能含有空格，且长度不能为0")
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
    @Email
    private String email;
    @TableField("createTime")
    private String createTime;
    @TableField("updateTime")
    private String updateTime;
}
