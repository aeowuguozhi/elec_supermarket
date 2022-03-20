/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: BrandController
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/17 10:33
 * Description: 品牌控制类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.brand.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnuz.electronic_supermarket.brand.service.BrandService;
import com.bnuz.electronic_supermarket.common.dto.SysResult;
import com.bnuz.electronic_supermarket.common.enums.SysResultEnum;
import com.bnuz.electronic_supermarket.common.exception.MsgException;
import com.bnuz.electronic_supermarket.common.javaBean.Brand;
import com.bnuz.electronic_supermarket.common.utils.CalendarUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * controller api
 */

@Api(tags = "品牌API")
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 创建品牌
     * @param brand
     * @return
     */
    @SaCheckLogin
    @SaCheckPermission("category-add")
    @ApiOperation("创建品牌")
    @ApiImplicitParam(paramType = "header",name = "token",value = "商家token",required = true)
    @PostMapping("/create")
    public SysResult create(@RequestBody Brand brand){
        try{
            brandService.save(brand);
            Map<String,Object> result = new HashMap<>();
            result.put("brandId",brand.getId());
            return new SysResult(SysResultEnum.Created.getIndex(),SysResultEnum.Created.getName(),result);
        }catch (MsgException e){
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(),e.getMessage(),null);
        }catch (Exception e){
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(),e.getMessage(),null);
        }
    }

    /**
     * 删除品牌
     * @param brandId
     * @return
     */
    @SaCheckLogin
    @SaCheckPermission("category-delete")
    @ApiOperation("通过品牌ID删除品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "id",value = "品牌ID"),
            @ApiImplicitParam(paramType = "header",name = "token",value = "商家token",required = true)
    })
    @DeleteMapping("/delete")
    public SysResult delete(@RequestParam("id") String brandId){
        try{
            String s = brandService.delete(brandId);
            Map<String,Object> result = new HashMap<>();
            result.put("brandId",s);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(),SysResultEnum.SUCCESS.getName(),result);
        }catch (MsgException e){
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(),e.getMessage(),null);
        }catch (Exception e){
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(),e.getMessage(),null);
        }
    }

    /**
     * 品牌名字查询品牌   非登陆态也可查询
     * @param name
     * @return
     */
    @ApiOperation("根据品牌名字分页查询品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "currPage",value = "当前页",defaultValue = "1"),
            @ApiImplicitParam(paramType = "query",name = "size",value = "一页多少条记录",defaultValue = "10")
    })
    @GetMapping("/query")
    public SysResult query(@RequestParam(value = "currPage",defaultValue = "1") Integer currPage,
                           @RequestParam(value = "size",defaultValue = "10") Integer size,
                           @RequestParam("name") String name){
        try{
            Page<Brand> page = brandService.query(currPage, size, name);
            Map<String,Object> result = new HashMap<>();
            result.put("page",page);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(),SysResultEnum.SUCCESS.getName(),result);
        }catch (MsgException e){
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(),e.getMessage(),null);
        }catch (Exception e){
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(),e.getMessage(),null);
        }
    }
}