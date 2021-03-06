package com.RoutineGongJakSo.BE.admin.member;

import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class MemberController {
    private final Validator validator;
    private final MemberService memberService;

    //팀원 추가
    @PostMapping("/member/{weekId}")
    public List<MemberDto.ResponseDto> addMembers(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody MemberDto.RequestDto addTeamDto,
            @PathVariable Long weekId) {
        log.info("GET /admin/admin/member/" + weekId);
        log.info("teamId : " + addTeamDto.getTeamId() + "userId : " + addTeamDto.getUserId());

        //로그인 여부 확인, 접근권한 확인
        validator.adminCheck(userDetails);
        return memberService.addMembers(weekId, addTeamDto);
    }

    //팀원 삭제
    @DeleteMapping("/member/{memberId}")
    public String deleteMember(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long memberId) {
        log.info("DELETE /admin/admin/member/" + memberId);

        //로그인 여부 확인, 접근권한 확인
        validator.adminCheck(userDetails);
        return memberService.deleteMember(memberId);
    }


    //해당 주차에 멤버아이디가 없는 유저 리스트
    @GetMapping("/noMember/{weekId}")
    public List<MemberDto.GetNoMember> getNoMember(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long weekId) {
        log.info("GET /admin/admin/noMember/" + weekId);

        //로그인 여부 확인, 접근권한 확인
        validator.adminCheck(userDetails);
        return memberService.getNoMember(weekId);
    }

}
