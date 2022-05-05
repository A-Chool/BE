package com.RoutineGongJakSo.BE.teamBoard;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class teamBoardDto {
    private String week;
    private Long teamId;
    private String workSpace;

    @Builder
    public teamBoardDto(String week, Long teamId, String workSpace) {
        this.week = week;
        this.teamId = teamId;
        this.workSpace = workSpace;
    }
}
