package com.flab.marketgola.user.repository;

import static com.flab.marketgola.user.domain.QUser.user;

import com.flab.marketgola.user.domain.User;
import com.flab.marketgola.user.dto.request.GetUserRequestDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.util.StringUtils;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    EntityManager em;

    @Override
    public Optional<User> findByCondition(GetUserRequestDto searchDto) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        User findUser = queryFactory
                .selectFrom(user)
                .where(loginIdEq(searchDto.getLoginId()),
                        emailEq(searchDto.getEmail()),
                        phoneNumberEq(searchDto.getPhoneNumber()))
                .fetchOne();

        return Optional.ofNullable(findUser);
    }

    private BooleanExpression loginIdEq(String loginId) {
        return StringUtils.hasText(loginId) ? user.loginId.eq(loginId) : null;
    }

    private BooleanExpression emailEq(String email) {
        return StringUtils.hasText(email) ? user.email.eq(email) : null;
    }

    private BooleanExpression phoneNumberEq(String phoneNumber) {
        return StringUtils.hasText(phoneNumber) ? user.phoneNumber.eq(phoneNumber) : null;
    }
}
