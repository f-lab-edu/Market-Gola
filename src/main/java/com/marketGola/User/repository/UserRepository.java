package com.marketGola.User.repository;

import com.marketGola.User.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

}
