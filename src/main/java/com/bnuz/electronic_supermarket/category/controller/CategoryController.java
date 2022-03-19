/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: CategoryController
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/17 21:23
 * Description: 分类控制类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.category.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnuz.electronic_supermarket.category.service.CategoryService;
import com.bnuz.electronic_supermarket.common.dto.SysResult;
import com.bnuz.electronic_supermarket.common.enums.SysResultEnum;
import com.bnuz.electronic_supermarket.common.exception.MsgException;
import com.bnuz.electronic_supermarket.common.javaBean.Category;
import com.bnuz.electronic_supermarket.common.javaBean.Specification;
import com.bnuz.electronic_supermarket.common.utils.GsonUtil;
import com.bnuz.electronic_supermarket.specification.speci.controller.SpecificationController;
import com.bnuz.electronic_supermarket.specification.speci.service.SpecificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 谁都可以创建
 */
@Api(tags = "分类API")
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService service;

    private final static Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @SaCheckLogin
    @SaCheckPermission("category-add")
    @ApiOperation("批量创建分类")
    @ApiImplicitParam(paramType = "header",name = "token",value = "商家token")
    @ApiResponse(description = "传入分类数组，返回分类数组")
    @PostMapping("/create")
    public SysResult create(@RequestBody ArrayList<String> categorys) {
        try {
            List<Category> entity = new ArrayList<>();
            int length = categorys.size();
            for (int i = 0; i < length; i++) {
                Category category = new Category(UUID.randomUUID().toString(), categorys.get(i));
                entity.add(category);
            }
            boolean save = service.saveBatch(entity);
            if (!save) throw new MsgException("创建失败");
            Map<String, Object> map = new HashMap<>();
            map.put("list", entity);
            return new SysResult(SysResultEnum.Created.getIndex(), SysResultEnum.SUCCESS.getName(), map);
        } catch (MsgException e) {
            LOGGER.info(e.getMessage());
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        }
    }

    @SaCheckLogin
    @SaCheckPermission("category-delete")
    @ApiOperation("删除分类")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "categoryId", value = "分类ID"),
            @ApiImplicitParam(paramType = "header",name = "token",value = "商家token")
    })
    @ApiResponse(description = "返回分类ID")
    @DeleteMapping("/delete")
    public SysResult delete(@RequestParam("categoryId") String id) {
        try {
            boolean b = service.removeById(id);
            if (!b) throw new MsgException("删除失败");
            Map<String, Object> map = new HashMap<>();
            map.put("categoryId", id);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(), SysResultEnum.SUCCESS.getName(), map);
        } catch (MsgException e) {
            LOGGER.info(e.getMessage());
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        }
    }

    @ApiOperation("通过分类名进行查询")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "currPage", value = "当前页", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "size", value = "一页多少条记录", defaultValue = "10"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "分类名")
    })
    @GetMapping("/queryByName")
    public SysResult query(@RequestParam(value = "currPage", defaultValue = "1") Integer currPage,
                           @RequestParam(value = "size", defaultValue = "10") Integer size,
                           @RequestParam("name") String name) {
        try {
            //select * from category where name like "name"
            Page<Category> page = new Page<>(currPage, size);
            QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("name", name);
            Page<Category> pageResult = service.page(page, queryWrapper);
            Map<String, Object> map = new HashMap<>();
            map.put("page", pageResult);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(), SysResultEnum.SUCCESS.getName(), map);
        } catch (MsgException e) {
            LOGGER.info(e.getMessage());
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        }
    }

    @ApiOperation("查询所有分类名")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "currPage", value = "当前页", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "size", value = "一页多少条记录", defaultValue = "10")
    })
    @GetMapping("/queryAll")
    public SysResult queryAll(@RequestParam(value = "currPage", defaultValue = "1") Integer currPage,
                              @RequestParam(value = "size", defaultValue = "10") Integer size) {
        try {
            //select * from category
            Page<Category> page = new Page<>(currPage, size);
            Page<Category> pageResult = service.page(page, null);
            Map<String, Object> map = new HashMap<>();
            map.put("page", pageResult);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(), SysResultEnum.SUCCESS.getName(), map);
        } catch (MsgException e) {
            LOGGER.info(e.getMessage());
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        }
    }

    /**
     * 创建商品后，商品ID需要add到分类名为name的那条记录的product_ids     List<int>   ERROR do not used
     */
    @ApiOperation("创建的商品的ID回填到分类的productIds字段")
    @PostMapping("/mergeProductIds")
    @Deprecated
    public SysResult saveBackProductIds(@RequestParam("categoryName") String name,
                                        @RequestBody List<String> ids) {
        try {
            //商品ID
            int length = ids.size();
            QueryWrapper<Category>queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name",name);
            //从数据库中取出这个分类
            Category category = service.getOne(queryWrapper);
            //update 回去数据库
            UpdateWrapper<Category>updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("name",category.getName());
            service.update(category,updateWrapper);
            Map<String,Object>map = new HashMap<>();
            map.put("categoryName",category.getName());
            return new SysResult(SysResultEnum.SUCCESS.getIndex(),SysResultEnum.SUCCESS.getName(),map);
        } catch (MsgException e) {
            LOGGER.info(e.getMessage());
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        }

    }
}