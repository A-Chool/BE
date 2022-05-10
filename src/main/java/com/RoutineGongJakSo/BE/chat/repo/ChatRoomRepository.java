package com.RoutineGongJakSo.BE.chat.repo;

import com.RoutineGongJakSo.BE.admin.repository.MemberRepository;
import com.RoutineGongJakSo.BE.chat.dto.ChatRoomDto;
import com.RoutineGongJakSo.BE.chat.model.ChatMessage;
import com.RoutineGongJakSo.BE.chat.model.ChatRoom;
import com.RoutineGongJakSo.BE.chat.pubsub.RedisSubscriber;
import com.RoutineGongJakSo.BE.model.Member;
import com.RoutineGongJakSo.BE.model.User;
import com.RoutineGongJakSo.BE.repository.UserRepository;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.codehaus.jackson.map.ObjectMapper;
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
        System.out.println("memberList = " + memberList);
        List<ChatRoom> chatRoomList = new ArrayList<>();
        List<ChatRoomDto> chatRoomDtoList = new ArrayList<>();
        for (Member member : memberList) {
            ChatRoomDto chatRoomDto = new ChatRoomDto();
            String roomId = member.getWeekTeam().getRoomId();
            chatRoomDto.setRoomId(roomId);
            chatRoomDto.setName(member.getWeekTeam().getRoomName());
//            ChatMessage lastMessage = chatMessageRepository.findLastMessage(roomId);

            if (chatMessageRepository.findAllMessage(roomId) != null) {
                ObjectMapper mapper = new ObjectMapper();

                Map<String, List<ChatMessage>> z = chatMessageRepository.test(roomId);

                List<ChatMessage> x = chatMessageRepository.findLastMessage(roomId);
                System.out.println("x.getClass() = " + x.getClass());
                ChatMessage lastMessage = x.get(x.size()-1);
                System.out.println("lastMessage.getClass() = " + lastMessage.getClass());
                System.out.println("lastMessage = " + lastMessage);
                chatRoomDto.setLastMessage(lastMessage);
            }

//            List<ChatMessage> chatMessageList = chatMessageRepository.findAllMessage(roomId);
//
//            ChatMessage lastMessage = null;
//            if(chatMessageList != null) {
//                for(ChatMessage chatMessage : chatMessageList){
//                    System.out.println("chatMessage = " + chatMessage);
//                    System.out.println("chatMessage.getCreatedAt() = " + chatMessage.getCreatedAt());
//                }
////                lastMessage = chatMessageList.get(0);
//            }
//            chatRoomDto.setLastMessage(lastMessage);
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
