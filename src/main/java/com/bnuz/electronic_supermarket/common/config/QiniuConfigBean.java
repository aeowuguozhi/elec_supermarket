/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: QiniuConfig
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/24 15:41
 * Description: 七牛云对象存储配置
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "qiniu")
public class QiniuConfigBean {
    private static String accessKey;
    private static String secretKey;
    private static String bucket;
    private static String cdnProfile;
    private static String protocol;

    public static String getAccessKey() {
        return accessKey;
    }

    public static void setAccessKey(String accessKey) {
        QiniuConfigBean.accessKey = accessKey;
    }

    public static String getSecretKey() {
        return secretKey;
    }

    public static void setSecretKey(String secretKey) {
        QiniuConfigBean.secretKey = secretKey;
    }

    public static String getBucket() {
        return bucket;
    }

    public static void setBucket(String bucket) {
        QiniuConfigBean.bucket = bucket;
    }

    public static String getCdnProfile() {
        return cdnProfile;
    }

    public static void setCdnProfile(String cdnProfile) {
        QiniuConfigBean.cdnProfile = cdnProfile;
    }

    public static String getProtocol() {
        return protocol;
    }

    public static void setProtocol(String protocol) {
        QiniuConfigBean.protocol = protocol;
    }
}