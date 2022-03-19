/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: ValiUtils
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/19 22:06
 * Description: 参数校验
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.common.utils;

import org.springframework.util.StringUtils;

public class ValiUtils {

    public static boolean hasText(String a){
        if(!StringUtils.hasText(a) || StringUtils.containsWhitespace(a)){
            return false;
        }
        return true;
    }
}