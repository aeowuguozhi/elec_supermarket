package com.bnuz.electronic_supermarker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ElectronicSupermarkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElectronicSupermarkerApplication.class, args);
    }

}
