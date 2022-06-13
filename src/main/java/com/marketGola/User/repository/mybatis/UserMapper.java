package com.marketGola.User.repository.mybatis;

import com.marketGola.User.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {
    void save(User user);
    Optional<User> findById(Long id);
}
