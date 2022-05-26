package com.RoutineGongJakSo.BE.client.myPage;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

public class MyPageDto {

    @Getter
    public static class PutRequestDto{
        @NotBlank(message = "이름은 필수 입력 값입니다.")
        @Pattern(regexp = "^([a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣]).{2,7}$", message = "이름은 한글, 영문, 숫자만 가능하며 2-8자리 가능합니다.")
        private String userName;

        private List<String> userTag;

        private String userGitHub;
        private String findKakaoId;

        @Pattern(regexp = "^[0-9]{3}-[0-9]{3,4}-[0-9]{4}$", message = "올바른 형식의 전화번호가 아닙니다.")
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


