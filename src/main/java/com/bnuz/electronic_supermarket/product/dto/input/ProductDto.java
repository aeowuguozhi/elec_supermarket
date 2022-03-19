/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: ProductDto
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/19 20:14
 * Description: 商品Dto
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.product.dto.input;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("商品Dto")
public class ProductDto {
    @ApiModelProperty(value = "品牌_id")
    private String brand_id;
    @ApiModelProperty(value = "店铺_id",required = true)
    private String store_id;
    @TableField("barCode")
    @ApiModelProperty("条形码")
    private String barCode;
    @ApiModelProperty(value = "名称",required = true)
    private String name;
    @ApiModelProperty("进价")
    @TableField("purchasePrice")
    private Float purchasePrice;
    @ApiModelProperty("原价")
    private Float price;
    @TableField("sellPrice")
    @ApiModelProperty(value = "售价",required = true)
    private Float sellPrice;
    @ApiModelProperty("简要描述")
    private String brief;
    @ApiModelProperty("详细描述")
    private String description;
    @ApiModelProperty("图片")
    private String pictures;
    @ApiModelProperty("视频")
    private String vedio;
    @ApiModelProperty("状态（1上架，0下架，2促销商品）")
    private int state;
    @ApiModelProperty(value = "库存",required = true)
    private int stock;
    @ApiModelProperty(value = "规格模板ID",required = true)
    @TableField("specitemplateId")
    private String specitemplateId;
    @ApiModelProperty("备注")
    private String tips;
    @ApiModelProperty("分类数组")
    private String[] categorys;
}