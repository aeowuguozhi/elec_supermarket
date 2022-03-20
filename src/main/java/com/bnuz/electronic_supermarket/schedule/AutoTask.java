/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: AutoTask
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/20 15:34
 * Description: 定时任务
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.schedule;


import com.bnuz.electronic_supermarket.common.utils.LocalDateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class AutoTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoTask.class);


    /**
     *  *：表示匹配该域的任意值，假如在Minutes域使用*, 即表示每分钟都会触发事件。
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void scheduleTask(){
        System.out.println();
    }
}