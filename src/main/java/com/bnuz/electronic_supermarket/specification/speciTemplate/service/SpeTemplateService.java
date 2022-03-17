package com.bnuz.electronic_supermarket.specification.speciTemplate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnuz.electronic_supermarket.common.javaBean.Specifictemplate;

import java.util.Map;

public interface SpeTemplateService extends IService<Specifictemplate> {
    /**
     * 创建规格模板，需要商家的token
     * @param map
     * @return
     */
    String create(Map<String,String> map);

    /**
     * 删除规格模板
     * @param templateId
     * @return
     */
    String delete(String templateId);

    /**
     * 根据ID查询规格模板
     */
    Specifictemplate queryById(String tid);
}
