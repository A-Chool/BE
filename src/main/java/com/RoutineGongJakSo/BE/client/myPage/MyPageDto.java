package com.RoutineGongJakSo.BE.client.myPage;

import lombok.Builder;
import lombok.Getter;

public class MyPageDto {

    @Getter
    public static class PutRequestDto{
        private String userNickName;
        private String userTag;
        private String userGitHub;
        private String userKakao;
    }

    @Getter
    @Builder
    public static class ResponseDto{
        private Long userId;
        private String userEmail;
        private String userImage;
        private String kakaoNickName;
        private String userPhoneNumber;
        private String userTag;
        private String userGitHub;
        private String userKakao;
    }
}


