package com.example.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.server.mapper")
public class RookcodeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RookcodeServerApplication.class, args);
    }

}
