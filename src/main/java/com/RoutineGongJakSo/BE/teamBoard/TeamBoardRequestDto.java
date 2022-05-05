package com.RoutineGongJakSo.BE.teamBoard;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamBoardRequestDto {

    private String week;
    private Long teamId;
    private String workSpace;

    public TeamBoard toEntity() {
        return TeamBoard.builder()
                .week(week)
                .teamId(teamId)
                .workSpace(workSpace)
                .build();
    }
}
