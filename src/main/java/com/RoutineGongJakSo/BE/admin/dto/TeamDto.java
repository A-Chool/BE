package com.RoutineGongJakSo.BE.admin.dto;

import lombok.Getter;


public class TeamDto {

    //팀 생성 요청
    @Getter
    public static class createTeamDto{
        private String teamName;
        private Long week;
    }

    //팀원 추가 요청
    @Getter
    public static class addMember {
        private String teamName;
        private Long teamId;
        private Long MemberId;
    }
}

