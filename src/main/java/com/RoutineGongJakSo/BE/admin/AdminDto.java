package com.RoutineGongJakSo.BE.admin;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDto {
    //로그인 request
    private String email;
    private String password;
}
