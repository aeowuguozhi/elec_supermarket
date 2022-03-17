/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: Store
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/16 14:36
 * Description: 商店
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.common.javaBean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * lombok   mybatis-plus   API
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@TableName("store")
@ApiModel("商店模型")
public class Store implements Serializable {
    private String id;
    @TableField("businessmanId")
    @ApiModelProperty(value = "商家ID(外键)",required = true)
    private String businessmanId;           //外键
    @TableField("headImage")
    @ApiModelProperty("头像")
    private String headImage;
    @ApiModelProperty(value = "店铺名字",required = true)
    private String name;
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty("状态[1 使用中] [0 注销] [2 异常]")
    private Integer state;
    @ApiModelProperty(value = "类型",required = true)
    private String category;
    @TableField("contactInfo")
    @ApiModelProperty(value = "联系方式",required = true)
    private String contactInfo;
    @TableField("createTime")
    @ApiModelProperty("创建时间")
    private String createTime;
    @TableField("updateTime")
    @ApiModelProperty("更新时间")
    private String updateTime;
}