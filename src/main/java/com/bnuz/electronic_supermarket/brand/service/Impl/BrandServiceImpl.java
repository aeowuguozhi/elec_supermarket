/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: BrandServiceImpl
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/17 10:34
 * Description: 品牌服务层实现类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.brand.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnuz.electronic_supermarket.brand.dao.BrandMapper;
import com.bnuz.electronic_supermarket.brand.service.BrandService;
import com.bnuz.electronic_supermarket.common.exception.MsgException;
import com.bnuz.electronic_supermarket.common.javaBean.Brand;
import com.bnuz.electronic_supermarket.common.utils.LocalDateTimeUtils;
import com.bnuz.electronic_supermarket.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

    @Autowired
    private BrandMapper brandDao;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private final static Logger LOGGER = LoggerFactory.getLogger(BrandServiceImpl.class);

    @Override
    public boolean save(Brand entity) {
        try{
            entity.setCreateTime(LocalDateTimeUtils.getLocalDateTime());
            entity.setId(UUID.randomUUID().toString());
            brandDao.insert(entity);
            return true;
        }catch(Exception e){
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public Page<Brand> query(int currPage,int size,String name) {
        try {
            Page<Brand> page = new Page<>(currPage,size);
            QueryWrapper<Brand>queryWrapper = new QueryWrapper<>();
            queryWrapper.like("name",name);
            Page<Brand> brandPage = brandDao.selectPage(page, queryWrapper);
            return brandPage;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public String delete(String id) {
        try{
            //delete from brand where id = id;
            QueryWrapper<Brand> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",id);
            int delete = brandDao.delete(queryWrapper);
//            LOGGER.info(delete);
            return id;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            throw e;
        }
    }
}