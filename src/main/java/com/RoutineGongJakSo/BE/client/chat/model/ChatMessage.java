package com.RoutineGongJakSo.BE.client.chat.model;


import com.RoutineGongJakSo.BE.client.chat.dto.ChatMessageDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ChatMessage {
    // 메시지 타입 : 입장, 채팅
    public enum MessageType {
        ENTER, TALK
    }

    private MessageType type; // 메시지 타입
    private String roomId; // 방번호
    private String sender; // 메시지 보낸사람 id
    private String nickname;// 메시지 보낸사람 name
    private String message; // 메시지

//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private Date createdAt;

    public ChatMessage(){

    }

    public ChatMessage(ChatMessageDto dto){
        this.type = dto.getType();
        this.roomId = dto.getRoomId();
        this.message = dto.getMessage();
    }
}
