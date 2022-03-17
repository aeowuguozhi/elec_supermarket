package com.bnuz.electronic_supermarket.store.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bnuz.electronic_supermarket.common.javaBean.Store;
import io.swagger.models.auth.In;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 直接使用mybatis-plus中IService<>接口定义的方法
 */

public interface StoreService extends IService<Store> {

    String save(Store entity, HttpServletRequest request);

    String delete(String sid,HttpServletRequest request);

    /**
     * 查询商店，无论是用户还是商家都可以查看到商店的所有信息看，所以用Page分页查询。请求参数有storeIds
     *
     */
    Page<Store> queryStore(Integer currPage, Integer size, List<String>ids);

    Page<Store> queryStoreByName(Integer currPage,Integer size,String storeName);

}
