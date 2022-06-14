package com.marketGola;

import com.marketGola.User.config.MyBatisConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(MyBatisConfig.class)
@SpringBootApplication(scanBasePackages = "com.marketGola.User.controller")
public class MarketgolaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MarketgolaApplication.class, args);
    }

}
