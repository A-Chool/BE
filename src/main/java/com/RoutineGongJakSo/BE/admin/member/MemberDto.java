package com.RoutineGongJakSo.BE.admin.member;

import com.RoutineGongJakSo.BE.client.user.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class MemberDto {

    @Getter
    public static class RequestDto {
        private Long teamId;
        private Long userId;
    }

    @Getter
    @Setter
    public static class ResponseDto {
        private Long memberId;
        private UserDto user;
    }

    @Getter
    @Builder
    public static class GetNoMember {
        private String userName;
        private Long userId;
    }
}
