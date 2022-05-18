package com.RoutineGongJakSo.BE.client.checkIn;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class CheckInListDto {

    @Getter
    @Builder
    public static class TeamListDto { //최종 결과값 담기는 곳
        private Long teamId;
        private String teamName;
        private Long weekId;
        private String weekName;
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

    @Getter
    @Builder
    public static class CheckInDto{
        private String daySumTime; // 하루 누적 공부시간
        private String totalSumTime; // 총 누적 공부시간
        private List<TodayLogDto> todayLog; // 로그인, 로그아웃 기록
    }

    @Getter
    @Builder
    public static class TodayLogDto{ //당일 로그인 로그아웃 기록
        private String checkIn;
        private String checkOut;
    }


}
