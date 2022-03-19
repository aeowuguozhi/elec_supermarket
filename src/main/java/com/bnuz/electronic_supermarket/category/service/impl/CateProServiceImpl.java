/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: CateProServiceImpl
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/19 17:54
 * Description: 分类-商品表实现类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.category.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnuz.electronic_supermarket.category.dao.Category_productMapper;
import com.bnuz.electronic_supermarket.category.service.CategoryProductService;
import com.bnuz.electronic_supermarket.common.javaBean.Category_product;
import org.springframework.stereotype.Service;

@Service
public class CateProServiceImpl extends ServiceImpl<Category_productMapper, Category_product> implements CategoryProductService {
}