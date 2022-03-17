/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: SpeTemplateController
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/17 15:33
 * Description: 规格模板控制类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.specification.speciTemplate.controller;

import com.bnuz.electronic_supermarket.common.dto.SysResult;
import com.bnuz.electronic_supermarket.common.enums.SysResultEnum;
import com.bnuz.electronic_supermarket.common.exception.MsgException;
import com.bnuz.electronic_supermarket.common.javaBean.Specifictemplate;
import com.bnuz.electronic_supermarket.common.utils.GsonUtil;
import com.bnuz.electronic_supermarket.specification.speciTemplate.service.SpeTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "规格模板API")
@RestController
@RequestMapping("/template")
public class SpeTemplateController {

    @Autowired
    private SpeTemplateService service;

    @ApiOperation("创建规格模板")
    @ApiImplicitParams({
          @ApiImplicitParam(paramType = "header",name = "token",value = "商家token",required = true),
          @ApiImplicitParam(paramType = "query",name = "map",value = "HashMap<规格,规格值>",required = true)
    })
    @ApiResponse(description = "")
    @PostMapping("/create")
    public SysResult create(@RequestBody Map<String,String> map){
        try{
            String tid = service.create(map);
            Map<String,Object>result = new HashMap<>();
            result.put("templateId",tid);
            return new SysResult(SysResultEnum.Created.getIndex(),SysResultEnum.Created.getName(),result);
        }catch (MsgException e){
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(),e.getMessage(),null);
        }catch (Exception e){
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(),e.getMessage(),null);
        }
    }

    @ApiOperation("删除规格模板")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "token",value = "商家token",required = true),
            @ApiImplicitParam(paramType = "query",name = "templateId",value = "规格模板ID",required = true)
    })
    @ApiResponse(description = "返回规格模板ID")
    @DeleteMapping("/delete")
    public SysResult delete(@RequestParam("templateId") String templateId){
        try{
            String tid = service.delete(templateId);
            Map<String,Object>result = new HashMap<>();
            result.put("templateId",tid);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(),SysResultEnum.SUCCESS.getName(),result);
        }catch (MsgException e){
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(),e.getMessage(),null);
        }catch (Exception e){
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(),e.getMessage(),null);
        }
    }

    @ApiOperation("通过ID查询规格模板")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "token",value = "商家token",required = true),
            @ApiImplicitParam(paramType = "query",name = "templateId",value = "规格模板ID",required = true)
    })
    @ApiResponse(description = "返回规格模板")
    @GetMapping("/queryById")
    public SysResult queryById(@RequestParam("templateId") String templateId){
        try{
            Specifictemplate template = service.queryById(templateId);
            Map<String,Object>result = new HashMap<>();
            HashMap<String,String> originMap = new HashMap<>();
            originMap = GsonUtil.getGson().fromJson(template.getSpecifTemplate(),HashMap.class);
            result.put("originTemplate",originMap);
            result.put("template",template);
            return new SysResult(SysResultEnum.SUCCESS.getIndex(),SysResultEnum.SUCCESS.getName(),result);
        }catch (MsgException e){
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(),e.getMessage(),null);
        }catch (Exception e){
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(),e.getMessage(),null);
        }
    }
}