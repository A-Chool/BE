package com.RoutineGongJakSo.BE.client.chat.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomDto {

    public String roomId;
    public String name;
    public Object lastMessage;
}
