package com.RoutineGongJakSo.BE.chat.repo;

import com.RoutineGongJakSo.BE.chat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ChatMessageRepository {// Redis

    private final RedisTemplate<String, Object> redisTemplate;

    public void save(ChatMessage chatMessage) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));

        String roomId = chatMessage.getRoomId();

        redisTemplate.opsForList().rightPush(roomId, chatMessage);

        Long size = redisTemplate.opsForList().size(roomId);

        //테스트 10개로 진행 완료
        //Todo 추후에 100개로 바꿀예정
        if (size > 10L) {
            redisTemplate.opsForList().leftPop(roomId, size - 10L);
        }
    }

    public List<ChatMessage> findAllMessage(String roomId) {
        List<ChatMessage> chatMessageList = null;
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));
        if (Boolean.TRUE.equals(redisTemplate.hasKey(roomId))) {
            // 저장된 전체 레코드 수
            chatMessageList = (List) redisTemplate.opsForList().range(roomId, 0, -1);

            if (chatMessageList == null) {
                chatMessageList = new LinkedList<>();
            }
        }

        return chatMessageList;
    }
}
