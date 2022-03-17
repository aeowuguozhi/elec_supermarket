package com.bnuz.electronic_supermarket.specification.speci.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnuz.electronic_supermarket.common.javaBean.Specification;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
@Mapper
public interface SpecificationMapper extends BaseMapper<Specification> {
}
