/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: Category
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/17 21:17
 * Description: 分类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.common.javaBean;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * lombok   mybatis-plus   API
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@TableName("category")
@ApiModel("分类表")
public class Category {
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("分类名字,eg:水果，衣服，电器")
    private String name;
    @ApiModelProperty("商品ids数组")
    private String product_ids;
}