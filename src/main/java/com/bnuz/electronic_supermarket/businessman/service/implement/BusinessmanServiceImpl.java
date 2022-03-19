/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: BusinessmanServiceImpl
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/15 22:06
 * Description: 商家服务层实现类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.businessman.service.implement;

import cn.dev33.satoken.stp.StpUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnuz.electronic_supermarket.businessman.dao.BusinessmanDao;
import com.bnuz.electronic_supermarket.businessman.dto.BusinessmanDto;
import com.bnuz.electronic_supermarket.businessman.service.BusinessmanService;
import com.bnuz.electronic_supermarket.common.enums.UserTypeEnum;
import com.bnuz.electronic_supermarket.common.exception.MsgException;
import com.bnuz.electronic_supermarket.common.javaBean.Businessman;
import com.bnuz.electronic_supermarket.common.utils.*;
import com.bnuz.electronic_supermarket.user.enums.UserStateEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class BusinessmanServiceImpl extends ServiceImpl<BusinessmanDao, Businessman> implements BusinessmanService{

    @Autowired
    BusinessmanDao businessmanDao;

    @Autowired
    StringRedisTemplate redisTemplate;

    private final static Logger LOGGER = LoggerFactory.getLogger(BusinessmanServiceImpl.class);

    /**
     * 商家注册
     * @param businessmanDto
     * @return businessmanId
     */
    @Override
    public String register(BusinessmanDto businessmanDto) {
        try{
            //没有空格 存在字符
            if(StringUtils.containsWhitespace(businessmanDto.getAccount()) || !StringUtils.hasText(businessmanDto.getAccount())){
                throw new MsgException("账号不能为空且不能含空格");
            }
            if(StringUtils.containsWhitespace(businessmanDto.getPassword()) || !StringUtils.hasText(businessmanDto.getPassword())){
                throw new MsgException("密码不能为空且不能含空格");
            }
            if(StringUtils.containsWhitespace(businessmanDto.getPhoneNumber()) || !StringUtils.hasText(businessmanDto.getPhoneNumber())){
                throw new MsgException("手机号码不能为空且不能含空格");
            }
            QueryWrapper<Businessman> wrapper = new QueryWrapper<>();
            wrapper.eq("account",businessmanDto.getAccount());
            if(this.businessmanDao.selectCount(wrapper) != 0L){
                throw new MsgException("账号已经被注册");
            }
            Businessman man = new Businessman();
            Dto2man(businessmanDto,man);
            man.setPassword(MD5Utils.md5(man.getPassword()));
            man.setState(UserStateEnum.USING.getIndex());
            man.setId(UUID.randomUUID().toString());
            man.setCreateTime(LocalDateTimeUtils.getLocalDateTime());
            this.businessmanDao.insert(man);
            return man.getId();
        }catch (MsgException e){
            LOGGER.info(e.getMessage());
            throw e;
        }catch(Exception e){
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    /** 
     *  商家进行登录，返回id，token
     * @param account
     * @param password
     * @return
     */
    @Override
    public Map<String, Object> login(String account, String password) {
        try{
            QueryWrapper<Businessman> wrapper = new QueryWrapper<>();
            wrapper.eq("account",account).eq("password",MD5Utils.md5(password));
            Businessman businessman = businessmanDao.selectOne(wrapper);
            if(businessman == null){
                throw new MsgException("登录失败，请检查用户名/密码");
            }
            //数据库查询成功，放到redis里面
            this.redisTemplate.opsForValue().set(Businessman.class.getSimpleName()+"_"+businessman.getId(), GsonUtil.getGson().toJson(businessman));
//            Map<String, String> payload = new HashMap<>();
//            payload.put("businessmanId", businessman.getId());
//            String token = JwtUtil.createJwtToken(payload, 120);
            StpUtil.login(Businessman.myPrefix + "_" + businessman.getId());
            StpUtil.getTokenInfo().setLoginType(UserTypeEnum.BUSINESSMAN.getName());
            Map<String,Object>result = new HashMap<>();
            result.put("token",StpUtil.getTokenValue());
            result.put("businessmanId",businessman.getId());
            return result;
        }catch (MsgException e){
            LOGGER.info(e.getMessage());
            throw e;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 应该从redis里面查，然后从db查
     * @param ids
     * @return 查询成功的businessmen，查询失败的notFoundIds
     */
    @Override
    public Map<String,Object> getByIds(List<String> ids, HttpServletRequest request) {
        try{
            //登录拦截器已经验证token了。 从token里取出自己商家ID
            String token = request.getHeader("token");
            String[] myId = StpUtil.getLoginId().toString().split("_");
            List<String>notFoundIds = new ArrayList<>();
            int length = ids.size();
            String key,json_data,id;
            ValueOperations<String, String> operations = this.redisTemplate.opsForValue();
            List<Businessman> result = new ArrayList<>();
            Map<String,Object> fin = new HashMap<>();
            //遍历ids
            for(int i = 0;i < length;i++){
                id = ids.get(i);
                //没有权限查询别人的ID,其实这里应该用selectById()就好了
                if(!myId[1].equals(id)){
                    notFoundIds.add(id);
                    continue;
                }
                key = Businessman.class.getSimpleName() + "_" + id;
                if(this.redisTemplate.hasKey(key)){
                    //redis里面存在这个key
                    json_data = operations.get(key);
                    Businessman businessman = GsonUtil.getGson().fromJson(json_data, Businessman.class);
                    result.add(businessman);
                }else{
                    //从数据库中查询,查到的添加到redis
                    Businessman man = this.businessmanDao.selectById(id);
                    if(man == null){
                        notFoundIds.add(id);
                    }
                    //添加到查询结果里
                    result.add(man);
                    //添加到redis
                    operations.set(Businessman.class.getSimpleName()+"_"+man.getId(),GsonUtil.getGson().toJson(man));
                }
            }
            fin.put("businessmen",result);
            fin.put("notFoundIds",notFoundIds);
            return fin;
        }catch (RedisConnectionFailureException e){
            LOGGER.error(e.getMessage());
            throw e;
        } catch (MsgException e){
            LOGGER.info(e.getMessage());
            throw e;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            throw e;
        }
    }


    private void Dto2man(BusinessmanDto businessmanDto, Businessman man) {
        man.setAccount(businessmanDto.getAccount());
        man.setHeadImage(businessmanDto.getHeadIamge());
        man.setIdCard(businessmanDto.getIdCard());
        man.setIdCardBackPict(businessmanDto.getIdCardBackPict());
        man.setIdCardFontPict(businessmanDto.getIdCardFontPict());
        man.setPassword(businessmanDto.getPassword());
        man.setPhoneNumber(businessmanDto.getPhoneNumber());
        man.setRealName(businessmanDto.getRealName());
        man.setWechatId(businessmanDto.getWechatId());
    }
}