package com.RoutineGongJakSo.BE.client.toDo;

import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ToDoController {

    private final TodoService todoService;

    //할 일 생성
    @PostMapping("/api/user/teamBoard")
    public void createToDo(@AuthenticationPrincipal UserDetailsImpl userDetails,
                           @RequestBody ToDoDto.RequestToDoDto requestToDoDto) {
        log.info("요청 메서드 [POST] /api/user/teamBoard");
        todoService.createToDo(userDetails, requestToDoDto);
    }

    //할 일 삭제
    @DeleteMapping("/api/user/teamBoard/{todoId}")
    public void deleteToDo(@AuthenticationPrincipal UserDetailsImpl userDetails,
                           @PathVariable Long todoId) {
        log.info("요청 메서드 [DELETE] /api/user/teamBoard/{todoId}");
        todoService.deleteToDo(userDetails, todoId);
    }

    //할 일 수정
    @PutMapping("/api/user/teamBoard/{todoId}")
    public ToDoDto.ResponseDto updateToDo(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @RequestBody ToDoDto.UpDateToDoDto dto,
                                          @PathVariable Long todoId) {
        log.info("요청 메서드 [PUT] /api/user/teamBoard/{todoId}");
        return todoService.updateToDo(userDetails, dto, todoId);
    }

    //완료 여부
    @PutMapping("/api/user/teamBoard/check/{todoId}")
    public ToDoDto.ResponseDto updateCheck(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @PathVariable Long todoId) {
        log.info("요청 메서드 [PUT] /api/user/teamBoard/check/{todoId}");
        return todoService.updateCheck(userDetails, todoId);
    }
}
