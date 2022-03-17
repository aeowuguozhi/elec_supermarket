package com.bnuz.electronic_supermarket.brand.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnuz.electronic_supermarket.common.javaBean.Brand;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BrandMapper extends BaseMapper<Brand> {

    Brand test(String id);
}
