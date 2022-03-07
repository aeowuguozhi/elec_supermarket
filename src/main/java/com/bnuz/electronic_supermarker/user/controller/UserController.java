/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: UserController
 * Author:   Mr.WuGuoZhi
 * Date:     2022/2/23 22:19
 * Description: 用户控制器
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarker.user.controller;

import com.bnuz.electronic_supermarker.common.dto.SysResult;
import com.bnuz.electronic_supermarker.common.exception.MsgException;
import com.bnuz.electronic_supermarker.common.javaBean.User;
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

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public SysResult registerUser(@RequestBody UserRegisterDto userdata, @RequestParam("password2") String password2
                                  ){
    //
        try{
            User user = new User();
            user.setPassword(userdata.getPassword());
            user.setAccount(userdata.getAccount());
            user.setPhoneNumber(userdata.getPhoneNumber());
            this.userService.registerUser(user,password2);
            return new SysResult(200,"OK",user.getId());
        }catch (MsgException e){
            e.printStackTrace();
            return new SysResult(201,e.getMessage(),e);
        }catch (Exception e){
            e.printStackTrace();
            return new SysResult(500,e.getMessage(),e);
        }
    }

    @GetMapping("/login")
    public SysResult login(@RequestParam("account") String account,
                                   @RequestParam("password") String password){
        try{
            Map<String, Object> objectMap = userService.login(account,password);
            return new SysResult(200,"登录成功!",objectMap);
        }catch (Exception e){
            e.printStackTrace();
            return new SysResult(500,e.getMessage(),e);
        }
    }

}