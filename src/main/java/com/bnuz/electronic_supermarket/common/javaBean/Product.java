/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: Product
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/18 10:44
 * Description: 商品
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

import javax.validation.constraints.PositiveOrZero;
import java.util.concurrent.locks.ReentrantLock;

/**
 * lombok   mybatis-plus   API       购买商品，生成订单，支付订单后需要减SKU库存，所以注意并发
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@TableName("product")
@ApiModel("商品")
public class Product {
    private static ReentrantLock reentrantLock = null;

    public static ReentrantLock getReentrantLock(){
        if(Product.reentrantLock == null){
            reentrantLock = new ReentrantLock();
        }
        return reentrantLock;
    }

    @ApiModelProperty("id(不要填)")
    private String id;
    @ApiModelProperty(value = "品牌_id")
    @TableField("brandId")
    private String brandId;
    @ApiModelProperty(value = "店铺_id",required = true)
    @TableField("storeId")
    private String storeId;
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
    @ApiModelProperty("状态")
    private int state;
    @ApiModelProperty(value = "库存",required = true)
    @PositiveOrZero(message = "库存不能为负数")        //共享变量，库存
    private int stock;
    @ApiModelProperty(value = "规格模板ID",required = true)
    @TableField("specitemplateId")
    private String specitemplateId;
    @ApiModelProperty("备注")
    private String tips;
    @TableField("createTime")
    private String createTime;
    @TableField("updateTime")
    private String updateTime;
}