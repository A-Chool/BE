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
    public List<BoardDto.boardDto> findAll() {
        List<Board> list = boardRepository.findAll();
        List<BoardDto.boardDto> boardDtos = new ArrayList<>();
        for (Board board : list) {
            BoardDto.boardDto responseDto = BoardDto.boardDto.builder()
                    .boardName(board.getBoardName())
                    .boardLevel(board.getBoardLevel())
                    .id(board.getId())
                    .build();
            boardDtos.add(responseDto);
        }
        return boardDtos;
    }
}

//    //게시판 이름으로 게시판 조회
//    public Board findBoard(String boardName){
//        return Optional.ofNullable(boardRepository.findByBoardName(boardName)).orElseThrow(OBJECT_NOT_EXIST::new);
//    }
//}

