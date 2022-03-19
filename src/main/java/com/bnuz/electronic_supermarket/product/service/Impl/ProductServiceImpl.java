/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: ProductServiceImpl
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/18 11:02
 * Description: 商品服务实现类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.product.service.Impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnuz.electronic_supermarket.brand.dao.BrandMapper;
import com.bnuz.electronic_supermarket.brand.service.BrandService;
import com.bnuz.electronic_supermarket.category.service.CategoryService;
import com.bnuz.electronic_supermarket.common.dto.SysResult;
import com.bnuz.electronic_supermarket.common.exception.MsgException;
import com.bnuz.electronic_supermarket.common.javaBean.*;
import com.bnuz.electronic_supermarket.common.utils.GsonUtil;
import com.bnuz.electronic_supermarket.common.utils.LocalDateTimeUtils;
import com.bnuz.electronic_supermarket.common.utils.ValiUtils;
import com.bnuz.electronic_supermarket.product.dao.ProductMapper;
import com.bnuz.electronic_supermarket.product.dto.input.ProductDto;
import com.bnuz.electronic_supermarket.product.dto.input.UpdateProductDto;
import com.bnuz.electronic_supermarket.product.enums.ProductStateEnum;
import com.bnuz.electronic_supermarket.product.service.ProductService;
import com.bnuz.electronic_supermarket.specification.speci.service.SpecificationService;
import com.bnuz.electronic_supermarket.specification.speciTemplate.service.SpeTemplateService;
import com.bnuz.electronic_supermarket.store.dao.StoreDao;
import com.bnuz.electronic_supermarket.store.service.Implement.StoreServiceImpl;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * SPU standard product unit
 * SKU stock keeping unit
 * 商家创建商品，需要商家token
 */

@Validated
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productDao;

    @Autowired
    private BrandService brandService;

    @Autowired
    private StoreDao storeDao;

    @Autowired
    private SpeTemplateService templateService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private final static Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    /**
     * 1，创建商品之前，需要先创建规格模板。
     * 2，创建商品，回填商品相关数据到规格模板表和分类表
     * @param product
     * @return
     */
    @Override
    @Transactional
    public boolean save(Product product,List<String> categorys) {
        try{
            //验证品牌if existed(一个SKU stock keeping unit也可没有品牌)
            if(StringUtils.hasText(product.getBrand_id())){
                Brand brand = brandService.getById(product.getBrand_id());
                if(brand == null){
                    throw new MsgException("找不到品牌信息");
                }
            }
            //验证商店，必须得有商店
            if(StringUtils.containsWhitespace(product.getStore_id()) || !StringUtils.hasText(product.getStore_id())){
                throw  new MsgException("商店ID不能为空");
            }else{
                Store store = storeDao.selectById(product.getStore_id());
                if(store == null){
                    throw new MsgException("商店ID有误");
                }
            }
            //验证规格模板存在不存在
            String specitemplateId = product.getSpecitemplateId();
            Specifictemplate specifictemplate = templateService.queryById(specitemplateId);
            if(specifictemplate == null){
                throw new MsgException("规格模板ID错误");
            }
            //验证分类数组，必须要有分类
            int length = categorys.size();
            for (int i = 0; i < length; i++) {
                QueryWrapper<Category>queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("name",categorys.get(i));
                Category one = categoryService.getOne(queryWrapper);
                if(one == null){
                    throw new MsgException("无" + categorys.get(i) + "该分类,请先创建该分类");
                }
            }
            //所有东西验证完毕。设置商品上架
            product.setUpdateTime(null);
            product.setCreateTime(LocalDateTimeUtils.getLocalDateTime());
            product.setId(UUID.randomUUID().toString());
            product.setState(ProductStateEnum.ONSHELF.getIndex());
            //所有东西设置完成,insert into db
            int insert = productDao.insert(product);
            if(insert <= 0){
                throw new MsgException("创建商品失败");
            }
            //创建商品成功，需要商品ID和分类填到分类-商品表
            SysResult result = categoryService.saveCatePro(categorys, product.getId());
            if(result.getStatus() != 200){
                throw new MsgException(result.getMsg());
            }
            //创建商品成功，需要将商品ID和商品名进行update到规格模板表,这个东西可以回填，也可以不会填。因为回填是基于规格模板是规格名的集合。
            specifictemplate.setProduct_id(product.getId());
            specifictemplate.setProductName(product.getName());
            UpdateWrapper<Specifictemplate>updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",specitemplateId);
            boolean update = templateService.update(specifictemplate, updateWrapper);
            return true;
        }catch (MsgException e){
            LOGGER.info(e.getMessage());
            throw e;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 更新商品数据
     * @param dto
     * @return
     */
    @Override
    public boolean update(UpdateProductDto dto, String productId) {
        try{
            Product oldProduct = productDao.selectById(productId);
            if(oldProduct == null) throw new MsgException("商品ID有误");
            //验证参数
            if(ValiUtils.hasText(dto.getBarCode())){
               oldProduct.setBarCode(dto.getBarCode());
            }
            if(ValiUtils.hasText(dto.getBrand_id())){
                oldProduct.setBrand_id(dto.getBrand_id());
            }
            if(ValiUtils.hasText(dto.getBrief())){
                oldProduct.setBrief(dto.getBrief());
            }
            if(ValiUtils.hasText(dto.getDescription())){
                oldProduct.setDescription(dto.getDescription());
            }
            if(ValiUtils.hasText(dto.getName())){
                oldProduct.setName(dto.getName());
            }
            if(ValiUtils.hasText(dto.getPictures())){
                oldProduct.setPictures(dto.getPictures());
            }
            if(ValiUtils.hasText(dto.getSpecitemplateId())){
                oldProduct.setSpecitemplateId(dto.getSpecitemplateId());
            }
            if (ValiUtils.hasText(dto.getTips())){
                oldProduct.setTips(dto.getTips());
            }
            if(ValiUtils.hasText(dto.getVedio())){
                oldProduct.setVedio(dto.getVedio());
            }
            if(dto.getPrice() >= 0){
                oldProduct.setPrice(dto.getPrice());
            }
            if(dto.getSellPrice() >= 0){
                oldProduct.setSellPrice(dto.getSellPrice());
            }
            if(dto.getPurchasePrice() >= 0){
                oldProduct.setPurchasePrice(dto.getPurchasePrice());
            }
            if(dto.getStock() >= 0){
                oldProduct.setStock(dto.getStock());
            }
            if(dto.getState() == 1 || dto.getState() == 0 || dto.getState() == 2){
                oldProduct.setState(dto.getState());
            }
            UpdateWrapper<Product>updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",productId);
            int update = productDao.update(oldProduct, updateWrapper);
            if(update <= 0){
                throw new MsgException("更新失败");
            }
            return true;
        }catch (MsgException e){
            LOGGER.info(e.getMessage());
            throw e;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public Map<String, Object> page(int currPage, int size, String productName) {
        try{
            Page<Product> page = new Page<>(currPage,size);
            QueryWrapper<Product>queryWrapper = new QueryWrapper<>();
            queryWrapper.like("name",productName).orderByDesc("createTime");
            Page<Product> page1 = productDao.selectPage(page, queryWrapper);
            Map<String,Object>map = new HashMap<>();
            map.put("products",map);
            return map;
        }catch (MsgException e){
            LOGGER.info(e.getMessage());
            throw e;
        }catch(Exception e){
            LOGGER.error(e.getMessage());
            throw e;
        }
    }
}