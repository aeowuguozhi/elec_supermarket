package com.bnuz.electronic_supermarket.common.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface UploadService {

    /**
     * 文件上传
     * @param is   文件流
     * @param fileName  文件名称
     */
    public String uploadFile(InputStream is,String fileName);
}
