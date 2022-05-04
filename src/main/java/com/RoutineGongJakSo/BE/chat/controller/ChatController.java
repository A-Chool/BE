package com.RoutineGongJakSo.BE.chat.controller;


import com.RoutineGongJakSo.BE.chat.model.ChatMessage;
import com.RoutineGongJakSo.BE.chat.pubsub.RedisPublisher;
import com.RoutineGongJakSo.BE.chat.repo.ChatRoomRepository;
import com.RoutineGongJakSo.BE.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageService chatMessageService;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            chatRoomRepository.enterChatRoom(message.getRoomId());
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        System.out.println("message = " + message);
        System.out.println("message.getSender() = " + message.getSender());
        System.out.println("message.getMessage() = " + message.getMessage());

        // Websocket에 발행된 메시지를 redis로 발행한다(publish)
        redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
        chatMessageService.save(message);
    }
}
