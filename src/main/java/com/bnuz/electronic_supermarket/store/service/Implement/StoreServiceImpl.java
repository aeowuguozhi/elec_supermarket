/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: StoreServiceImpl
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/16 16:56
 * Description: 商店服务实现类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.store.service.Implement;

import cn.dev33.satoken.stp.StpUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnuz.electronic_supermarket.common.enums.StateEnum;
import com.bnuz.electronic_supermarket.common.exception.MsgException;
import com.bnuz.electronic_supermarket.common.javaBean.Store;
import com.bnuz.electronic_supermarket.common.utils.GsonUtil;
import com.bnuz.electronic_supermarket.common.utils.JudgeUserIdUtil;
import com.bnuz.electronic_supermarket.common.utils.JwtUtil;
import com.bnuz.electronic_supermarket.common.utils.LocalDateTimeUtils;
import com.bnuz.electronic_supermarket.store.dao.StoreDao;
import com.bnuz.electronic_supermarket.store.service.StoreService;
import com.bnuz.electronic_supermarket.user.enums.UserStateEnum;
import com.google.gson.Gson;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class StoreServiceImpl extends ServiceImpl<StoreDao, Store> implements StoreService {

    @Autowired
    private StoreDao storeDao;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private final static Logger LOGGER = LoggerFactory.getLogger(StoreServiceImpl.class);

    /**
     * 创建商店
     * @param entity
     * @param request
     * @return store.id
     */
    @Override
    public String save(Store entity, HttpServletRequest request) {
        try{
            //Sa-token 注解已经检验是否登录，是否有权限，登录拦截器已经验证token
            //验证信息
            String name = entity.getName();
            if(StringUtils.containsWhitespace(name) || !StringUtils.hasText(name)){
                throw new MsgException("店铺名不能含有空格");
            }
            String token = request.getHeader("token");
//            DecodedJWT decodedJWT = JwtUtil.verifyToken(token);
//            String businessmanId = decodedJWT.getClaim("businessmanId").asString();
            String[] businessmanId = StpUtil.getLoginId().toString().split("_");
            entity.setBusinessmanId(businessmanId[1]);
            entity.setState(StateEnum.USING.getIndex());
            entity.setCreateTime(LocalDateTimeUtils.getLocalDateTime());
            entity.setUpdateTime(null);
            entity.setId(UUID.randomUUID().toString());
            storeDao.insert(entity);
            redisTemplate.opsForValue().set(Store.class.getSimpleName()+"_"+entity.getId(), GsonUtil.getGson().toJson(entity));
            return entity.getId();
        }catch (MsgException e){
            LOGGER.info(e.getMessage());
            throw e;
        }catch (Exception e){
            LOGGER.info(e.getMessage());
            throw e;
        }
    }

    /**
     * 注销商店，将商店状态设置为0。UpdateWrapper用Mapper的太麻烦，那就用ServiceImpl的update
     * @param sid
     * @param request
     * @return
     *
     */
    @Override
    public String delete(String sid, HttpServletRequest request) {
        try{
            //从token中取出商家ID
            String token = request.getHeader("token");
            String[] s = StpUtil.getLoginId().toString().split("_");
            String businessmanId = s[1];
//            DecodedJWT decodedJWT = JwtUtil.verifyToken(token);
//            String businessmanId = decodedJWT.getClaim("businessmanId").asString();
            //鉴权，只有自己创建的商店才能delete。 select * from store where `id` = sid AND `businessmanId` = businessmanId
            String key = Store.class.getSimpleName()+"_"+sid;
            Store store = null;
            if(redisTemplate.hasKey(key)){
                //redis存在该商店数据,从redis中取
                String json_data = redisTemplate.opsForValue().get(key);
                store = GsonUtil.getGson().fromJson(json_data, Store.class);
            }else{
                //从数据库中取
                QueryWrapper<Store>queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("id",sid).eq("businessmanId",businessmanId);
                store = storeDao.selectOne(queryWrapper);
            }
            if(store == null){
                throw new MsgException("删除失败,不存在该商店");
            }
            //OK，确定数据库存在这样的商店，然后修改state值为0，update回去。
            store.setState(StateEnum.UNUSED.getIndex());
            UpdateWrapper<Store>updateWrapper = new UpdateWrapper<>();
            Map<String,Object> map = new HashMap<>();
            map.put("businessmanId",businessmanId);
            map.put("id",sid);
            updateWrapper.set("state",StateEnum.UNUSED.getIndex());
            updateWrapper.allEq(map);
            int update = storeDao.update(store, updateWrapper);
//            log.info("执行语句storeDao.update(store, updateWrapper)后，返回值是：{}",update);  更新成功返回1。
            //update之后更新redis
            redisTemplate.opsForValue().set(key, GsonUtil.getGson().toJson(store));
            return store.getId();
        }catch(MsgException e){
            LOGGER.info(e.getMessage());
            throw e;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    /**
     *
     * @param currPage 当前页
     * @param size     一页多少条记录
     * @param ids      查询条件
     * @return
     */
    @Override
    public Page<Store> queryStore(Integer currPage, Integer size, List<String> ids) {
        Page<Store>page = new Page<>(currPage,size);
        Page<Store> storePage = null;
        try{
            if(ids.size() == 0){
                //TODO 用QueryWrapper的话，很难使用redis操作??    查询所有
                storePage = storeDao.selectPage(page, null);
                return storePage;
            }else{
                //根据ids条件进行查询，最新创建的店铺派最前面
                //select * from store where id in ids order by id DESC
                QueryWrapper<Store>queryWrapper = new QueryWrapper<>();
                queryWrapper.in("id",ids).orderByDesc("createTime");
                storePage = storeDao.selectPage(page, queryWrapper);
            }
            if(storePage == null){
                throw new MsgException("查询失败");
            }
            return storePage;
        }catch(MsgException e){
            LOGGER.info(e.getMessage());
            throw e;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 根据商店名查询商店名称，模糊匹配
     * @param storeName
     * @return
     */
    @Override
    public Page<Store> queryStoreByName(Integer currPage,Integer size,String storeName) {
        try{
            //select * from store where `name` like "%storeName%"
            Page<Store>page = new Page<>(currPage,size);
            QueryWrapper<Store> wrapper = new QueryWrapper<>();
            wrapper.like("name",storeName).orderByDesc("createTime");
            Page<Store> storePage = storeDao.selectPage(page, wrapper);
            return storePage;
        }catch (MsgException e){
            LOGGER.info(e.getMessage());
            throw e;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            throw e;
        }
    }
}