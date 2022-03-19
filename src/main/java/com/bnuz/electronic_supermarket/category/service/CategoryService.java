package com.bnuz.electronic_supermarket.category.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnuz.electronic_supermarket.common.dto.SysResult;
import com.bnuz.electronic_supermarket.common.javaBean.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService extends IService<Category> {

    public SysResult saveCatePro(List<String>categorys,String productId);
}
