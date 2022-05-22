package com.RoutineGongJakSo.BE.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(final String message) {
        CustomEvent customEvent = new CustomEvent(this, message);
        applicationEventPublisher.publishEvent(customEvent);
    }
}
