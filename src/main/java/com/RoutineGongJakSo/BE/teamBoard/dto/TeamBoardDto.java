package com.RoutineGongJakSo.BE.teamBoard.dto;

import com.RoutineGongJakSo.BE.admin.dto.MemberDto;
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
    public List<WeekTeamDto> weekTeamDtoList;
    public Long teamId;
    public String teamName;
    public List<MemberDto> memberDtoList;
    public String groundRule;
    public String workSpace;

    public TeamBoardDto(WeekTeam weekTeam) {
        this.teamId = weekTeam.getWeekTeamId();
        this.teamName = weekTeam.getTeamName();
        this.groundRule = weekTeam.getGroundRole();
        this.workSpace = weekTeam.getWorkSpace();
    }
}
