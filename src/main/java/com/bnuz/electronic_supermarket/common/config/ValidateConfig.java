/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: ValidateConfig
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/19 21:46
 * Description: 配置HibernateValidator的校验
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.common.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * https://blog.csdn.net/m0_37542889/article/details/88694739
 */
@Configuration
public class ValidateConfig {

    @Bean
    public Validator validator(){
        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
                .configure()
                //开启快速校验--默认校验所有参数，false校验全部
                .addProperty( "hibernate.validator.fail_fast", "true" )
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        return validator;
    }
}
