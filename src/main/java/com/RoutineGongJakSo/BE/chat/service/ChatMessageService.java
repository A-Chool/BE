package com.RoutineGongJakSo.BE.chat.service;

import com.RoutineGongJakSo.BE.chat.model.ChatMessage;
import com.RoutineGongJakSo.BE.chat.pubsub.RedisPublisher;
import com.RoutineGongJakSo.BE.chat.repo.ChatMessageRepository;
import com.RoutineGongJakSo.BE.chat.repo.ChatRoomRepository;
import com.RoutineGongJakSo.BE.security.jwt.JwtDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final JwtDecoder jwtDecoder;

    public void save(ChatMessage message, String token) {
        // username 세팅
        String username = "";
        String sender = "";
        if (!(String.valueOf(token).equals("Authorization") || String.valueOf(token).equals("null"))) {
            String tokenInfo = token.substring(7); // Bearer빼고
            username = jwtDecoder.decodeNickName(tokenInfo);
            sender = jwtDecoder.decodeUsername(tokenInfo);
        }
        message.setSender(sender);
        message.setNickname(username);

        // 시간 세팅
        Date date = new Date();
        message.setCreatedAt(date.toString().substring(11, 19));
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            chatRoomRepository.enterChatRoom(message.getRoomId());
//            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        } else {
            chatMessageRepository.save(message);
        }
        // Websocket에 발행된 메시지를 redis로 발행한다(publish)
        redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
    }

    public List<ChatMessage> getMessages(String roomId) {
        List<ChatMessage> chatMessageList = chatMessageRepository.findAllMessage(roomId);

        return chatMessageList;
    }
}
