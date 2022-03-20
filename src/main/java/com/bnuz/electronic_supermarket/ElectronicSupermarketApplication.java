package com.bnuz.electronic_supermarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableScheduling          //开启定时任务
@EnableOpenApi
@EnableSwagger2
@SpringBootApplication
public class ElectronicSupermarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElectronicSupermarketApplication.class, args);
    }

}
