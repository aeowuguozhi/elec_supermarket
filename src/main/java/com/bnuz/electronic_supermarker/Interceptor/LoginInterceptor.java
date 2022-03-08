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

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bnuz.electronic_supermarker.common.dto.SysResult;
import com.bnuz.electronic_supermarker.common.exception.MsgException;
import com.bnuz.electronic_supermarker.common.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器，用户的request的Header需要设置token字段并且携带token值。如果携带的token信息不正确，那么判定为用户没有登录，需要跳转到登录页面。
 */


@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        boolean token = (boolean)request.getAttribute("token");
        try{
            String token = request.getHeader("token");
//            Cookie[] cookies = request.getCookies();
//            for (Cookie c:cookies
//            ) {
//                log.info(c.getName() + ":" + c.getValue());
//                if(c.getName().equals("token")){
//                    token = c.getValue();
//                    break;
//                }
//            }
            if(token == null || token.equals("")){
                throw new MsgException("未携带token");
            }
            //验证token正确性和是否过期了。
            JwtUtil.verifyToken(token);
            return true;
        }catch (MsgException e){
            throw e;
        }catch (JWTDecodeException e){
            throw new MsgException("token过期，请重新登录");
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}