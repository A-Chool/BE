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
    public List<BoardListDto> findAll() {
        List<Board> list = boardRepository.findAll();
        List<BoardListDto> boardListDtos = new ArrayList<>();
        for (Board board : list) {
            System.out.println(board);
             BoardListDto boardListDto = new BoardListDto(board);
            boardListDtos.add(boardListDto);
        }
        return boardListDtos;
    }

}
