/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: ScheduleConfig
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/20 14:37
 * Description: 定时任务配置类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//所有的定时任务都放在一个线程池中，定时任务启动是使用不同的线程。
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

    //重写方法，线程池有10个线程供定时任务使用
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(10));
    }
}