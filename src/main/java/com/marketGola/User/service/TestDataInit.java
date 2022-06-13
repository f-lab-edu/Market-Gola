package com.marketGola.User.service;

import com.marketGola.User.domain.User;
import com.marketGola.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@RequiredArgsConstructor
public class TestDataInit {
    private final UserRepository userRepository;
    /**
     * @EventListener: 스프링 컨테이너가 완전히 초기화를 다 끝내고, 실행 준비가 되었을 때 발생하는 이벤트
     * 스프링이 뜰 때 TestDataInt 를 호출. 자동으로 데이터를 넣어줌. 확인용으로 초기 데이터 추가하는 것.
     * 지금은 findALL 을 안하니 없어도 될 것 같다.(리스트를 보여주는 화면도 없다)
     * 이 기능이 없으면, 서버를 실행 할 때마다 데이터를 입력해야 리스트에 나타난다.
     * 현재 실습에서는 메ㅗ모리여서 서버를 내리면 데이터가 제거되기 때문이다.
     * ApplicationReadyEvent: Application 이 준비되었을 때, 나타나는 이벤트.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        log.info("test data init");
        userRepository.save(new User("jieun", "jieun@yesgmail.com" ));
    }
}
