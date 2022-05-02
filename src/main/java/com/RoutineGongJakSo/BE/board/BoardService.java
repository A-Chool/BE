package com.RoutineGongJakSo.BE.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    //전체조회
    public List<BoardDto> findAll() {
        List<Board> list = boardRepository.findAll();
        List<BoardDto> boardDtos = new ArrayList<>();
        for (Board board : list) {
            System.out.println(board);
            BoardDto boardDto = new BoardDto(board);
            boardDtos.add(boardDto);
        }
        return boardDtos;
    }

}
