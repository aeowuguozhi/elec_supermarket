/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: Category_product
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/19 17:23
 * Description: 分类商品表
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
import org.springframework.data.relational.core.mapping.Table;

/**
 * lombok mybatis-plus api
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("分类-商品表")
@TableName("category_product")
public class Category_product {
    private String id;
    @ApiModelProperty("分类名")
    @TableField("categoryName")
    private String categoryName;
    @TableField("productId")
    @ApiModelProperty("商品ID")
    private String productId;
}