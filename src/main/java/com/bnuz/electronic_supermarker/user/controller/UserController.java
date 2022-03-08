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


package com.bnuz.electronic_supermarker.user.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.bnuz.electronic_supermarker.common.dto.SysResult;
import com.bnuz.electronic_supermarker.common.enums.SysResultEnum;
import com.bnuz.electronic_supermarker.common.exception.MsgException;
import com.bnuz.electronic_supermarker.common.javaBean.User;
import com.bnuz.electronic_supermarker.common.utils.JwtUtil;
import com.bnuz.electronic_supermarker.common.utils.MD5Utils;
import com.bnuz.electronic_supermarker.user.dto.UserDto;
import com.bnuz.electronic_supermarker.user.dto.UserRegisterDto;
import com.bnuz.electronic_supermarker.user.service.UserService;
import com.bnuz.electronic_supermarker.user.service.implement.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

//@CrossOrigin        //跨域
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param user
     * @param password2
     * @return
     */
    @PostMapping("/register/user")
    public SysResult registerUser(@RequestBody User user, @RequestParam("password2") String password2
                                  ){
    //
        try{
            this.userService.registerUser(user,password2);
            Map<String,Object> result = new HashMap<>();
            result.put("userId",user.getId());
            return new SysResult(SysResultEnum.Created.getIndex(),"OK",result);
        }catch (MsgException e){
            e.printStackTrace();
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(),e.getMessage(),null);
        }catch (Exception e){
            e.printStackTrace();
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(),e.getMessage(),null);
        }
    }

    /**
     * 用户登陆 返回JWT生成的token
     * @param account
     * @param password
     * @return
     */
    @GetMapping("/login/user")
    public SysResult login(@RequestParam("account") String account,
                                   @RequestParam("password") String password){
        try{
            String token = this.userService.login(account,password);
            Map<String,Object> result = new HashMap<>();
            result.put("token",token);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(),"登录成功!",result);
        }catch (MsgException e){
            e.printStackTrace();
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(),e.getMessage(),null);
        }catch (Exception e){
            e.printStackTrace();
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(),e.getMessage(),null);
        }
    }

    /**
     * 获取用户信息  先从redis缓存里找，，，，，待定
     * @param request
     * @return
     */
    @GetMapping("/user/info")
    public SysResult getUserInfo(HttpServletRequest request){
        try{
            //TODO 先从redis里面找，参考supermaket项目。
            //登陆拦截器已经做了验证了。
            String token = request.getHeader("token");
            DecodedJWT decodedJWT = JwtUtil.verifyToken(token);
            String userId = decodedJWT.getClaim("id").asString();
            String account = decodedJWT.getClaim("account").asString();
            User user = this.userService.getById(userId);
            Map<String,Object> result = new HashMap<>();
            result.put("user",user);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(),SysResultEnum.SUCCESS.getName(),result);
        }catch (MsgException e){
            e.printStackTrace();
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(),e.getMessage(),null);
        }catch (Exception e){
            e.printStackTrace();
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(),e.getMessage(),null);
        }
    }

}