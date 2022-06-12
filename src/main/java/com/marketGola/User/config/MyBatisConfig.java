package com.marketGola.User.config;

import com.marketGola.User.repository.UserRepository;
import com.marketGola.User.repository.mybatis.MyBatisUserRepository;
import com.marketGola.User.repository.mybatis.UserMapper;
import com.marketGola.User.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

//UserMapper 를 주입받고, 필요한 의존관계 생성.
@Configuration
@RequiredArgsConstructor

public class MyBatisConfig {
    private final UserMapper userMapper;

    @Bean
    public UserService userService() {
        return new UserService(userRepository());

    }

    @Bean
    public UserRepository userRepository() {
        return new MyBatisUserRepository(userMapper);
    }
}

