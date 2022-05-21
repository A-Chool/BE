package com.RoutineGongJakSo.BE.client.chat.controller;

import com.RoutineGongJakSo.BE.client.chat.dto.ChatRoomDto;
import com.RoutineGongJakSo.BE.client.chat.model.ChatRoom;
import com.RoutineGongJakSo.BE.client.chat.repo.ChatRoomRepository;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoomDto> room(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("요청 메서드 [GET] /chat/rooms");
        return chatRoomRepository.findAllRoom(userDetails);
    }

    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String name) {
        log.info("요청 메서드 [POST] /chat/room");
        return chatRoomRepository.createChatRoom(name);
    }

    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        log.info("요청 메서드 [GET] /chat/room/{roomId}");
        return chatRoomRepository.findRoomById(roomId);
    }
}
