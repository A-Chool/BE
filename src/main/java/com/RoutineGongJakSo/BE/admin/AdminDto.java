package com.RoutineGongJakSo.BE.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class AdminDto {

    @Getter
    @Setter
    //관리자 로그인 request
    public static class RequestDto {
        private String email;
        private String password;
    }

    @Getter
    @Builder
    //전체 유저 조회용
    public static class ResponseDto {
        private Long userId;
        private String userEmail;
        private String userName;
        private String phoneNumber;
        private int userLevel;
        private Long kakaoId;
        private String naverId;
    }

    @Getter
    @Setter
    //권한수정
    public static class UpdateDto {
        private int userLevel;
    }
}
