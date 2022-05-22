package com.RoutineGongJakSo.BE.client.chat.service;

import com.RoutineGongJakSo.BE.client.chat.dto.ChatMessageDto;
import com.RoutineGongJakSo.BE.client.chat.model.ChatMessage;
import com.RoutineGongJakSo.BE.client.chat.pubsub.RedisPublisher;
import com.RoutineGongJakSo.BE.client.chat.repo.ChatMessageRepository;
import com.RoutineGongJakSo.BE.client.chat.repo.ChatRoomRepository;
import com.RoutineGongJakSo.BE.exception.CustomException;
import com.RoutineGongJakSo.BE.exception.ErrorCode;
import com.RoutineGongJakSo.BE.security.jwt.JwtDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.RoutineGongJakSo.BE.exception.ErrorCode.NO_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final JwtDecoder jwtDecoder;


    public void save(ChatMessageDto messageDto, String token) {
        // username 세팅
        String username = "";
        String sender = "";

        if(messageDto.getMessage().trim().equals("")){
            throw new CustomException(NO_MESSAGE);
        }

        if (!(String.valueOf(token).equals("Authorization") || String.valueOf(token).equals("null"))) {
            String tokenInfo = token.substring(7); // Bearer빼고
            username = jwtDecoder.decodeNickName(tokenInfo);
            sender = jwtDecoder.decodeUsername(tokenInfo);
        }

        ChatMessage message = new ChatMessage(messageDto);

        message.setSender(sender);
        message.setNickname(username);

        // 시간 세팅
        Date date = new Date();
        message.setCreatedAt(date);
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            chatRoomRepository.enterChatRoom(message.getRoomId());
//            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        } else {
            chatMessageRepository.save(message);
        }
        // Websocket에 발행된 메시지를 redis로 발행한다(publish)
        redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
    }

    // redis 에 저장되어 있는 message 출력
    public List<ChatMessage> getMessages(String roomId) {
        log.info("getMessage");
        List<ChatMessage> chatMessageList = chatMessageRepository.findAllMessage(roomId);

        return chatMessageList;
    }
}
