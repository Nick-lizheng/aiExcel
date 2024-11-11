package com.hkct.aiexcel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hkct.aiexcel.mapper")
public class AiExcelApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiExcelApplication.class, args);
    }

}
