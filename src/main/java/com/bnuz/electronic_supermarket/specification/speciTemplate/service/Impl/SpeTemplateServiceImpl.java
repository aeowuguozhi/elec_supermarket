/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: SpeTemplateServiceImpl
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/17 15:28
 * Description: 规格模板服务实现类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.specification.speciTemplate.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnuz.electronic_supermarket.common.dto.SysResult;
import com.bnuz.electronic_supermarket.common.enums.SysResultEnum;
import com.bnuz.electronic_supermarket.common.exception.MsgException;
import com.bnuz.electronic_supermarket.common.javaBean.Specifictemplate;
import com.bnuz.electronic_supermarket.common.utils.GsonUtil;
import com.bnuz.electronic_supermarket.specification.speci.dao.SpecificationMapper;
import com.bnuz.electronic_supermarket.specification.speciTemplate.dao.SpeTemplateMapper;
import com.bnuz.electronic_supermarket.specification.speciTemplate.service.SpeTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SpeTemplateServiceImpl extends ServiceImpl<SpeTemplateMapper, Specifictemplate> implements SpeTemplateService {

    @Autowired
    private SpeTemplateMapper templateDao;

    private final static Logger LOGGER = LoggerFactory.getLogger(SpeTemplateServiceImpl.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 创建规格模板，需要商家token，规格模板用Map表示,HashMap<String,String> map,map.put(specification,value)
     *
     * @param map
     * @return
     */
    @Override
    public String create(Map<String, String> map) {
        try {
            String json = GsonUtil.getGson().toJson(map);
            Specifictemplate template = new Specifictemplate();
            template.setId(UUID.randomUUID().toString());
            template.setProduct_id(null);
            template.setProductName(null);
            template.setSpecifTemplate(json);
            //插入
            int insert = templateDao.insert(template);
            if (insert <= 0) {
                throw new MsgException("创建模板失败");
            }
            String key = Specifictemplate.class.getSimpleName() + "_" + template.getId();
            redisTemplate.opsForValue().set(key, GsonUtil.getGson().toJson(template));
            return template.getId();
        } catch (MsgException e) {
            LOGGER.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public String delete(String templateId) {
        try {
            //delete data in Database
            int i = templateDao.deleteById(templateId);
            if (i <= 0) {
                throw new MsgException("删除失败");
            }
            //delete data in redis
            String key = Specifictemplate.class.getSimpleName() + "_" + templateId;
            if (redisTemplate.hasKey(key)) {
                redisTemplate.delete(key);
            }
            return templateId;
        } catch (MsgException e) {
            LOGGER.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public Specifictemplate queryById(String tid) {
        try {
            //get data from redis if existed
            String key = Specifictemplate.class.getSimpleName() + "_" + tid;
            if(redisTemplate.hasKey(key)){
                String json = redisTemplate.opsForValue().get(key);
                Specifictemplate template = GsonUtil.getGson().fromJson(json, Specifictemplate.class);
                return template;
            }
            //get data from database
            Specifictemplate specifictemplate = templateDao.selectById(tid);
            //set the data which is from database into redis,过期时间60minutes
            redisTemplate.opsForValue().set(key,GsonUtil.getGson().toJson(specifictemplate),60, TimeUnit.MINUTES);
            return specifictemplate;
        } catch (MsgException e) {
            LOGGER.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 将创建好的商品的ID和商品的名字回填到规格模板表中
     * @param productId
     * @param productName
     * @param templateId
     * @return
     */
    @Override
    public SysResult saveBackProductId(String productId, String productName, String templateId) {
        try{
            //TODO unTest
            //从数据库中取出数据
            QueryWrapper<Specifictemplate>queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",templateId);
            Specifictemplate template = templateDao.selectOne(queryWrapper);
            //回填商品数据
            template.setProductName(productName);
            template.setProduct_id(productId);
            //更新数据库
            UpdateWrapper<Specifictemplate>updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",template.getId());
            int update = templateDao.update(template, updateWrapper);
            if(update <= 0) throw new MsgException("回填商品数据到规格模板失败");
            return new SysResult(SysResultEnum.SUCCESS.getIndex(),SysResultEnum.SUCCESS.getName(),null);
        }catch (MsgException e){
            LOGGER.info(e.getMessage());
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(),SysResultEnum.SYS_ERROR.getName(),null);
        }
    }
}