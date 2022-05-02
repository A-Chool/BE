package com.RoutineGongJakSo.BE.board;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardDto {

    private Long id;
    private String boardName;
    private int boardLevel;

    public BoardDto(Board board) {
        this.id = board.getId();
        this.boardName = board.getboardName();
        this.boardLevel = board.getBoardLevel();
    }
}