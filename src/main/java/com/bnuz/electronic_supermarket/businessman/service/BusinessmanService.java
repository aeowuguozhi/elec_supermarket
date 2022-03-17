package com.bnuz.electronic_supermarket.businessman.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnuz.electronic_supermarket.businessman.dto.BusinessmanDto;
import com.bnuz.electronic_supermarket.common.javaBean.Businessman;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface BusinessmanService extends IService<Businessman> {

    /**
     * 商家注册
     * @param businessmanDto
     * @return
     */
    String register(BusinessmanDto businessmanDto);

    /**
     * 商家登录
     * @param name
     * @param password
     * @return token,businessmanId
     */
    Map<String,Object> login(String name,String password);

    /**
     * 查询商家信息，通过ids
     * @param ids
     * @return
     */
    Map<String,Object> getByIds(List<String> ids, HttpServletRequest request);

}
