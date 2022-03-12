/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: CalendarUtils
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/2 21:30
 * Description: 日历工具类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.common.utils;

import java.util.Calendar;

public class CalendarUtils {

    private static Calendar calendar;

    public static String  getDateTime(){
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        return year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + seconds;
    }
}