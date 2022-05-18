package com.RoutineGongJakSo.BE.admin.team;

import com.RoutineGongJakSo.BE.admin.member.MemberDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


public class TeamDto {

    //팀 생성 요청
    @Getter
    @Setter
    public static class CreateTeamDto {
        private String teamName;
    }

    @Getter
    @Setter
    public static class CreateResponseDto {
        private Long teamId;
        private String teamName;

        public CreateResponseDto(Team team) {
            this.teamId = team.getTeamId();
            this.teamName = team.getTeamName();
        }
    }


    @Getter
    @Builder
    public static class WeekTeamDto {
        private Long teamId;
        private String teamName;
        private Long weekId;
        private String weekName;
        private List<MemberDto.ResponseDto> memberList;
    }
}

