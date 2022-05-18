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
    @GetMapping("/{weekId}")
    public List<TeamDto.WeekTeamDto> getTeamList(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long weekId) {

        log.info("GET /admin/admin/teams/" + weekId);
        //로그인 여부 확인
        validator.loginCheck(userDetails);

        //접근권한 확인
        validator.adminCheck(userDetails);
        return teamService.getTeamList(weekId);
    }

    @DeleteMapping("/{teamId}")
    public String deleteTeam(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long teamId) {

        log.info("DELETE /admin/admin/teams/" + teamId);
        //로그인 여부 확인
        validator.loginCheck(userDetails);

        //접근권한 확인
        validator.adminCheck(userDetails);
        return teamService.deleteTeam(teamId);

    }
}
