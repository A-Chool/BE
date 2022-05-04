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
    private static final String CHAT_MESSAGE = "CHAT_MESSAGE";
    private final RedisTemplate<String, Object> redisTemplate;
//    private HashOperations<String, String, ChatMessage> opsHashChatMessage;
//    // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보. 서버별로 채팅방에 매치되는 topic정보를 Map에 넣어 roomId로 찾을수 있도록 한다.
//    private Map<String, ChannelTopic> topics;
//
//    @PostConstruct
//    private void init() {
//        opsHashChatMessage = redisTemplate.opsForHash();
//        topics = new HashMap<>();
//    }

    public void save(ChatMessage chatMessage) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));
        redisTemplate.opsForList().rightPush(chatMessage.getRoomId(), chatMessage);
    }

    public List<ChatMessage> findAllMessage(String roomId) {
        List<ChatMessage> chatMessageList = null;
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));
        if (Boolean.TRUE.equals(redisTemplate.hasKey(roomId))) {
            // 저장된 전체 레코드 수
            chatMessageList = (List) redisTemplate.opsForList().range(roomId, 0, -1);

            if (chatMessageList == null) {
                chatMessageList = new LinkedList<ChatMessage>();
            }
        }

        return chatMessageList;
    }
}
