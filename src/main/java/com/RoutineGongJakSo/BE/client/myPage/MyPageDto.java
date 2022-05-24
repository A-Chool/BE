package com.RoutineGongJakSo.BE.client.myPage;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class MyPageDto {

    @Getter
    public static class PutRequestDto{
        private String userName;
        private List<String> userTag;
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
        private List<String> userTag;
        private String userGitHub;
        private String findKakaoId;
        private Long kakaoId;
    }
}


