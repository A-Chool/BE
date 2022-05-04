package com.RoutineGongJakSo.BE.chat.repo;

import com.RoutineGongJakSo.BE.chat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class ChatMessageRepository {// Redis
    private static final String CHAT_MESSAGE = "CHAT_MESSAGE";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatMessage> opsHashChatMessage;
    // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보. 서버별로 채팅방에 매치되는 topic정보를 Map에 넣어 roomId로 찾을수 있도록 한다.
    private Map<String, ChannelTopic> topics;

    @PostConstruct
    private void init() {
        opsHashChatMessage = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    public void save(ChatMessage chatMessage){

        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));
        redisTemplate.opsForList().rightPush(chatMessage.getRoomId(), chatMessage);

        System.out.println("chatMessage 저장완료" );
    }

    public List<ChatMessage> findAllMessage() {
        Object x = opsHashChatMessage.values(CHAT_MESSAGE);
        System.out.println("x = " + x);
        return opsHashChatMessage.values(CHAT_MESSAGE);
    }

    public ChatMessage findRoomById(String id) {
        return opsHashChatMessage.get(CHAT_MESSAGE, id);
    }

    /**
     * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
     */
//    public ChatRoom createChatRoom(String name) {
//        ChatRoom chatRoom = ChatRoom.create(name);
//        opsHashChatMessage.put(CHAT_MESSAGE, chatRoom.getRoomId(), chatRoom);
//        return chatRoom;
//    }

    /**
     * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
     */
//    public void enterChatRoom(String roomId) {
//        ChannelTopic topic = topics.get(roomId);
//        if (topic == null)
//            topic = new ChannelTopic(roomId);
//        redisMessageListener.addMessageListener(redisSubscriber, topic);
//        topics.put(roomId, topic);
//    }

    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }
}
