package com.RoutineGongJakSo.BE.board;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    //게시판 리스트 조회
    @GetMapping("/api/user/board")
    public List<BoardDto.boardDto> findAll(){
        return boardService.findAll();
    }
}
