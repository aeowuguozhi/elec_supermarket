/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: CategoryServiceImpl
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/17 21:21
 * Description: 分类服务实现类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.category.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnuz.electronic_supermarket.category.controller.CategoryController;
import com.bnuz.electronic_supermarket.category.dao.CategoryMapper;
import com.bnuz.electronic_supermarket.category.service.CategoryProductService;
import com.bnuz.electronic_supermarket.category.service.CategoryService;
import com.bnuz.electronic_supermarket.common.dto.SysResult;
import com.bnuz.electronic_supermarket.common.enums.SysResultEnum;
import com.bnuz.electronic_supermarket.common.exception.MsgException;
import com.bnuz.electronic_supermarket.common.javaBean.Category;
import com.bnuz.electronic_supermarket.common.javaBean.Category_product;
import com.bnuz.electronic_supermarket.common.utils.GsonUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryProductService cpservice;
    private final static Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    /**
     * 数据库设计有点问题，分类下有多个商品，商品可以从属多个分类
     * 回填刚创建的那一个商品ID到分类数组中
     * @param categorys
     * @param productId
     * @return
     */
    @Override
    public SysResult saveCatePro(List<String>categorys,String productId) {
        try {
            //分类数组已经在创建商品的时候进行了验证了
            int length = categorys.size();
            List<Category_product>list = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                Category_product cp = new Category_product(UUID.randomUUID().toString(),categorys.get(i),productId);
                list.add(cp);
            }
            boolean b = cpservice.saveBatch(list);
            //更新成功判断HttpCode == 200
            if(!b){
                throw new MsgException("分类-商品信息存储失败");
            }
            return new SysResult(SysResultEnum.SUCCESS.getIndex(), SysResultEnum.SUCCESS.getName(), null);
        } catch (MsgException e) {
            LOGGER.info(e.getMessage());
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        }
    }
}
