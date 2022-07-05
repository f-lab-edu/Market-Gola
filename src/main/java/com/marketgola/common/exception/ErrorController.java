package com.marketgola.common.exception;

import com.marketgola.user.exception.BadRequestException;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 아직 Service 로 구현되지 않았지만, 추후 쓰일 에러 테스트 해보기 위해 ErrorController 생성
 */

@RestController
public class ErrorController {

    @GetMapping("/wronginput")
    public String badException() {
        throw new BadRequestException(HttpStatus.BAD_REQUEST, LogLevel.ERROR, "잘못된 입력 값 입니다");
    }

    @GetMapping("/unexpected")
    public String unExpectedException() {
        throw new IllegalCallerException();
    }

}



