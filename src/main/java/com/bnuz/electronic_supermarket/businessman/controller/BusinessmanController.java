/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: BusinessmanController
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/15 21:51
 * Description: 商人控制类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.businessman.controller;

import com.bnuz.electronic_supermarket.businessman.dto.BusinessmanDto;
import com.bnuz.electronic_supermarket.businessman.service.BusinessmanService;
import com.bnuz.electronic_supermarket.common.dto.SysResult;
import com.bnuz.electronic_supermarket.common.enums.SysResultEnum;
import com.bnuz.electronic_supermarket.common.exception.MsgException;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BusinessmanController {

    private BusinessmanService businessmanService;

    public SysResult register(BusinessmanDto manDto){
        try{
            String bid = this.businessmanService.register(manDto);
            Map<String,Object> result = new HashMap<>();
            result.put("businessmanId",bid);
            return new SysResult(SysResultEnum.Created.getIndex(),"OK",result);
        }catch (MsgException e){
            e.printStackTrace();
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(),e.getMessage(),null);
        }catch (Exception e){
            e.printStackTrace();
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(),e.getMessage(),null);
        }
    }
}