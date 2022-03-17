package com.bnuz.electronic_supermarket.specification.speciTemplate.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnuz.electronic_supermarket.common.javaBean.Specifictemplate;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SpeTemplateMapper extends BaseMapper<Specifictemplate> {
}
