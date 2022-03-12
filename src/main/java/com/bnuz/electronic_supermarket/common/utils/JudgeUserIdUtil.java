/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: UserIdFromToken
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/9 16:56
 * Description: 从token获取用户ID
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.common.utils;

import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.http.HttpServletRequest;

public class JudgeUserIdUtil {

    public static Boolean Judge(HttpServletRequest request,String userId){
        String token = request.getHeader("token");
        DecodedJWT decodedJWT = JwtUtil.verifyToken(token);
        String userIdInToken = decodedJWT.getClaim("id").asString();
        if(userId.equals(userIdInToken)){
            return true;
        }
        return false;
    }
}