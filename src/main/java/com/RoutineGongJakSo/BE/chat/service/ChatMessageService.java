package com.RoutineGongJakSo.BE.chat.service;

import com.RoutineGongJakSo.BE.chat.model.ChatMessage;
import com.RoutineGongJakSo.BE.chat.repo.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private ChatMessageRepository chatMessageRepository;

    public void save(ChatMessage chatMessage){
        System.out.println("ChatMessageService Save!");
        chatMessageRepository.save(chatMessage);
    }
}
