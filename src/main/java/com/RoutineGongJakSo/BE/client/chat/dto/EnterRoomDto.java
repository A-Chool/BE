package com.RoutineGongJakSo.BE.client.chat.dto;

import com.RoutineGongJakSo.BE.client.chat.model.ChatMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EnterRoomDto {
    private Long prevId;
    private List<ChatMessage> chatMessageList;
}
