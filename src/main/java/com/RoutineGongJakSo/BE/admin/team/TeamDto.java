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
    public static class CreateResponseDto{
        private Long teamId;
        private String teamName;

        public CreateResponseDto(Team team){
            this.teamId = team.getTeamId();
            this.teamName = team.getTeamName();
        }
    }


    @Getter
    public static class AddTeamDto {
        private Long teamId;
        private Long userId;
    }

    //주차별 팀 response
    @Getter
    @Builder
    public static class ResponseDto {
        private String teamName;
        private List<GetUserList> userList;
    }

    @Getter
    @Builder
    public static class GetUserList {
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
    public static class GetNoMember {
        private String userName;
        private Long userId;
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

