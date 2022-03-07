/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: UserServiceImpl
 * Author:   Mr.WuGuoZhi
 * Date:     2022/2/23 18:36
 * Description: 用户Service层
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarker.user.service.implement;

import ch.qos.logback.core.util.TimeUtil;
import com.bnuz.electronic_supermarker.common.db.DS;
import com.bnuz.electronic_supermarker.common.exception.MsgException;
import com.bnuz.electronic_supermarker.common.javaBean.User;
import com.bnuz.electronic_supermarker.common.utils.CalendarUtils;
import com.bnuz.electronic_supermarker.common.utils.JwtUtil;
import com.bnuz.electronic_supermarker.common.utils.MD5Utils;
import com.bnuz.electronic_supermarker.user.dao.UserDao;
import com.bnuz.electronic_supermarker.user.dto.UserDto;
import com.bnuz.electronic_supermarker.user.exception.PasswordErrorException;
import com.bnuz.electronic_supermarker.user.service.UserService;
import io.netty.util.internal.StringUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userdao;

    @Override
    public boolean checkAccount(String account) {

        User user = userdao.checkAccount(account);
        if(user != null){
            throw new MsgException("用户名已被注册");
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void registerUser(User user, String password2) {
        //账号能否使用
        if(user.getAccount() == null || user.getAccount().equals("")){
            throw new MsgException("用户名不能为空");
        }
        if(user.getPassword() == null || user.getPassword().equals("")){
            throw new MsgException("密码不能为空");
        }
        checkAccount(user.getAccount());
        //密码一致性校验
        if (!user.getPassword().equals(password2)) {
            throw new PasswordErrorException("两次密码输入不一致");
        }
        try{
            user.setId(UUID.randomUUID().toString());
            user.setPassword(MD5Utils.md5(user.getPassword()));
            user.setState(1);
//            DateTime datetime = new DateTime();        //2022-03-02T21:07:45.792+08:00
            user.setCreateTime(CalendarUtils.getDateTime());
            userdao.insertUser(user);
        }catch(Exception e){
            e.printStackTrace();
            throw new MsgException("注册失败");
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Map<String,Object> login(String account,String password) {
        User userDB = userdao.selectUsersByAccountPassword(account,MD5Utils.md5(password));
        if(userDB != null){

            //添加负载 Payload
            Map<String,String> map = new HashMap<>();
            map.put("account",userDB.getAccount());
            map.put("id",userDB.getId());
            String token = JwtUtil.createJwtToken(map, 120);//设置负载，设置token过期时间
            Map<String,Object>resultMap = new HashMap<>();
            resultMap.put("user",userDB);
            resultMap.put("token",token);
            return resultMap;
        }
        throw new MsgException("登陆失败,用户名或密码错误");
    }
}