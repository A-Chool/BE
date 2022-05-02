//package com.RoutineGongJakSo.BE.chat;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/chat")
//public class ChatRoomController {
//
//    private final ChatRoomRepository chatRoomRepository;
//
//    // 채팅 리스트 화면
////    @GetMapping("/room")
////    public String rooms(Model model) {
////        return "/chat/room";
////    }
//
//    // 모든 채팅방 목록 반환
//    @GetMapping("/rooms")
//    public List<ChatRoom> room() {
//        return chatRoomRepository.findAllRoom();
//    }
//
//    // 채팅방 생성
//    @PostMapping("/room")
//    public ChatRoom createRoom(@RequestParam String name) {
//        return chatRoomRepository.createChatRoom(name);
//    }
//
//    // 채팅방 입장 화면
//    @GetMapping("/room/enter/{roomId}")
//    public String roomDetail(Model model, @PathVariable String roomId) {
//        model.addAttribute("roomId", roomId);
//        return "/chat/roomdetail";
//    }
//
//    // 특정 채팅방 조회
//    @GetMapping("/room/{roomId}")
//    public ChatRoom roomInfo(@PathVariable String roomId) {
//        return chatRoomRepository.findRoomById(roomId);
//    }
//}