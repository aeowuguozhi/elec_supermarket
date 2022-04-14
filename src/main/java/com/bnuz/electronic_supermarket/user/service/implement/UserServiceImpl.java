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

package com.bnuz.electronic_supermarket.user.service.implement;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnuz.electronic_supermarket.common.enums.UserTypeEnum;
import com.bnuz.electronic_supermarket.common.exception.MsgException;
import com.bnuz.electronic_supermarket.common.javaBean.User;
import com.bnuz.electronic_supermarket.common.utils.*;
import com.bnuz.electronic_supermarket.user.dao.UserDao;
import com.bnuz.electronic_supermarket.user.dto.UserRegisterDTO;
import com.bnuz.electronic_supermarket.user.enums.UserStateEnum;
import com.bnuz.electronic_supermarket.user.exception.PasswordErrorException;
import com.bnuz.electronic_supermarket.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * 检查账号是否被注册
     * @param account
     * @return
     */
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
            LOGGER.info(e.getMessage());
            throw e;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            throw e;
        }
        return true;
    }

    /**
     * 用户注册
     * @param userDto
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public String registerUser(UserRegisterDTO userDto) {
        //账号能否使用
        try{
            if(userDto.getAccount() == null || userDto.getAccount().equals("")){
                throw new MsgException("用户名不能为空");
            }
            if(userDto.getPassword() == null || userDto.getPassword().equals("")){
                throw new MsgException("密码不能为空");
            }
            checkAccount(userDto.getAccount());
            User user = new User();
            userDto2user(userDto,user);
            user.setId(UUID.randomUUID().toString());   //UUID
            user.setPassword(MD5Utils.md5(user.getPassword()));   //MD5加密 密码
            user.setState(UserStateEnum.USING.getIndex());       //用户状态
            user.setCreateTime(CalendarUtils.getDateTime());     //创建时间
            userdao.insert(user);                                 //BaseMapper<User>
            return user.getId();
        }catch (MsgException e){
            LOGGER.info(e.getMessage());
            throw e;
        }catch (PasswordErrorException e){
            LOGGER.info(e.getMessage());
            throw e;
        } catch (Exception e){
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    private void userDto2user(UserRegisterDTO userDto, User user) {
        user.setWechatId(userDto.getWechatId());
        user.setAccount(userDto.getAccount());
        user.setAddress(userDto.getAddress());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setHeadImage(userDto.getHeadImage());
        user.setIdCard(userDto.getIdCard());
        user.setNickName(userDto.getNickName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setRealName(userDto.getRealName());
        user.setNote(userDto.getNote());
    }

    /**
     * 用户登陆，返回token。redis存放用户信息
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
            if(userDB == null){
                throw new MsgException("登陆失败,用户名或密码错误");
            }
            // JWT ：添加负载 Payload 不要放用户敏感信息
//            Map<String,String> Payload = new HashMap<>();
//            Payload.put("id",userDB.getId());
//            Payload.put("account",userDB.getAccount());
//            Payload.put("type", UserTypeEnum.USER.getName());
//            String token = JwtUtil.createJwtToken(Payload, 120);//设置负载，设置token过期时间 120minutes
            StpUtil.login(User.myPrefix + "_" + userDB.getId());
            StpUtil.getSession().set("type",UserTypeEnum.USER.getName());
            StpUtil.getSession().set(UserTypeEnum.USER.getName(),userDB);
            redisTemplate.opsForValue().set(User.myPrefix + "_"+ userDB.getId(), GsonUtil.getGson().toJson(userDB));
            Map<String,String>map = new HashMap<>();
            map.put("token", StpUtil.getTokenValue());
            map.put("userId",userDB.getId());
            return map;
        }catch (MsgException e){
            LOGGER.info(e.getMessage());
            throw e;
        }catch(RedisConnectionFailureException e){
            LOGGER.error("Redis缓存连接失败!");
            throw new MsgException("Redis缓存连接失败!");
        } catch (Exception e){
            LOGGER.error(e.getMessage());
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
        try {
            //token里面的userId跟请求的userId不一致。鉴权：自己token的用户只能查看自己的信息。    sa-token也要判断一下。
            //sa-token  鉴权  loginId由prefix + "_" + UUID构成
            String[] loginId = StpUtil.getLoginIdAsString().split("_");
            if(!loginId[1].equals(userId)){
                //比如输入token正确，输入userId不正确，但是我可以在不调用sql查询的情况下，检查userId的正确性。
                throw new MsgException("用户ID错误");
            }
            //先从redis里面找，参考supermarket项目。
            //登陆拦截器已经做了验证了。
            exists = this.redisTemplate.hasKey(User.class.getSimpleName() + "_" + userId);
        }catch (RedisConnectionFailureException e) {
            //Redis连接失效了，直接数据库查。
            LOGGER.error("Redis缓存连接失败!");
            return this.userdao.selectById(userId);
        }catch (MsgException e){
            LOGGER.info(e.getMessage());
            throw e;
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
                this.redisTemplate.opsForValue().set(User.myPrefix + "_" + user.getId(),GsonUtil.getGson().toJson(user));
                return user;
            }else{
                //从缓存中取出用户信息。
                String jsonData = this.redisTemplate.opsForValue().get(User.myPrefix + "_" + userId);
                user = GsonUtil.getGson().fromJson(jsonData,User.class);
                return user;
            }
        }catch (MsgException e){
            LOGGER.info(e.getMessage());
            throw e;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            throw e;
        }
    }
}