package com.RoutineGongJakSo.BE.client.teamBoard.dto;

import com.RoutineGongJakSo.BE.admin.team.Team;
import com.RoutineGongJakSo.BE.admin.team.WeekTeam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamDto {
    private Long teamId;
    private String teamName;
    private Long weekId;
    private String weekName;

    public TeamDto(Team team) {
        this.teamId = team.getTeamId();
        this.teamName = team.getTeamName();
        this.weekId = team.getWeek().getWeekId();
        this.weekName = team.getWeek().getWeekName();
    }
}
