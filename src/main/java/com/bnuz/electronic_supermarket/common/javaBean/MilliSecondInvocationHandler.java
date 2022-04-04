/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: LoggerInvocationHandler
 * Author:   Mr.WuGuoZhi
 * Date:     2022/4/4 9:47
 * Description: 实现InvocationHandler接口的实现类，主要做方法的执行时间。
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.common.javaBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MilliSecondInvocationHandler implements InvocationHandler {
    /**
     * 动态代理的代理对象
     */
    private final Object target;
    private static Logger LOGGER = LoggerFactory.getLogger(MilliSecondInvocationHandler.class);

    public MilliSecondInvocationHandler(Object target){
        this.target = target;
    }

    //方法的执行时间
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        LOGGER.info("method " + method.getName() + " start to execute.");
        System.out.println("method " + method.getName() + " start to execute.");
        long l = System.currentTimeMillis();
        Object result =  method.invoke(target,args);
        long e = System.currentTimeMillis();
        System.out.println("method " + method.getName() + " is already done using " + (e-l) + "Milliseconds");
        LOGGER.info("method " + method.getName() + " is already done using " + (e-l) + "Milliseconds");
        return result;
    }
}