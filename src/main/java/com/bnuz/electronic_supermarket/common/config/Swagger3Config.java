/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: SwaggerConfig
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/14 20:12
 * Description: swagger接口文档
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class Swagger3Config {

    /**
     * 开发组一
     */
    Contact contact = new Contact("小志", "https://blog.csdn.net/aeowuguozhi?type=blog", "1404734418@qq.com");

    //配置了Swagger的Docket的Bean实例
    //
    @Bean
    public Docket CreateRestApi() {
        return new Docket(DocumentationType.OAS_30)    //指定swagger3.0版本
                .groupName("开发组001")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.atguigu.boot.demo.controller.Hello"))//指定扫描的包 常用方式
                .build()
                .apiInfo(apiInfo())
                .enable(true);                        //   关闭/开启Api
    }

    //配置Swagger信息  apiInfo
    private ApiInfo apiInfo() {
        return new ApiInfo("小志11的SwaggerAPI文档",
                "电子商城项目",
                "1.0",
                "https://blog.csdn.net/aeowuguozhi?type=blog",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }

    /**
     * 开发组二
     */

    Contact contact1 = new Contact("小志志", "https://blog.csdn.net/aeowuguozhi?type=blog", "1404734418@qq.com");

    //配置了Swagger的Docket的Bean实例
    //
    @Bean
    public Docket CreateRestApi1() {
        return new Docket(DocumentationType.OAS_30)    //指定swagger3.0版本
                .groupName("开发组002")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.bnuz.electronic_supermarket.user"))//指定扫描的包 常用方式
                .build()
                .apiInfo(apiInfo1())
                .enable(true);                        //   关闭/开启Api
    }

    //配置Swagger信息  apiInfo
    private ApiInfo apiInfo1() {
        return new ApiInfo("小志22的SwaggerAPI文档",
                "电子商城项目",
                "1.0",
                "https://blog.csdn.net/aeowuguozhi?type=blog",
                contact1,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }
}