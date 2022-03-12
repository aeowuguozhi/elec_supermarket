/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: jwtTest
 * Author:   Mr.WuGuoZhi
 * Date:     2022/2/23 15:53
 * Description: jwt 测试类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.bnuz.electronic_supermarket.common.utils.JwtUtil;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class JwtTest {

    @Test
    void jwtToken() {
        Map<String,String> map = new HashMap<>();
        map.put("account","aeowuguozhi");
        map.put("type",String.valueOf(1));
        String token = JwtUtil.createJwtToken(map,120);
        System.out.println(token);
        DecodedJWT decodedJWT = JwtUtil.verifyToken(token);
        String account = decodedJWT.getClaim("account").asString();
        Integer type = decodedJWT.getClaim("type").asInt();
        System.out.println();
    }

    @Test
    void decodeToken(){
        String token = "";
        JwtUtil.verifyToken(token);
    }

    @Test
    void validationUtils() {
        DateTime dt = new DateTime();
//        System.currentTimeMillis();
        System.out.println(System.currentTimeMillis());
        System.out.println(dt.toString());
        System.out.println(dt.toDateTime());
//        System.out.println(dt.getYear()+"-");
    }


}