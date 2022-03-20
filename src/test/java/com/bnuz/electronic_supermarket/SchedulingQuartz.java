/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: SchedulingQuartz
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/20 13:48
 * Description: quartz
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket;

import com.bnuz.electronic_supermarket.common.utils.LocalDateTimeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.annotation.Scheduled;

public class SchedulingQuartz {
    @Scheduled(cron = "0/4 0 0 0 0 ? *")
    @Test
    public void testQuartz(){
        String localDateTime = LocalDateTimeUtils.getLocalDateTime();
        System.out.println(localDateTime);
    }
}