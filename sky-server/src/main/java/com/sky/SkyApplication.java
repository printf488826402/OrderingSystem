package com.sky;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
@Slf4j
public class SkyApplication {
    public static void main(String[] args) {
        SpringApplication.run(SkyApplication.class, args);
        log.info("server started");
        Dotenv dotenv = Dotenv.configure()
                .directory(".env")  // 放 `.env` 的目录
                .load();

        System.setProperty("ALI_ACCESS_KEY_ID", dotenv.get("ALI_ACCESS_KEY_ID"));
        System.setProperty("ALI_ACCESS_KEY_SECRET", dotenv.get("ALI_ACCESS_KEY_SECRET"));

    }

}
