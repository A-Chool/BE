package com.RoutineGongJakSo.BE.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@RestController
public class NotificationController {
    private static final long SSE_SESSION_TIMEOUT = 30 * 60 * 1000L;

    private Set<SseEmitter> emitterSet = new CopyOnWriteArraySet<>();

    @GetMapping(value = "/notice", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter signup(HttpServletRequest request) {
        log.info("SSE stream 접근 : {}", request.getRemoteAddr());

        SseEmitter emitter = new SseEmitter(SSE_SESSION_TIMEOUT);
        emitterSet.add(emitter);

//        emitter.onTimeout(() -> emitterSet.remove(emitter));
//        emitter.onCompletion(() -> emitterSet.remove(emitter));

        return emitter;
    }

    @Async
    @EventListener
    public void onSignupEvent(final SignupEvent signupEvent) {
        log.info("신규 회원 = {}, 이벤트 구독자 수 = {}", signupEvent.getUser(), emitterSet.size());

        List<SseEmitter> deadEmitters = new ArrayList<>();
        emitterSet.forEach(emitter -> {
            try {
                emitter.send(signupEvent, MediaType.APPLICATION_JSON);
            } catch (Exception ignore) {
                deadEmitters.add(emitter);
            }
        });
        emitterSet.removeAll(deadEmitters);
    }
}