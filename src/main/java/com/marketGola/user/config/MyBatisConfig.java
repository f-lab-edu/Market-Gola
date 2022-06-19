package com.marketGola.user.config;

import com.marketGola.user.repository.mybatis.MyBatisUserRepository;
import com.marketGola.user.repository.mybatis.UserMapper;
import com.marketGola.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//UserMapper 를 주입받고, 필요한 의존관계 생성.
@Configuration
@RequiredArgsConstructor
public class MyBatisConfig {
    private final UserMapper userMapper;

//    @Bean
//    public UserService userService() {
//        return new UserService(MyBatisUserRepository());
//    }
//
//    @Bean
//    public MyBatisUserRepository MyBatisUserRepository() {
//        return new MyBatisUserRepository(userMapper);
//    }
}

