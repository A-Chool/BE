package com.RoutineGongJakSo.BE.teamBoard;

import lombok.Getter;

@Getter
public class TeamBoardResponseDto {
    private String week;
    private Long teamId;
    private String workSpace;

    public TeamBoardResponseDto(TeamBoard entity) {
        this.week = entity.getWeek();
        this.teamId = entity.getTeamId();
        this.workSpace = entity.getWorkSpace();
    }
}