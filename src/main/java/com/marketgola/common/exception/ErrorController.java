package com.marketgola.common.exception;

import com.marketgola.user.exception.UserBadRequestException;
import org.springframework.boot.logging.LogLevel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 아직 Service 로 구현되지 않았지만, 추후 쓰일 에러 테스트 해보기 위해 ErrorController 생성
 */

@RestController
public class ErrorController {

    @GetMapping("/wronginput")
    public String badException() {
        throw new UserBadRequestException(LogLevel.ERROR, "잘못된 입력 값 입니다");
    }

    /**
     * 예상치 못한 Exception 을 가정하여, IllegalCallerException() 호출
     */

    @GetMapping("/unexpected")
    public String unExpectedException() {
        throw new IllegalCallerException();
    }

}



