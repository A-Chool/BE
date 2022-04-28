package com.RoutineGongJakSo.BE.Social;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class kakaoUserInfoDto {
    private Long kakaoId;
    private String userName;
    private String email;
}
