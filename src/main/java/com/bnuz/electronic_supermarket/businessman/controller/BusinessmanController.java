/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: BusinessmanController
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/15 21:51
 * Description: 商人控制类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.businessman.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnuz.electronic_supermarket.businessman.dto.BusinessmanDto;
import com.bnuz.electronic_supermarket.businessman.service.BusinessmanService;
import com.bnuz.electronic_supermarket.common.dto.SysResult;
import com.bnuz.electronic_supermarket.common.enums.SysResultEnum;
import com.bnuz.electronic_supermarket.common.exception.MsgException;
import com.bnuz.electronic_supermarket.common.javaBean.Administrator;
import com.bnuz.electronic_supermarket.common.javaBean.Businessman;
import com.bnuz.electronic_supermarket.common.javaBean.Orderr;
import com.bnuz.electronic_supermarket.order.enums.OrderrEnum;
import com.bnuz.electronic_supermarket.order.service.Impl.OrderrServiceImpl;
import com.sun.org.apache.xpath.internal.operations.Or;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "商家请求")
@RestController
public class BusinessmanController {

    @Autowired
    private BusinessmanService businessmanService;

    @Autowired
    private OrderrServiceImpl orderrService;

    /**
     * 注册
     * @param manDto
     * @return
     */
    @ApiOperation(value = "注册")
    @ApiResponse(description = "返回用户ID",ref = "String")
    @PostMapping("/register/businessman")
    public SysResult register(@RequestBody BusinessmanDto manDto){
        try{
            String bid = this.businessmanService.register(manDto);
            Map<String,Object> result = new HashMap<>();
            result.put("businessmanId",bid);
            return new SysResult(SysResultEnum.Created.getIndex(),SysResultEnum.SUCCESS.getName(),result);
        }catch (MsgException e){
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(),e.getMessage(),null);
        }catch (Exception e){
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(),e.getMessage(),null);
        }
    }

    /**
     * 登录
     * @param account
     * @param password
     * @return
     */
    @ApiOperation("登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "account",value = "账号",dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "password",value = "密码")
    })
    @ApiResponse(description = "返回用户ID和商家token")
    @GetMapping("/login/businessman")
    public SysResult login(@RequestParam("account") String account,@RequestParam("password") String password){
        try{
            Map<String, Object> map = this.businessmanService.login(account, password);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(),SysResultEnum.SUCCESS.getName(),map);
        }catch (MsgException e){
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(),e.getMessage(),null);
        }catch (Exception e){
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(),e.getMessage(),null);
        }
    }

    /**
     * 获取商家信息
     * @param ids
     * @param request
     * @return
     */
    @PostMapping("/businessman/info")
    @ApiOperation("获取商家个人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "token",value = "商家token"),
            @ApiImplicitParam(name = "ids",value = "商家id数组")
    })
    @ApiResponse(description = "请求body填写商家ids数组。返回notFoundIds数组(找不到数据的商家ID),businessmen数组")
    @SaCheckLogin
    public SysResult getByIds(@RequestBody List<String> ids, HttpServletRequest request){
        try{
//            StpUtil.checkRoleOr(Businessman.Role, Administrator.Role);
            StpUtil.checkPermission("businessman-query");
            Map<String, Object> data = this.businessmanService.getByIds(ids,request);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(),SysResultEnum.SUCCESS.getName(),data);
        }catch (MsgException e){
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(),e.getMessage(),null);
        }catch (Exception e){
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(),e.getMessage(),null);
        }
    }


    /**
     *     //分页查看自己的商店下的所有订单(订单状态要管)
     * @param storeId
     * @param state
     * @param currPage
     * @param size
     * @return
     */
    @GetMapping("/businessman/{storeId}/orderr")
    @ApiOperation("获取商家某店铺下某种状态的订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "token",value = "商家token"),
            @ApiImplicitParam(paramType = "path",name = "storeId",value = "商家的店铺ID"),
            @ApiImplicitParam(paramType = "query",name = "state",value = "订单状态")
    })
    @ApiResponse(description = "state参数不填就默认查询所有订单")
    @SaCheckLogin
    public SysResult getOrderByStoreId(@PathVariable("storeId") String storeId,
                                       @RequestParam(value = "state",defaultValue = "1024") Integer state,
                                       @RequestParam(value = "currPage", defaultValue = "1") Integer currPage,
                                       @RequestParam(value = "size", defaultValue = "10") Integer size){
        try{
            StpUtil.checkPermission("orderr-query");
//           select * from orderr where storeId = storeId and state = state order by createTime desc
            //校验state,没有校验店铺ID是不是自己的
            if(OrderrEnum.getName(state) == null && state != 1024) throw new MsgException("订单状态不存在");
            QueryWrapper<Orderr>queryWrapper = new QueryWrapper<>();
            if(state == 1024){
                //查询所有最新的所有订单
                queryWrapper.eq("storeId",storeId).orderByDesc("createTime");
            }else{
                queryWrapper.eq("storeId",storeId).eq("state",state).orderByDesc("createTime");
            }
            Page<Orderr> page = orderrService.page(new Page<Orderr>(currPage, size), queryWrapper);
            Map<String, Object> data = new HashMap<>();
            data.put("page",page);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(),SysResultEnum.SUCCESS.getName(),data);
        }catch (MsgException e){
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(),e.getMessage(),null);
        }catch (Exception e){
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(),e.getMessage(),null);
        }
    }

}