/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: TokenUtil
 * Author:   Mr.WuGuoZhi
 * Date:     2022/2/23 14:43
 * Description: JWT技术，用于服务器签发token
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarker.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bnuz.electronic_supermarker.common.enums.UserTypeEnum;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 学习JWT  https://www.bilibili.com/video/BV1i54y1m7cP?p=3&spm_id_from=pageDriver
 * token = header.payload.signature
 * header默认
 * {
 * ​   "alg" : "HS256",
 * ​	  "typ" : "JWT"
 * }
 */
public class JwtUtil {
    /**
     * 服务器签名密钥
     */
    public static final String SECRET = "admin@wuguozhi";

    /**
     * 服务器签发token
     */
    public static String createJwtToken(Map<String,String> map,int minute){
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE,minute);         //token 过期时间：120 minutes

        //创建jwt builder
        JWTCreator.Builder builder = JWT.create();
        //header使用默认

        //payload 不可以放敏感信息
        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });
        String token = builder.withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(SECRET));                      //signature
        return token;
    }

    /**
     * 验证token合法性，合法就返回DecodedJWT，从中获取用户信息。
     * @param token
     * @return DecodedJWT
     */
    public static DecodedJWT verifyToken(String token) {
        //相同的服务器密钥，相同的加密算法
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
//        String account = decodedJWT.getClaim("account").asString();
//        int type = decodedJWT.getClaim("type").asInt();
//        String typeName = UserTypeEnum.getName(type);
        return decodedJWT;
    }

}