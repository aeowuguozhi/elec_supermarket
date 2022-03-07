/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: MsgException
 * Author:   Mr.WuGuoZhi
 * Date:     2022/2/23 18:49
 * Description: 自定义消息异常类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarker.user.exception;

public class PasswordErrorException extends RuntimeException{

    public PasswordErrorException(String msg){
        super(msg);
    }
}