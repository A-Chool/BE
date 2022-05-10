package com.RoutineGongJakSo.BE.teamBoard.dto;

import com.RoutineGongJakSo.BE.admin.controller.dto.MemberDto;
import com.RoutineGongJakSo.BE.model.WeekTeam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamBoardDto {
    public List<WeekTeamDto> weekTeamList;
    public Long teamId;
    public String teamName;
    public String weekName;
    public List<MemberDto> memberList;
    public String groundRule;
    public String workSpace;
    public List<ToDoDto> toDoList;

    public TeamBoardDto(WeekTeam weekTeam) {
        this.teamId = weekTeam.getWeekTeamId();
        this.teamName = weekTeam.getTeamName();
        this.weekName = weekTeam.getWeek();
        this.groundRule = weekTeam.getGroundRole();
        this.workSpace = weekTeam.getWorkSpace();
    }
}
