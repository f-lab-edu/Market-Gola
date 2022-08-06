package com.flab.marketgola.user.mapper;

import com.flab.marketgola.user.domain.User;
import com.flab.marketgola.user.dto.UserUpdateDto;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    void create(User user);

    Optional<User> findByLoginId(String loginId);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    void update(@Param("id") Long id, @Param("updateParam") UserUpdateDto updateParam);

    void delete(Long id);
}
