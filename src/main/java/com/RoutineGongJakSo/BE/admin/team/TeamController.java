package com.RoutineGongJakSo.BE.admin.team;

import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.exception.StatusResponseDto;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/teams")
public class TeamController {

    private final TeamService teamService;
    private final Validator validator;

    //팀 추가
    @PostMapping("/{weekId}")
    public ResponseEntity<Object> createTeam(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody TeamDto.CreateTeamDto teamDto,
            @PathVariable Long weekId) {

        log.info("POST /admin/admin/teams/" + weekId);
        //로그인 여부 확인
        validator.loginCheck(userDetails);

        //접근권한 확인
        validator.adminCheck(userDetails);
        TeamDto.CreateResponseDto responseDto = teamService.createTeam(weekId, teamDto);
        return new ResponseEntity<>(new StatusResponseDto("팀 추가 성공", responseDto), HttpStatus.OK);
    }

    //해당 주차의 모든 팀을 조회
    @GetMapping("/{week}")
    public List<TeamDto.WeekTeamDto> getTeamList(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable String week) {
        return teamService.getTeamList(userDetails, week);
    }

    //팀원 추가
    @PostMapping("/members")
    public String addMembers(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody TeamDto.AddTeamDto addTeamDto) {
        return teamService.addMembers(userDetails, addTeamDto);
    }

    //팀 삭제
    @DeleteMapping("/{teamId}")
    public String deleteTeam(@PathVariable Long teamId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return teamService.deleteTeam(teamId, userDetails);
    }

    //팀원 삭제
    @DeleteMapping("/members/{memberId}")
    public String deleteMember(@PathVariable Long memberId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return teamService.deleteMember(memberId, userDetails);
    }

    //주차 정보
    @GetMapping("/week")
    public ArrayList<String> getWeeks(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return teamService.getWeeks(userDetails);
    }

    //해당 주차에 멤버아이디가 없는 유저 리스트
    @GetMapping("/noMember/{week}")
    public List<TeamDto.GetNoMember> getNoMember(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable String week) {
        return teamService.getNoMember(userDetails, week);
    }
}
