/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: SpecificationController
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/17 12:26
 * Description: 规格控制类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.specification.speci.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnuz.electronic_supermarket.common.dto.SysResult;
import com.bnuz.electronic_supermarket.common.enums.SysResultEnum;
import com.bnuz.electronic_supermarket.common.exception.MsgException;
import com.bnuz.electronic_supermarket.common.javaBean.Specification;
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
 * 规格什么人都可以创建，但是规格模板必须商家进行创建。
 */
@Api(tags = "规格名称")
@RestController
@RequestMapping("/specification")
public class SpecificationController {
    @Autowired
    private SpecificationService speService;

    private final static Logger LOGGER = LoggerFactory.getLogger(SpecificationController.class);

    @ApiOperation("批量创建规格")
    @ApiResponse(description = "传入规格数组，返回规格数组")
    @PostMapping("/create")
    public SysResult create(@RequestBody ArrayList<String> list) {
        try {
            List<Specification>entity = new ArrayList<>();
            int length = list.size();
            for (int i = 0; i < length; i++) {
                Specification spe = new Specification(UUID.randomUUID().toString(),list.get(i));
                entity.add(spe);
            }
            boolean save = speService.saveBatch(entity);
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

    @ApiOperation("删除规格")
    @ApiImplicitParam(paramType = "query",name = "specificationId",value = "规格ID")
    @ApiResponse(description = "返回规格ID")
    @DeleteMapping("/delete")
    public SysResult delete(@RequestParam("specificationId") String id) {
        try {
            boolean b = speService.removeById(id);
            if(!b) throw new MsgException("删除失败");
            Map<String, Object> map = new HashMap<>();
            map.put("specificationId", id);
            return new SysResult(SysResultEnum.Created.getIndex(), SysResultEnum.SUCCESS.getName(), map);
        } catch (MsgException e) {
            LOGGER.info(e.getMessage());
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        }
    }

    @ApiOperation("通过规格名进行查询")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "currPage",value = "当前页",defaultValue = "1"),
            @ApiImplicitParam(paramType = "query",name = "size",value = "一页多少条记录",defaultValue = "10"),
            @ApiImplicitParam(paramType = "query",name = "name",value = "规格名")
    })
    @GetMapping("/queryByName")
    public SysResult query(@RequestParam(value = "currPage",defaultValue = "1") Integer currPage,
                           @RequestParam(value = "size",defaultValue = "10") Integer size,
                           @RequestParam("name") String name) {
        try {
            //select * from specification where name like "name"
            Page<Specification>page = new Page<>(currPage,size);
            QueryWrapper<Specification>queryWrapper = new QueryWrapper<>();
            queryWrapper.like("name",name);
            Page<Specification> pageResult = speService.page(page, queryWrapper);
            Map<String, Object> map = new HashMap<>();
            map.put("page",pageResult);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(), SysResultEnum.SUCCESS.getName(), map);
        } catch (MsgException e) {
            LOGGER.info(e.getMessage());
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        }
    }

    @ApiOperation("查询所有规格名")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "currPage",value = "当前页",defaultValue = "1"),
            @ApiImplicitParam(paramType = "query",name = "size",value = "一页多少条记录",defaultValue = "10")
    })
    @GetMapping("/queryAll")
    public SysResult queryAll(@RequestParam(value = "currPage",defaultValue = "1") Integer currPage,
                           @RequestParam(value = "size",defaultValue = "10") Integer size) {
        try {
            //select * from specification
            Page<Specification>page = new Page<>(currPage,size);
            Page<Specification> pageResult = speService.page(page, null);
            Map<String, Object> map = new HashMap<>();
            map.put("page",pageResult);
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