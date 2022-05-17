package com.RoutineGongJakSo.BE.client.chat.dto;

import com.RoutineGongJakSo.BE.client.chat.model.ChatMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {
    // 메시지 타입 : 입장, 채팅
    private ChatMessage.MessageType type; // 메시지 타입
    private String roomId; // 방번호
    private String message; // 메시지
}
