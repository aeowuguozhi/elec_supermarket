/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: Gosn
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/17 15:45
 * Description: 测试Gson
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket;

import com.bnuz.electronic_supermarket.common.utils.GsonUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class Gosn {
    @Test
    public void testGson(){
        HashMap<String,String>map = new HashMap<>();
        map.put("hello","hello1");
        map.put("hi","hi1");
        String json = GsonUtil.getGson().toJson(map);
        System.out.println(json);
        HashMap hashMap = GsonUtil.getGson().fromJson(json, HashMap.class);
        hashMap.forEach((k,v)->{
            System.out.println(k + ":" + v);
        });
        String jsondata = "{\"additionalProp1\":\"string\",\"additionalProp2\":\"string\",\"additionalProp3\":\"string\"}";
        HashMap hashMap1 = GsonUtil.getGson().fromJson(jsondata, HashMap.class);
        hashMap1.forEach((k,v)->{
            System.out.println(k + ":" + v);
        });
    }
}