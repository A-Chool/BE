package com.RoutineGongJakSo.BE.admin.controller;

import com.RoutineGongJakSo.BE.admin.dto.TeamDto;
import com.RoutineGongJakSo.BE.admin.service.TeamService;
import com.RoutineGongJakSo.BE.model.User;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/teams")
public class TeamController {

    private final TeamService teamService;

    //팀 추가
    @PostMapping("/")
    public String createTeam(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody TeamDto.createTeamDto teamDto) {
        return teamService.createTeam(userDetails, teamDto);
    }

    //해당 주차의 모든 팀을 조회
    @GetMapping("/{week}")
    public Map<String, Object> getTeamList(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable String week) {
        return teamService.getTeamList(userDetails, week);
    }

    //팀원 추가
    @PostMapping("/members")
    public String addMembers(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody TeamDto.addTeamDto addTeamDto) {
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
    public ArrayList<String> getWeeks(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return teamService.getWeeks(userDetails);
    }


}
