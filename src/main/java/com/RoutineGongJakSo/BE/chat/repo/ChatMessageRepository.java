package com.RoutineGongJakSo.BE.chat.repo;

import com.RoutineGongJakSo.BE.chat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@RequiredArgsConstructor
@Repository
public class ChatMessageRepository {// Redis

    // Redis
    private static final String CHAT_MESSAGE = "CHAT_MESSAGE";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, List<ChatMessage>> opsHashChatMessage;
    private Map<String, ChannelTopic> topics;

    @PostConstruct
    private void init() {
        opsHashChatMessage = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    public ChatMessage save(ChatMessage chatMessage) {
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(List.class));
        String roomId = chatMessage.getRoomId();
        List<ChatMessage> chatMessageList = opsHashChatMessage.get(CHAT_MESSAGE, roomId);
        if (chatMessageList == null) chatMessageList = new ArrayList<>();
        chatMessageList.add(chatMessage);
        opsHashChatMessage.put(CHAT_MESSAGE, roomId, chatMessageList);

//        Long size = redisTemplate.opsForList().size(roomId);

        //테스트 10개로 진행 완료
        //Todo 추후에 100개로 바꿀예정
//        if (size > 10L) {
//            redisTemplate.opsForList().leftPop(roomId, size - 10L);
//        }

        return chatMessage;
    }

    public List<ChatMessage> findAllMessage(String roomId) {
        return opsHashChatMessage.get(CHAT_MESSAGE, roomId);
    }

    public List<ChatMessage> findLastMessage(String roomId){
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(List.class));
        return opsHashChatMessage.get(CHAT_MESSAGE,roomId);
    }

    public Map<String, List<ChatMessage>> test(String roomId) {
        return opsHashChatMessage.entries(CHAT_MESSAGE);
    }
}
