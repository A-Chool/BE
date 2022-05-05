package com.RoutineGongJakSo.BE.admin.dto;

import com.RoutineGongJakSo.BE.model.User;
import com.RoutineGongJakSo.BE.user.UserDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {
    private Long memberId;
    private UserDto user;
}
