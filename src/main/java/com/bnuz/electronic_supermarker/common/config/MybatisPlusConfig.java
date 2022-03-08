/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: MybatisPlusConfig
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/6 22:23
 * Description: mybatis-plus配置类，主要配置分页功能
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarker.common.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 参考官网的插件主体 https://baomidou.com/pages/2976a3/#innerinterceptor
 */

@Configuration
//@MapperScan("com.atguigu.boot.demo.mapper")           //scan your mapper package
public class MybatisPlusConfig {
    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setOverflow(true); //设置请求的页面大于最大页后操作，true跳回首页，false继续请求，默认false
        paginationInnerInterceptor.setMaxLimit(500L); //设置最大单页限制数量，这里默认了500条，-1是不受限制。

        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);
//        mybatisPlusInterceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return mybatisPlusInterceptor;
    }

//    @Bean
//    public ConfigurationCustomizer configurationCustomizer() {
//        return configuration -> configuration.setUseDeprecatedExecutor(false);
//    }
}