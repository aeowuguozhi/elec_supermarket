/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: LocalDateTimeUtils
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/6 17:29
 * Description: LocalDateTime工具类‘
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarker.common.utils;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LocalDateTimeUtils {

    private static LocalDateTime localDateTime;

    public static String getLocalDateTime(){
        localDateTime = LocalDateTime.now();
        String s = localDateTime.toString();
        s = s.replace("T"," ");
        String[] split = s.split("\\.");          //转义字符.
        System.out.println(split[0]);
        return split[0];
    }
}