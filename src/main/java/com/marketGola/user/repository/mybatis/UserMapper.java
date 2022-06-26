package com.marketgola.user.repository.mybatis;

import com.marketgola.user.domain.User;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void save(User user);

    Boolean findByName(String name);

    Optional<User> findById(Long id);
}
