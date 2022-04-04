/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: JDKProxyFactory
 * Author:   Mr.WuGuoZhi
 * Date:     2022/4/4 9:45
 * Description: JDK的Proxy类做动态代理
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.common.Factory;

import com.bnuz.electronic_supermarket.common.javaBean.MilliSecondInvocationHandler;

import java.lang.reflect.Proxy;

public class JDKProxyFactory {
    /**
     * 思考：如果业务要求实现多个InvocationHandler呢？是要getMilliSecondProxy（）？getLoggerProxy（）？下去吗
     * @param target
     * @return
     */
    public static Object getProxy(Object target){
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new MilliSecondInvocationHandler(target));
    }
}