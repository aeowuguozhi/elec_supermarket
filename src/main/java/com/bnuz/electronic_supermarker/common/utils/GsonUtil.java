/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: GsonUtil
 * Author:   Mr.WuGuoZhi
 * Date:     2022/2/23 14:03
 * Description: Gson工具类（单例模式）
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarker.common.utils;

import com.google.gson.Gson;

/**
 *  Gson使用方法：
 *  Object obj = new Object();
 *  //Object转Json字符串
 *  String obstr = new Gson().toJson(object);
 *  //Json字符串转Object
 *  Object object = new Gson().fromJson(obstr);
 */

public class GsonUtil {
    private static Gson gson;

    public static Gson getGson(){
        if (GsonUtil.gson == null){
            GsonUtil.gson = new Gson();
            return gson;
        }
        return gson;
    }
}