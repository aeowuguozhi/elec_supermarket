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


package com.bnuz.electronic_supermarket.Interceptor;

import cn.dev33.satoken.stp.StpUtil;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.bnuz.electronic_supermarket.common.exception.MsgException;
import com.bnuz.electronic_supermarket.common.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.util.validation.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器，用户的request的Header需要设置token字段并且携带token值。如果携带的token信息不正确，那么判定为用户没有登录，需要跳转到登录页面。
 * 登录拦截器本来用JWT验证的，现在改为使用Sa-token进行验证
 */

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private final static Logger LOGGER = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        boolean token = (boolean)request.getAttribute("token");
        try{
            String token = request.getHeader("token");
            if(token == null || token.equals("")){
                throw new MsgException("未携带token");
            }
            //TODO 未实现token超时自动刷新，待验证使用sa-token能否解决
            //验证token正确性和是否过期了。
//            JwtUtil.verifyToken(token);
            if(!StpUtil.getTokenInfo().getTokenValue().equals(token)){
                throw new MsgException("token信息不正确");
            }
            LOGGER.info("客户端:主机名:{},IP:{}  访问  服务器端:IP:{},请求资源名称:{}" ,
                    request.getRemoteHost(), request.getRemoteAddr(),request.getLocalAddr(),request.getRequestURI());
            return true;
        }catch (MsgException e){
            LOGGER.info(e.getMessage());
            throw e;
        }catch (TokenExpiredException e){
            LOGGER.info("token过期");
            throw new MsgException("token过期，请重新登录");
        }catch (Exception e){
            LOGGER.info(e.getMessage());
            throw e;
        }
    }
    //catch (JWTDecodeException e){
    //            throw new MsgException("token过期，请重新登录");
    //        }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}