package com.RoutineGongJakSo.BE.admin.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;


public class TeamDto {

    //팀 생성 요청
    @Getter
    public static class createTeamDto {
        private String teamName;
        private String week;
    }

    @Getter
    public static class addTeamDto{
        private Long teamId;
        private Long userId;
    }

    //주차별 팀 response
    @Getter
    @Builder
    public static class responseDto {
        private String teamName;
        private List<getUserList> userList;
    }

    @Getter
    @Builder
    public static class getUserList {
        private Long userId;
        private String userName;
        private String userEmail;
        private String phoneNumber;
        private Long kakaoId;
        private String createdAt;
        private Long teamId;
        private Long memberId;

    }

    @Getter
    @Builder
    public static class getNoMember{
        private String userName;
        private Long userId;
    }

}

