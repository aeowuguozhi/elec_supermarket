/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: MsgException
 * Author:   Mr.WuGuoZhi
 * Date:     2022/2/23 21:09
 * Description: 通用异常类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.common.exception;

public class MsgException extends RuntimeException{
    /**
     * 韩餐构造方法
     * @param message 在网页中的提示信息
     */
    public MsgException(String message) {
        super(message);
    }

    /**
     * 获取提示信息
     * @return 在网页中的提示信息
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}