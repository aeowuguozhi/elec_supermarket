/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: ProductController
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/19 16:56
 * Description: 商品控制类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.product.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.IdUtil;
import cn.hutool.db.sql.Order;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnuz.electronic_supermarket.common.dto.SysResult;
import com.bnuz.electronic_supermarket.common.enums.SysResultEnum;
import com.bnuz.electronic_supermarket.common.exception.MsgException;
import com.bnuz.electronic_supermarket.common.javaBean.Orderr;
import com.bnuz.electronic_supermarket.common.javaBean.Product;
import com.bnuz.electronic_supermarket.common.javaBean.Store;
import com.bnuz.electronic_supermarket.common.javaBean.User;
import com.bnuz.electronic_supermarket.common.utils.GsonUtil;
import com.bnuz.electronic_supermarket.common.utils.LocalDateTimeUtils;
import com.bnuz.electronic_supermarket.order.dto.OrderDto;
import com.bnuz.electronic_supermarket.order.enums.OrderrEnum;
import com.bnuz.electronic_supermarket.order.service.OrderrService;
import com.bnuz.electronic_supermarket.product.dto.input.ProductDto;
import com.bnuz.electronic_supermarket.product.dto.input.UpdateProductDto;
import com.bnuz.electronic_supermarket.product.service.ProductService;
import com.bnuz.electronic_supermarket.store.service.StoreService;
import com.bnuz.electronic_supermarket.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sa-Token API
 */
@Api(tags = "商品API")
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;



    @SaCheckLogin
    @SaCheckPermission("product-add")
    @ApiOperation("商家创建商品")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "商家token", required = true)
    })
    @PostMapping("/create")
    public SysResult create(@RequestBody ProductDto productDto) {
        try {
            List<String> categorys = Arrays.asList(productDto.getCategorys());
            Product product = new Product();
            dto2bean(product, productDto);
            productService.save(product, categorys);
            Map<String, Object> map = new HashMap<>();
            map.put("product_id", product.getId());
            map.put("specificTemplateId", product.getSpecitemplateId());
            map.put("brandId", product.getBrand_id());
            return new SysResult(SysResultEnum.Created.getIndex(), SysResultEnum.Created.getName(), map);
        } catch (MsgException e) {
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        } finally {

        }
    }

    @SaCheckLogin
    @SaCheckPermission("product-delete")
    @ApiOperation("商家删除商品")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "商家token", required = true),
            @ApiImplicitParam(paramType = "query", name = "proId", value = "商品ID", required = true)
    })
    @DeleteMapping("/delete")
    public SysResult delete(@RequestParam("proId") String productId) {
        try {
            boolean b = productService.removeById(productId);
            if (!b) throw new MsgException("删除失败");
            Map<String, Object> map = new HashMap<>();
            map.put("productId", productId);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(), SysResultEnum.SUCCESS.getName(), map);
        } catch (MsgException e) {
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        } finally {

        }
    }


    @ApiOperation("查看所有商品")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "currPage", value = "当前页", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "size", value = "一页多少条记录", defaultValue = "10")
    })
    @GetMapping("/queryAll")
    public SysResult queryAll(@RequestParam(value = "currPage", defaultValue = "1") Integer currPage,
                              @RequestParam(value = "size", defaultValue = "10") Integer size) {
        try {
            Page<Product> page = productService.page(new Page<Product>(currPage, size));
            Map<String, Object> map = new HashMap<>();
            map.put("products", page);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(), SysResultEnum.SUCCESS.getName(), map);
        } catch (MsgException e) {
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        } finally {

        }
    }

    @ApiOperation("通过商品名查看商品")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "currPage", value = "当前页", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "size", value = "一页多少条记录", defaultValue = "10"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "商品名", required = true)
    })
    @GetMapping("/queryByName")
    public SysResult queryByName(@RequestParam(value = "currPage", defaultValue = "1") Integer currPage,
                                 @RequestParam(value = "size", defaultValue = "10") Integer size,
                                 @RequestParam("name") String name) {
        try {
            Map<String, Object> page = productService.page(currPage, size, name);
            Map<String, Object> map = new HashMap<>();
            map.put("products", page);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(), SysResultEnum.SUCCESS.getName(), map);
        } catch (MsgException e) {
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        } finally {

        }
    }

    @ApiOperation("商家修改商品信息")
    @SaCheckLogin
    @SaCheckPermission("product-update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "proId", value = "商品ID", required = true),
            @ApiImplicitParam(paramType = "header", name = "token", value = "商家token", required = true)
    })
    @PutMapping("/put")
    public SysResult update(@RequestBody UpdateProductDto dto, @RequestParam("proId") String proId) {
        try {
            boolean update = productService.update(dto, proId);
            if (!update) throw new MsgException("更新失败");
            Map<String, Object> map = new HashMap<>();
            map.put("productId", proId);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(), SysResultEnum.SUCCESS.getName(), map);
        } catch (MsgException e) {
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        } finally {

        }
    }


    @ApiOperation("查看某商店下所有商品")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "currPage", value = "当前页", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "size", value = "一页多少条记录", defaultValue = "10"),
            @ApiImplicitParam(paramType = "query",name = "storeId",value = "商店ID")
    })
    @GetMapping("/queryByStoreId")
    public SysResult queryAllByStoreId(@RequestParam(value = "currPage", defaultValue = "1") Integer currPage,
                              @RequestParam(value = "size", defaultValue = "10") Integer size,
                              @RequestParam("storeId") String storeId) {
        try {
            QueryWrapper<Product>queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("store_id",storeId);
            Page<Product> page = productService.page(new Page<Product>(currPage, size),queryWrapper);
            Map<String, Object> map = new HashMap<>();
            map.put("products", page);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(), SysResultEnum.SUCCESS.getName(), map);
        } catch (MsgException e) {
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        } finally {

        }
    }



    private void dto2bean(Product product, ProductDto productDto) {
        product.setBarCode(productDto.getBarCode());
        product.setBrand_id(productDto.getBrand_id());
        product.setStore_id(productDto.getStore_id());
        product.setBrief(productDto.getBrief());
        product.setDescription(productDto.getDescription());
        product.setName(productDto.getName());
        product.setVedio(productDto.getVedio());
        product.setSpecitemplateId(productDto.getSpecitemplateId());
        product.setPictures(productDto.getPictures());
        product.setPrice(productDto.getPrice());
        product.setPurchasePrice(productDto.getPurchasePrice());
        product.setSellPrice(productDto.getSellPrice());
        product.setState(productDto.getState());
        product.setStock(productDto.getStock());
        product.setTips(productDto.getTips());

    }

}