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
import com.bnuz.electronic_supermarket.businessman.dto.BusinessmanDto;
import com.bnuz.electronic_supermarket.businessman.service.BusinessmanService;
import com.bnuz.electronic_supermarket.common.dto.SysResult;
import com.bnuz.electronic_supermarket.common.enums.SysResultEnum;
import com.bnuz.electronic_supermarket.common.exception.MsgException;
import com.bnuz.electronic_supermarket.common.javaBean.Administrator;
import com.bnuz.electronic_supermarket.common.javaBean.Businessman;
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
            e.printStackTrace();
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(),e.getMessage(),null);
        }catch (Exception e){
            e.printStackTrace();
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
            e.printStackTrace();
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(),e.getMessage(),null);
        }catch (Exception e){
            e.printStackTrace();
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(),e.getMessage(),null);
        }
    }

    /**
     * 获取个人信息
     * @param ids
     * @param request
     * @return
     */
    @PostMapping("/businessman/info")
    @ApiOperation("获取用户信息")
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
            e.printStackTrace();
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(),e.getMessage(),null);
        }catch (Exception e){
            e.printStackTrace();
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(),e.getMessage(),null);
        }
    }
}