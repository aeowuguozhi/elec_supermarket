package com.bnuz.electronic_supermarket.businessman.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnuz.electronic_supermarket.common.javaBean.Businessman;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface BusinessmanDao extends BaseMapper<Businessman> {
}
