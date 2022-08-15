package com.flab.marketgola.user;

import com.flab.marketgola.user.domain.Gender;
import java.time.LocalDate;

/**
 * 유효한 입력값을 가진 테스트용 유저 데이터
 */
public class ValidUser {

    public static final String LOGIN_ID = "abc123";
    public static final String EMAIL = "abc123@naver.com";
    public static final String NAME = "홍길동";
    public static final String PASSWORD = "helloworld123!";
    public static final String PHONE_NUMBER = "01011111111";
    public static final String ADDRESS = "서울 성북구 길음동 530-17 길음래미안 아파트 111동 111호";
    public static final Gender GENDER = Gender.MALE;
    public static final LocalDate BIRTH = LocalDate.of(1994, 1, 11);
}
