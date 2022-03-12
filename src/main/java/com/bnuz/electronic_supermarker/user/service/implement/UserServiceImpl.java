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
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnuz.electronic_supermarker.common.db.DS;
import com.bnuz.electronic_supermarker.common.enums.UserTypeEnum;
import com.bnuz.electronic_supermarker.common.exception.MsgException;
import com.bnuz.electronic_supermarker.common.javaBean.User;
import com.bnuz.electronic_supermarker.common.utils.*;
import com.bnuz.electronic_supermarker.user.dao.UserDao;
import com.bnuz.electronic_supermarker.user.dto.UserDto;
import com.bnuz.electronic_supermarker.user.enums.UserStateEnum;
import com.bnuz.electronic_supermarker.user.exception.PasswordErrorException;
import com.bnuz.electronic_supermarker.user.service.UserService;
import com.google.gson.Gson;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private UserDao userdao;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean checkAccount(String account){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("account",account);
        try{
            Long i = this.userdao.selectCount(wrapper);
            if(i != 0L){
                throw new MsgException("用户名已被注册");
            }
        }catch (MsgException e){
            throw e;
        }catch (Exception e){
            throw e;
        }
        return true;
    }

    /**
     * 用户注册
     * @param user
     * @param password2
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void registerUser(User user, String password2) {
        //账号能否使用
        try{
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
            user.setId(UUID.randomUUID().toString());   //UUID
            user.setPassword(MD5Utils.md5(user.getPassword()));   //MD5加密 密码
            user.setState(UserStateEnum.USING.getIndex());       //用户状态
            user.setCreateTime(CalendarUtils.getDateTime());     //创建时间
            userdao.insert(user);                                 //BaseMapper<User>
        }catch (MsgException e){
            throw e;
        }catch (PasswordErrorException e){
            throw e;
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * 登陆用户，返回token。redis存放用户信息
     * @param account
     * @param password
     * @return token
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Map<String,String> login(String account,String password) {
        try{
            QueryWrapper<User> wrapper = new QueryWrapper();
            wrapper.eq("account",account).eq("password",MD5Utils.md5(password));
            User userDB = userdao.selectOne(wrapper);
//            User userDB = userdao.selectUsersByAccountPassword(account,MD5Utils.md5(password));
            if(userDB != null){
                //添加负载 Payload 不要放用户敏感信息
                Map<String,String> Payload = new HashMap<>();
                Payload.put("id",userDB.getId());
                Payload.put("account",userDB.getAccount());
                Payload.put("type", UserTypeEnum.USER.getName());
                String token = JwtUtil.createJwtToken(Payload, 120);//设置负载，设置token过期时间 120minutes
                redisTemplate.opsForValue().set(User.class.getSimpleName() + "_"+ userDB.getId(), GsonUtil.getGson().toJson(userDB));
                Map<String,String>map = new HashMap<>();
                map.put("token",token);
                map.put("userId",userDB.getId());
                return map;
            }
            throw new MsgException("登陆失败,用户名或密码错误");
        }catch (MsgException e){
            throw e;
        }catch(RedisConnectionFailureException e){
            log.warn(e.getMessage(),e);
            throw new MsgException("Redis缓存连接失败!");
        } catch (Exception e){
            throw e;
        }
    }


    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    @Override
    public User getInfo(String userId, HttpServletRequest request) {
        Boolean exists = null;
        //token里面的userId跟请求的userId不一致。
        if(JudgeUserIdUtil.Judge(request,userId) == false){
            throw new MsgException("非法访问,请验证token或者用户ID");
        }
        try {
            //先从redis里面找，参考supermaket项目。
            //登陆拦截器已经做了验证了。
            exists = this.redisTemplate.hasKey(User.class.getSimpleName() + "_" + userId);
        }catch (RedisConnectionFailureException e) {
            //Redis连接失效了，直接数据库查。
            log.warn(e.getMessage(),e);
            return this.userdao.selectById(userId);
        }
        try{
            //Redis连接成功，但是没有在缓存中找到对应的用户信息。数据库查找然后存到redis
            User user = null;
            if(exists == null || !exists){
                user = this.userdao.selectById(userId);
                //数据库查不到
                if(user == null){
                    throw new MsgException("查无此人");
                }
                this.redisTemplate.opsForValue().set(User.class.getSimpleName() + "_" + user.getId(),GsonUtil.getGson().toJson(user));
                return user;
            }else{
                //从缓存中取出用户信息。
                String jsonData = this.redisTemplate.opsForValue().get(User.class.getSimpleName() + "_" + userId);
                user = GsonUtil.getGson().fromJson(jsonData,User.class);
                return user;
            }
        }catch (MsgException e){
            throw e;
        }catch (Exception e){
            throw e;
        }
    }











}