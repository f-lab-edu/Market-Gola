package com.flab.marketgola.user.repository;

import com.flab.marketgola.user.domain.User;
import com.flab.marketgola.user.dto.request.GetUserRequestDto;
import java.util.Optional;

public interface UserRepositoryCustom {

    Optional<User> findByCondition(GetUserRequestDto searchDto);

}
