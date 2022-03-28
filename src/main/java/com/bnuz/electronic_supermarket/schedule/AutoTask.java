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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bnuz.electronic_supermarket.common.javaBean.Orderr;
import com.bnuz.electronic_supermarket.common.utils.LocalDateTimeUtils;
import com.bnuz.electronic_supermarket.order.dao.OrderMapper;
import com.bnuz.electronic_supermarket.order.service.OrderrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 开启自动任务SpringBoot自带的Schedule：1，在启动类添加注释@EnableScheduling
 * 2，在自动任务类添加@Component加入到Spring容器中。
 * 3,在方法上添加注解@Scheduled
 */
@Component
public class AutoTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoTask.class);

    @Autowired
    private OrderMapper orderMapper;
    /**
     *  *：表示匹配该域的任意值，假如在Minutes域使用*, 即表示每分钟都会触发事件。
     *  业务：查询数据库中的Order表，找到未完成支付的订单，如果提交订单的时间和CurrentTime相差超过24h，系统将取消这个订单
     *     update orderr
     *     set state = 0
     *     where currentTime - orderTime > 24h
     *
     *     select * from orderr where state = 2；
     *
     */
    @Scheduled(cron = "* 0/1 * * * ?")
    public void scheduleTask(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<Orderr>queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state",2);         //确认订单但未支付订单
        List<Orderr> orderrs = orderMapper.selectList(queryWrapper);
        orderrs.forEach(order->{
            LocalDateTime past = LocalDateTime.parse(order.getOrderTime(),dateTimeFormatter);
            //订单距今超过24h未支付
            boolean b = past.isBefore(LocalDateTime.now().minusHours(24));
            if(b){
                //更改订单状态
                UpdateWrapper<Orderr>updateWrapper = new UpdateWrapper<>();
                updateWrapper.set("state",0).eq("id",order.getId());   //取消订单
                orderMapper.update(null,updateWrapper);
                LOGGER.info("{} 的订单{} 超时未支付,已取消",order.getAccount(),order.getId());
            }
        });
    }
}