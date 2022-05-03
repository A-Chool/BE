package com.RoutineGongJakSo.BE.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardDto {

    private Long boardId;
    private String boardName;
    private int boardLevel;

//    public BoardDto(Board board) {
//        this.id = board.getId();
//        this.boardName = board.getboardName();
//        this.boardLevel = board.getBoardLevel();
//    }
    @Getter
    @Builder
    public static class boardDto{
        private Long boardId;
        private String boardName;
        private int boardLevel;
    }
}