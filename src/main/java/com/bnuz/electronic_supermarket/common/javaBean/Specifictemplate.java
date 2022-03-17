/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: Specificationtemplate
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/17 12:18
 * Description: 规格模版
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
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("规格模板")
@TableName("specifictemplate")
public class Specifictemplate {
    @ApiModelProperty("id(不要填)")
    private String id;
    @ApiModelProperty("商品ID(不要填)")
    private String product_id;
    @ApiModelProperty("商品名称(不要填)")
    @TableField("productName")
    private String productName;
    @ApiModelProperty("Map<规格名,规格值>")
    @TableField("specifTemplate")
    private String specifTemplate;
}