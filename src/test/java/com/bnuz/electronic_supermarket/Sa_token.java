/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: Sa_token
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/18 13:06
 * Description: sa-token
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import java.util.UUID;

/**
 * 需要启动webMVC场景
 */
@Deprecated
@Slf4j
public class Sa_token {
    @Test
    public static void test(){
        String id = UUID.randomUUID().toString();
        log.info("ID:" + id);
        StpUtil.login(id);
        StpUtil.checkLogin();
        log.info(String.valueOf(StpUtil.isLogin()));
        StpUtil.logout(id);
        StpUtil.checkLogin();
    }

    public static void main(String[] args) throws JsonProcessingException {
        SpringApplication.run(Sa_token.class, args);
        System.out.println("启动成功：Sa-Token配置如下：" + SaManager.getConfig());
        test();
    }
}