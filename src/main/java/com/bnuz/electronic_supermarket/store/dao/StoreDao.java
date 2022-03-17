package com.bnuz.electronic_supermarket.store.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnuz.electronic_supermarket.common.javaBean.Store;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface StoreDao extends BaseMapper<Store> {

}
