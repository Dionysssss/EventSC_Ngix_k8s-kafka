package com.steve;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.steve.mapper")
public class AirStarterApp {
    public static void main(String[] args) {
        SpringApplication.run(AirStarterApp.class, args);
    }

}
