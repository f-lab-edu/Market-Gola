package com.marketGola;

import com.marketGola.User.config.MyBatisConfig;
import com.marketGola.User.repository.UserRepository;
import com.marketGola.User.service.TestDataInit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Import(MyBatisConfig.class)
@SpringBootApplication(scanBasePackages = "com.marketGola.User.controller")
public class MarketgolaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MarketgolaApplication.class, args);
    }

    @Bean
    @Profile("local")
    public TestDataInit testDataInit(UserRepository userRepository) {
        return new TestDataInit(userRepository);
    }
}
