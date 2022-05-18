package com.RoutineGongJakSo.BE.client.chat.repo;

import com.RoutineGongJakSo.BE.admin.member.MemberRepository;
import com.RoutineGongJakSo.BE.client.chat.dto.ChatRoomDto;
import com.RoutineGongJakSo.BE.client.chat.model.ChatRoom;
import com.RoutineGongJakSo.BE.client.chat.pubsub.RedisSubscriber;
import com.RoutineGongJakSo.BE.admin.member.Member;
import com.RoutineGongJakSo.BE.client.user.User;
import com.RoutineGongJakSo.BE.client.user.UserRepository;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {
    // 채팅방(topic)에 발행되는 메시지를 처리할 Listner
    private final RedisMessageListenerContainer redisMessageListener;
    // 구독 처리 서비스
    private final RedisSubscriber redisSubscriber;
    // Redis
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;
    // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보. 서버별로 채팅방에 매치되는 topic정보를 Map에 넣어 roomId로 찾을수 있도록 한다.
    private Map<String, ChannelTopic> topics;

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    public List<ChatRoomDto> findAllRoom(UserDetailsImpl userDetails) {
        User user = userRepository.findByUserEmail(userDetails.getUserEmail()).orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));
        List<Member> memberList = memberRepository.findAllByUser(user);
        List<ChatRoomDto> chatRoomDtoList = new ArrayList<>();
        for (Member member : memberList) {

            String roomId = member.getTeam().getRoomId();
            Object lastMessage = chatMessageRepository.findLastMessage(roomId);

            ChatRoomDto chatRoomDto = new ChatRoomDto();
            chatRoomDto.setRoomId(roomId);
            chatRoomDto.setName(member.getTeam().getRoomName());
            chatRoomDto.setLastMessage(lastMessage);

            chatRoomDtoList.add(chatRoomDto);
        }

        return chatRoomDtoList;
    }

    public ChatRoom findRoomById(String id) {
        return opsHashChatRoom.get(CHAT_ROOMS, id);
    }

    /**
     * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
     */
    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

    /**
     * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
     */
    public void enterChatRoom(String roomId) {
        ChannelTopic topic = topics.get(roomId);
        if (topic == null)
            topic = new ChannelTopic(roomId);
        redisMessageListener.addMessageListener(redisSubscriber, topic);
        topics.put(roomId, topic);
    }

    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }
}
