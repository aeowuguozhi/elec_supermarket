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
import com.bnuz.electronic_supermarket.category.service.CategoryService;
import com.bnuz.electronic_supermarket.common.dto.SysResult;
import com.bnuz.electronic_supermarket.common.enums.SysResultEnum;
import com.bnuz.electronic_supermarket.common.exception.MsgException;
import com.bnuz.electronic_supermarket.common.javaBean.Category;
import com.bnuz.electronic_supermarket.common.utils.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryDao;

    private final static Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    /**
     * 回填刚创建的商品ID到分类表中
     * @param CategoryName
     * @param newProIds
     * @return
     */
    @Override
    public SysResult saveBackProductIds(String CategoryName, List<String> newProIds) {
        try {
            //TODO unTest
            //商品ID
            int length = newProIds.size();
            QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", CategoryName);
            //从数据库中取出这个分类
            Category category = categoryDao.selectOne(queryWrapper);
            //从json解析还原旧数组
            ArrayList productIds = GsonUtil.getGson().fromJson(category.getProduct_ids(), ArrayList.class);
            //add 刚刚创建的商品的ID回去这个分类里
            for (int i = 0; i < length; i++) {
                productIds.add(newProIds.get(i));
            }
            //新商品ids数组形成了，json数据set回去category
            category.setProduct_ids(GsonUtil.getGson().toJson(productIds));
            //update 回去数据库
            UpdateWrapper<Category> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("name", category.getName());
            int update = categoryDao.update(category, updateWrapper);
            if (update <= 0) {
                throw new MsgException("更新失败");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("categoryName", category.getName());
            //更新成功判断HttpCode == 200
            return new SysResult(SysResultEnum.SUCCESS.getIndex(), SysResultEnum.SUCCESS.getName(), map);
        } catch (MsgException e) {
            LOGGER.info(e.getMessage());
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        }

    }
}
