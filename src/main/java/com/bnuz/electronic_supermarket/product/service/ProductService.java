package com.bnuz.electronic_supermarket.product.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bnuz.electronic_supermarket.common.javaBean.Product;
import com.bnuz.electronic_supermarket.product.dto.input.ProductDto;
import com.bnuz.electronic_supermarket.product.dto.input.UpdateProductDto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface ProductService extends IService<Product> {

    boolean save(Product entity,List<String> categorys);

    boolean update(UpdateProductDto productDto, String productId);

    Map<String,Object> page(int currPage, int size, String productName);
}
