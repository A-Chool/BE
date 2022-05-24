package com.RoutineGongJakSo.BE.sse;

import com.RoutineGongJakSo.BE.client.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void signup(final User user) {
        log.info("checkIn publish");
        // 회원가입 핵심 비즈니스 로직...

        // 부가적 비즈니스 수행 등을 위하여 이벤트 전파
        applicationEventPublisher.publishEvent(new SignupEvent(user, true));
    }
}