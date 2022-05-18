package com.RoutineGongJakSo.BE.admin.member;

import com.RoutineGongJakSo.BE.admin.team.TeamDto;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final Validator validator;
    private final MemberService memberService;
    //팀원 추가
    @PostMapping("/api/admin/member/{weekId}")
    public String addMembers(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody MemberDto.RequestDto addTeamDto,
            @PathVariable Long weekId) {
        log.info("GET /admin/admin/teams/members");
        log.info("teamId : " + addTeamDto.getTeamId() + "userId : " + addTeamDto.getUserId());
        //로그인 여부 확인
        validator.loginCheck(userDetails);

        //접근권한 확인
        validator.adminCheck(userDetails);
        return memberService.addMembers(weekId, addTeamDto);
    }
}
