package com.flab.marketgola;

import com.p6spy.engine.spy.P6SpyOptions;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

/**
 * P6spy를 사용해서 쿼리 파라미터를 찍을 때 예쁘게 찍힐 수 있게 도와주는 포맷터를 등록하는 클래스
 */
@Configuration
public class P6spyConfig {

    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance()
                .setLogMessageFormat(P6spyPrettySqlFormatter.class.getName());
    }
}
