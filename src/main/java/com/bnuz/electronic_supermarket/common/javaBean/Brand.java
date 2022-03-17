/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: Brand
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/17 10:15
 * Description: 品牌
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.common.javaBean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * lombok mybatis-plus api
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName("brand")
@ApiModel(value = "商品品牌",description = "eg:南极人，天堂..")
public class Brand {
    @ApiModelProperty("id(不要填)")
    private String id;
    @TableField("headImage")
    @ApiModelProperty("头像")
    private String headImage;
    @ApiModelProperty("品牌名称")
    private String name;
    @ApiModelProperty("商品id数组(不要填)")
    private String product_ids;
    @ApiModelProperty("详细描述/简介")
    private String description;
    @ApiModelProperty("创建时间(不要填)")
    @TableField("createTime")
    private String createTime;
}