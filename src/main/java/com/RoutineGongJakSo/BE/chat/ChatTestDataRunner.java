//package com.RoutineGongJakSo.BE.chat;
//
//import com.RoutineGongJakSo.BE.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ChatTestDataRunner implements ApplicationRunner {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private ChatRoomRepository chatRoomRepository;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        ChatRoom chatRoom = chatRoomRepository.createChatRoom("1조");
//        String roomId = chatRoom.getRoomId();
//        String roomName = chatRoom.getName();
//        System.out.println("roomId = " + roomId);
//        System.out.println("roomName = " + roomName);
//    }
//}
