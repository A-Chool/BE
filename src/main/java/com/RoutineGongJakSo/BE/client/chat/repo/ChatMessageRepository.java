package com.RoutineGongJakSo.BE.client.chat.repo;

import com.RoutineGongJakSo.BE.client.chat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
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

    @PostConstruct
    private void init() {
        opsHashChatMessage = redisTemplate.opsForHash();
    }

    public ChatMessage save(ChatMessage chatMessage) {
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(List.class));
        String roomId = chatMessage.getRoomId();
        List<ChatMessage> chatMessageList = opsHashChatMessage.get(CHAT_MESSAGE, roomId);
        if (chatMessageList == null) chatMessageList = new ArrayList<>();
        chatMessageList.add(chatMessage);
        opsHashChatMessage.put(CHAT_MESSAGE, roomId, chatMessageList);

        return chatMessage;
    }

    public List<ChatMessage> findAllMessage(String roomId) {
        return opsHashChatMessage.get(CHAT_MESSAGE, roomId);
    }

    public Object findLastMessage(String roomId){
        if(opsHashChatMessage.get(CHAT_MESSAGE,roomId) == null) return null;
        return opsHashChatMessage.get(CHAT_MESSAGE,roomId).get(opsHashChatMessage.get(CHAT_MESSAGE,roomId).size()-1);
    }
}
