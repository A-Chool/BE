package com.RoutineGongJakSo.BE.client.teamBoard.dto;

import com.RoutineGongJakSo.BE.admin.member.MemberDto;
import com.RoutineGongJakSo.BE.admin.team.Team;
import com.RoutineGongJakSo.BE.admin.team.WeekTeam;
import com.RoutineGongJakSo.BE.client.toDo.ToDoDto;
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
    public List<TeamDto> teamDtoList;
    public Long teamId;
    public String teamName;
    public String weekName;
    public List<MemberDto.ResponseDto> memberList;
    public String groundRule;
    public String workSpace;
    public List<ToDoDto.OriginToDoDto> toDoList;

    public TeamBoardDto(Team team) {
        this.teamId = team.getTeamId();
        this.teamName = team.getTeamName();
        this.weekName = team.getWeek().getWeekName();
        this.groundRule = team.getGroundRule();
        this.workSpace = team.getWorkSpace();
    }
}
