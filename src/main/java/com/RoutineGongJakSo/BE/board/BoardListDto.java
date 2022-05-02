package com.RoutineGongJakSo.BE.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardListDto {

    private Long id;
    private String title;

    @Builder
    public void Board(Long id, String title) {
    this.id = id;
    this.title = title;
    }
}