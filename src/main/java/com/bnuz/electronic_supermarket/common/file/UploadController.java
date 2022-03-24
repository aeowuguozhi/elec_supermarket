/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: UploadController
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/24 12:30
 * Description: 文件上传Controller
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.common.file;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.bnuz.electronic_supermarket.common.dto.SysResult;
import com.bnuz.electronic_supermarket.common.enums.SysResultEnum;
import com.bnuz.electronic_supermarket.common.exception.MsgException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(tags = "文件API")
@RestController
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @ApiOperation("上传文件")
    @SaCheckLogin
    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)         //My   file.getInputStream(), file.getOriginalFilename()
    public SysResult upload(@RequestPart("file") MultipartFile file){
        try {
            if(file.isEmpty()){
                throw new MsgException("空文件");
            }
            String uploadFile = uploadService.uploadFile(file.getInputStream(),file.getOriginalFilename());
            Map<String,Object> map = new HashMap<>();
            map.put("url",uploadFile);
            if(uploadFile == null) throw new MsgException("上传失败");
            return new SysResult(SysResultEnum.SUCCESS.getIndex(),SysResultEnum.SUCCESS.getName(),map);
        } catch (MsgException e) {
            return new SysResult(SysResultEnum.Client_ERROR.getIndex(),e.getMessage(),null);
        }catch (Exception e){
            return new SysResult(SysResultEnum.SYS_ERROR.getIndex(),e.getMessage(),null);
        }
    }

}