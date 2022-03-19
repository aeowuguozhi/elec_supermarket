/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: StoreController
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/16 14:36
 * Description: 商店控制类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.store.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnuz.electronic_supermarket.common.dto.SysResult;
import com.bnuz.electronic_supermarket.common.enums.StateEnum;
import com.bnuz.electronic_supermarket.common.enums.SysResultEnum;
import com.bnuz.electronic_supermarket.common.exception.MsgException;
import com.bnuz.electronic_supermarket.common.javaBean.Administrator;
import com.bnuz.electronic_supermarket.common.javaBean.Businessman;
import com.bnuz.electronic_supermarket.common.javaBean.Store;
import com.bnuz.electronic_supermarket.common.utils.JwtUtil;
import com.bnuz.electronic_supermarket.store.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商店权限说明：用户：查看商店信息
 *            商家、管理员：查看、更新、添加、删除商店
 *
 */

@Api(tags = "店铺API")
@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreService storeService;

    /**
     * 创建商店
     * @param store
     * @param request
     * @return
     */
    @SaCheckLogin
    @SaCheckPermission("store-add")
    @ApiOperation("商人创建商店")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "token",value = "商家token",required = true),
    })
    @ApiResponse(description = "id,businessmanId,state,createTime,updateTime不用填")
    @PostMapping("/create")
    public SysResult createStore(@RequestBody Store store, HttpServletRequest request) {
        try {
            //返回商店ID
            String id = storeService.save(store, request);
            Map<String, Object> map = new HashMap<>();
            map.put("storeId", id);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(), SysResultEnum.SUCCESS.getName(), map);
        } catch (MsgException e) {
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        }
    }

    /**
     * 删除商店，state = 0
     * @param sid
     * @param request
     * @return
     */
    @SaCheckLogin
    @SaCheckPermission("store-delete")
    @ApiOperation("删除店铺")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "token",value = "商家token",required = true),
            @ApiImplicitParam(paramType = "query",name = "storeId",value = "店铺ID",required = true)
    })
    @ApiResponse(description = "并没有真正删除，只是将state = 0(注销)")
    @PutMapping("/delete")
    public SysResult deleteStore(@RequestParam("storeId") String sid, HttpServletRequest request) {
        try {
            String storeId = storeService.delete(sid, request);
            Map<String, Object> map = new HashMap<>();
            map.put("storeId", storeId);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(), SysResultEnum.SUCCESS.getName(), map);
        } catch (MsgException e) {
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        }
    }

    /**
     * 根据ids查询商店信息，如果ids.size()等于0即查询全部商店信息。
     * @return
     */
    @SaCheckLogin
    @SaCheckPermission("store-query")
    @ApiOperation("根据店铺ids数组查询商店信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "currPage",value = "当前页",defaultValue = "1"),
            @ApiImplicitParam(paramType = "query",name = "size",value = "一页多少条记录",defaultValue = "10")
    })
    @ApiResponse(description = "ids数组长度为空，执行查询所有数据")
    @PostMapping("/queryByIds")
    public SysResult queryStore(@RequestParam(value = "currPage",defaultValue = "1") Integer currPage,
                                @RequestParam(value = "size",defaultValue = "10") Integer size,
                                @RequestBody List<String> ids){
        try{
            Page<Store> store = storeService.queryStore(currPage, size, ids);
            Map<String,Object>map = new HashMap<>();
            map.put("page",store);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(),SysResultEnum.SUCCESS.getName(),map);
        } catch (MsgException e) {
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        }
    }

    @SaCheckLogin
    @SaCheckPermission("store-query")
    @ApiOperation("通过店铺名查询店铺信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "currPage",value = "当前页",defaultValue = "1"),
            @ApiImplicitParam(paramType = "query",name = "size",value = "一页多少条记录",defaultValue = "10"),
            @ApiImplicitParam(paramType = "query",name = "storeName",value = "店铺名",required = true)
    })
    @GetMapping("queryByName")
    public SysResult queryStoreByName(@RequestParam(value = "currPage",defaultValue = "1") Integer currPage,
                                      @RequestParam(value = "size",defaultValue = "10") Integer size,
                                      @RequestParam("storeName") String name){
        try{
            Page<Store> store = storeService.queryStoreByName(currPage, size, name);
            Map<String,Object>map = new HashMap<>();
            map.put("page",store);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(),SysResultEnum.SUCCESS.getName(),map);
        } catch (MsgException e) {
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        }
    }

    @SaCheckLogin
    @ApiOperation("商人查询自己的商店")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "currPage",value = "当前页",defaultValue = "1"),
            @ApiImplicitParam(paramType = "query",name = "size",value = "一页多少条记录",defaultValue = "10"),
            @ApiImplicitParam(paramType = "header",name = "token",value = "商家token",required = true)
    })
    @GetMapping("/queryMyStore")
    public SysResult queryMyStore(@RequestParam(value = "currPage",defaultValue = "1") Integer currPage,
                                  @RequestParam(value = "size",defaultValue = "10") Integer size,
                                  HttpServletRequest request){
        try{
            StpUtil.checkRoleOr(Businessman.Role, Administrator.Role);
            String idAsString = StpUtil.getLoginIdAsString();
            String[] s = idAsString.split("_");
            String businessmanId = s[1];
            Page<Store>page = new Page<Store>(currPage,size);
            QueryWrapper<Store>queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("businessmanId",businessmanId);
            Page<Store> stores = storeService.page(page,queryWrapper);
            Map<String,Object>map = new HashMap<>();
            map.put("stores",stores);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(),SysResultEnum.SUCCESS.getName(),map);
        } catch (MsgException e) {
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(), e.getMessage(), null);
        } catch (Exception e) {
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(), e.getMessage(), null);
        }
    }
}