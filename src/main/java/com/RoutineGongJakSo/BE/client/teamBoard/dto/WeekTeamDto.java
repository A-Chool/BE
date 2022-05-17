package com.RoutineGongJakSo.BE.client.teamBoard.dto;

import com.RoutineGongJakSo.BE.admin.team.WeekTeam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeekTeamDto {
    public Long weekTeamId;
    public String week;
    public String weekTeamName;

    public WeekTeamDto(WeekTeam weekTeam) {
        this.weekTeamId = weekTeam.getWeekTeamId();
        this.week = weekTeam.getWeek();
        this.weekTeamName = weekTeam.getTeamName();
    }
}
