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
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * javaBean:mybatis-plus,API
 */

@ToString
@NoArgsConstructor
@Data
@TableName("businessman")
public class Businessman {
    private String id;
    private String account;
    private String password;
    @TableField("headImage")
    private String headImage;
    @TableField("realName")
    private String realName;
    @TableField("phoneNumber")
    private String phoneNumber;
    @TableField("idCard")
    private String idCard;
    @TableField("idCardFontPict")
    private String idCardFontPict;
    @TableField("idCardBackPict")
    private String idCardBackPict;
    private Integer state;
    @TableField("createTime")
    private String createTime;
    @TableField("updateTime")
    private String updateTime;
    @TableField("wechatId")
    private String wechatId;
}