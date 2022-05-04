package com.RoutineGongJakSo.BE.chat.service;

import com.RoutineGongJakSo.BE.chat.dto.ChatMessageDto;
import com.RoutineGongJakSo.BE.chat.model.ChatMessage;
import com.RoutineGongJakSo.BE.chat.repo.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public void save(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessageDto> getMessages(String roomId) {
        List<ChatMessage> chatMessageList = chatMessageRepository.findAllMessage(roomId);
        List<ChatMessageDto> dtoList = new ArrayList<>();
        for (ChatMessage chatMessage : chatMessageList) {
            ChatMessageDto dto = new ChatMessageDto(chatMessage);
            dtoList.add(dto);
            System.out.println("dto = " + dto);
        }

        return dtoList;
    }
}
