/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: UserController
 * Author:   Mr.WuGuoZhi
 * Date:     2022/2/23 22:19
 * Description: 用户控制器
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 * 功能点列表：查询、修改、添加个人信息
 */


package com.bnuz.electronic_supermarket.user.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.bnuz.electronic_supermarket.common.dto.SysResult;
import com.bnuz.electronic_supermarket.common.enums.SysResultEnum;
import com.bnuz.electronic_supermarket.common.exception.MsgException;
import com.bnuz.electronic_supermarket.common.javaBean.User;
import com.bnuz.electronic_supermarket.user.dto.UserRegisterDTO;
import com.bnuz.electronic_supermarket.user.service.UserService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

//@CrossOrigin        //跨域
@Api(tags = "用户请求")
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param
     * @return
     */
    @ApiOperation(value = "用户注册")
    @ApiImplicitParam(name = "userDto",value = "用户Dto")
    @PostMapping("/register/user")
    public SysResult registerUser(@RequestBody UserRegisterDTO userDto){
        try{
            String uid = this.userService.registerUser(userDto);
            Map<String,Object> result = new HashMap<>();
            result.put("userId",uid);
            return new SysResult(SysResultEnum.Created.getIndex(),SysResultEnum.SUCCESS.getName(),result);
        }catch (MsgException e){
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(),e.getMessage(),null);
        }catch (Exception e){
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(),e.getMessage(),null);
        }
    }

    /**
     * 用户登陆 返回sa-token生成的token
     * @param account
     * @param password
     * @return
     */
    @ApiOperation(value = "用户登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "account",value = "账号",required = true),
            @ApiImplicitParam(paramType = "query",name = "password",value = "密码",required = true)
    })
    @GetMapping("/login/user")
    public SysResult login(@RequestParam("account") String account,
                                   @RequestParam("password") String password){
        try{
            Map<String,String> result = this.userService.login(account,password);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(),"登录成功!",result);
        }catch (MsgException e){
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(),e.getMessage(),null);
        }catch (Exception e){
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(),e.getMessage(),null);
        }
    }

    /**
     * 获取用户信息,HttpRequest的header需要放token
     * @param userId
     * @return
     */
    @ApiOperation(value = "通过用户ID获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",name = "userId",value = "用户ID",required = true),
            @ApiImplicitParam(paramType = "header",name = "token",value = "用户token",required = true)
    })
    @GetMapping("/user/info/{userId}")
    @SaCheckLogin
    public SysResult getUserInfo(@PathVariable("userId") String userId,HttpServletRequest request){
        try{
//            StpUtil.checkRoleOr(User.Role, Administrator.Role);
            StpUtil.checkPermission("user-query");
            User user = this.userService.getInfo(userId,request);
            Map<String,Object> result = new HashMap<>();
            result.put("user",user);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(),SysResultEnum.SUCCESS.getName(),result);
        }catch (MsgException e){
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(),e.getMessage(),null);
        }catch (Exception e){
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(),e.getMessage(),null);
        }
    }

}