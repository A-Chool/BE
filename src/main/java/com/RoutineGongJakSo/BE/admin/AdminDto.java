package com.RoutineGongJakSo.BE.admin;

import lombok.*;

public class AdminDto {

    @Getter
    @Setter
    //관리자 로그인 request
    public static class RequestDto{
        private String email;
        private String password;
    }

    @Getter
    @Setter
    //권한수정
    public static class Update{
        private int userLevel;
    }
}
