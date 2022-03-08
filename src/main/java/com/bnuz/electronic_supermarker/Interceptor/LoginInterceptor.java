/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: LoginInterceptor
 * Author:   Mr.WuGuoZhi
 * Date:     2022/2/24 20:39
 * Description: 拦截器
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarker.Interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器，当用户的request没有携带token，而且携带的token信息不正确，那么判定为用户没有登录，需要跳转到登录页面。
 */


@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        boolean token = (boolean)request.getAttribute("token");
        Cookie[] cookies = request.getCookies();
        for (Cookie c:cookies
             ) {
            System.out.println(c.getName() + ":" + c.getValue());
        }
        String token = "s";
        if(token == "s"){
            Object id = request.getAttribute("id");
            log.info("用户id:{}",id);
            return true;
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}