package com.RoutineGongJakSo.BE.admin.admin;

import com.RoutineGongJakSo.BE.client.user.User;
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
        private String createdAt;

        public ResponseDto(User user) {
            this.userId = user.getUserId();
            this.userEmail = user.getUserEmail();
            this.userName = user.getUserName();
            this.phoneNumber = user.getPhoneNumber();
            this.userLevel = user.getUserLevel();
            this.kakaoId = user.getKakaoId();
            this.naverId = user.getNaverId();
            this.createdAt = user.getCreatedAt();
        }
    }

    @Getter
    @Setter
    //권한수정
    public static class UpdateDto {
        private int userLevel;
    }
}
