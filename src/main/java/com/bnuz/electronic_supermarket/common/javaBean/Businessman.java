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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * javaBean:mybatis-plus,API
 */

@ToString
@NoArgsConstructor
@Data
@TableName("businessman")
@ApiModel("商家模型")
public class Businessman implements Serializable {
    public static final String Role = "商家";

    public static final String[] myPermission = {
            "businessman-*",                                                              //修改个人信息
            "orderr-query",                                                        //查看用户下的的订单
            "store-*",                                                             //查看店铺
            "product-*",                                                           //查看自己商店的商品，分类，品牌
            "category-add","category-query","brand-*",                              //查看配送包裹
            "package-query","reply-*",                                  //商品评论只有购买过后的用户才可以评论，回复某条商品评论
            "specifictemplate-*",                                       //规格模板里面的数据含有规格和规格值，所以商家和管理员可以看见
            "specification-*",                                           //规格只有商家和管理员可以操作
            "brand-*"
    };

    public static final String myPrefix = Businessman.class.getSimpleName();

    private String id;
    @ApiModelProperty("账号")
    private String account;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("头像")
    @TableField("headImage")
    private String headImage;
    @ApiModelProperty("真实姓名")
    @TableField("realName")
    private String realName;
    @ApiModelProperty("手机号码")
    @TableField("phoneNumber")
    private String phoneNumber;
    @TableField("idCard")
    @ApiModelProperty("身份证号")
    private String idCard;
    @TableField("idCardFontPict")
    @ApiModelProperty("身份证正面照片")
    private String idCardFontPict;
    @ApiModelProperty("身份证背面照片")
    @TableField("idCardBackPict")
    private String idCardBackPict;
    @ApiModelProperty("状态")
    private Integer state;
    @TableField("createTime")
    @ApiModelProperty("创建时间")
    private String createTime;
    @TableField("updateTime")
    @ApiModelProperty("更新时间")
    private String updateTime;
    @TableField("wechatId")
    @ApiModelProperty("微信ID")
    private String wechatId;
}