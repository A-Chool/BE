package com.RoutineGongJakSo.BE.checkIn;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class CheckInListDto {

    @Getter
    @Builder
    public static class TeamListDto { //최종 결과값 담기는 곳
        private Long teamId;
        private String teamName;
        private String week;
        private List<UserDto> memberList;

    }

    @Getter
    @Builder
    public static class UserDto {
        private Long memberId;
        private String userEmail;
        private String userName;
        private String phoneNumber;
        private boolean online; //로그인 여부
        private boolean lateCheck; //지각 여부
    }


}
