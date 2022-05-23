package com.RoutineGongJakSo.BE.client.myPage;

import lombok.Builder;
import lombok.Getter;

public class MyPageDto {

    @Getter
    public static class PutRequestDto{
        private String userName;
        private String userTag;
        private String userGitHub;
        private String findKakaoId;
        private String phoneNumber;
    }

    @Getter
    @Builder
    public static class ResponseDto{
        private Long userId;
        private String userEmail;
        private String userImage;
        private String username;
        private String userPhoneNumber;
        private String userTag;
        private String userGitHub;
        private String findKakaoId;
        private Long kakaoId;
    }
}


