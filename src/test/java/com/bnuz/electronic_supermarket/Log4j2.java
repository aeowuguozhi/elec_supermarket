/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: Log4j2
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/12 21:03
 * Description: log4j2测试日志
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

public class Log4j2 {

    public static final Logger LOGGER = LogManager.getLogger(Log4j2.class);

    @Test
    public void test(){
        //日志消息输出
        for (int i = 0; i < 10000; i++) {
            LOGGER.fatal("fatal");
            LOGGER.error("error");
            LOGGER.warn("warn");
            LOGGER.info("info");
            LOGGER.debug("debug");
            LOGGER.trace("trace");
        }
    }
}