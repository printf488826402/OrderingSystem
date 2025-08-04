package com.sky;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
//import io.github.cdimascio.dotenv.spring.boot.EnableDotenv;

@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
@Slf4j
//@EnableDotenv// 让 Spring 自动加载 .env 中的变量
public class SkyApplication {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
                .directory(".") // sky-take-out的根目录
                .filename(".env")//与sky-take-out（父项目）的pom.xml同级
                .load();
        System.setProperty("ALI_ACCESS_KEY_ID", dotenv.get("ALI_ACCESS_KEY_ID"));
        System.setProperty("ALI_ACCESS_KEY_SECRET", dotenv.get("ALI_ACCESS_KEY_SECRET"));

        SpringApplication.run(SkyApplication.class, args);
        log.info("server started");
    }
}
