package com.bnuz.electronic_supermarket.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnuz.electronic_supermarket.common.javaBean.Orderr;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface OrderMapper extends BaseMapper<Orderr> {
}
