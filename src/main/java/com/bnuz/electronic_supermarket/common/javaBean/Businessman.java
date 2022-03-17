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