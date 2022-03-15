package com.bnuz.electronic_supermarket.businessman.service;

import com.bnuz.electronic_supermarket.businessman.dto.BusinessmanDto;

public interface BusinessmanService {

    /**
     * 商家注册
     * @param businessmanDto
     * @return
     */
    String register(BusinessmanDto businessmanDto);
}
