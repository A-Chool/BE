package com.RoutineGongJakSo.BE.chat.service;

import com.RoutineGongJakSo.BE.chat.model.ChatMessage;
import com.RoutineGongJakSo.BE.chat.repo.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public void save(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
    }

//    public List<ChatMessage> getMessages(String roomId) {
//        List<ChatMessage> chatMessageList = chatMessageRepository.findAllMessage(roomId);
//
//        return chatMessageList;
//    }
}
