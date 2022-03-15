/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: Slf4j
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/14 13:57
 * Description: slf4j+log4j2
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket;

import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4j {

    public static final Logger LOGGER = LoggerFactory.getLogger(Slf4j.class);

    @Test
    public void test() {
        //日志消息输出,级别高到低
        LOGGER.error("error");
        LOGGER.warn("warn");
        LOGGER.info("info");
        LOGGER.debug("debug");
        LOGGER.trace("trace");
    }
}