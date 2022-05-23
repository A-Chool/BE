package com.RoutineGongJakSo.BE.client.chat.pubsub;

import com.RoutineGongJakSo.BE.client.chat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, ChatMessage message) {
        log.info("publish");
        log.info("topic : {}", topic.getTopic());
        log.info("roomId : {}", message.getRoomId());
        log.info("message : {}", message.getMessage());
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
