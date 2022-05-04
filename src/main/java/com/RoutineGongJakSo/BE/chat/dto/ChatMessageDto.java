package com.RoutineGongJakSo.BE.chat.dto;

import com.RoutineGongJakSo.BE.chat.model.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private String sender;
    private String message;

    public ChatMessageDto(ChatMessage chatMessage) {
        this.sender = chatMessage.getSender();
        this.message = chatMessage.getMessage();
    }
}
