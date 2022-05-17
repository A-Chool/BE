package com.RoutineGongJakSo.BE.admin.member;

import com.RoutineGongJakSo.BE.client.user.UserDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {
    private Long memberId;
    private UserDto user;
}
