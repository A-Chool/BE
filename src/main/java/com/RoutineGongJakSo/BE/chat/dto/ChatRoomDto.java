package com.RoutineGongJakSo.BE.chat.dto;


import com.RoutineGongJakSo.BE.chat.model.ChatMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomDto {

    public String roomId;
    public String name;
    public Object lastMessage;
}
