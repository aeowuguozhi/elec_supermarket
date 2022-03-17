package com.bnuz.electronic_supermarket.brand.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bnuz.electronic_supermarket.common.javaBean.Brand;

public interface BrandService extends IService<Brand> {

    @Override
    boolean save(Brand entity);

    Page<Brand> query(int currPage,int size,String name);

    String delete(String id);
}
