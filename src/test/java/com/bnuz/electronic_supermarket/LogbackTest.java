/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: LogbackTest
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/11 15:30
 * Description: logback
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(LogbackTest.class);

    @Test
    public void  test01() throws Exception{
        for(int i = 0;i < 10000;i++){
            //日志输出 级别由高到低
            LOGGER.error("error");
            LOGGER.warn("warning");
            LOGGER.info("info");
            LOGGER.debug("debug");
            LOGGER.trace("trace");       //栈追踪信息
        }
    }
}