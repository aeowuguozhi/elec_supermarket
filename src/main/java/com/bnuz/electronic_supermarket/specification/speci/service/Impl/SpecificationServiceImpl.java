/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: SpecificationServiceImpl
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/17 12:29
 * Description: 规格服务层实现类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.specification.speci.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnuz.electronic_supermarket.common.javaBean.Specification;
import com.bnuz.electronic_supermarket.specification.speci.dao.SpecificationMapper;
import com.bnuz.electronic_supermarket.specification.speci.service.SpecificationService;
import org.springframework.stereotype.Service;

@Service
public class SpecificationServiceImpl extends ServiceImpl<SpecificationMapper, Specification> implements SpecificationService {
}