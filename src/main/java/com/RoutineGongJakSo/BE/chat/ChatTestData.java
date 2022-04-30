package com.RoutineGongJakSo.BE.chat;

import com.RoutineGongJakSo.BE.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ChatTestData implements ApplicationRunner {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatService chatService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String roomName = "1조";
        ChatRoom chatRoom = chatService.createRoom(roomName);
        System.out.println("roomName = " + chatRoom.getRoomId());
    }
}
