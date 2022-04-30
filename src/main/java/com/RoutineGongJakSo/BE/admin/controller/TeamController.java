package com.RoutineGongJakSo.BE.admin.controller;

import com.RoutineGongJakSo.BE.admin.dto.TeamDto;
import com.RoutineGongJakSo.BE.admin.service.TeamService;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/teams")
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/")
    public String createTeam(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody TeamDto.createTeamDto teamDto){
        return teamService.createTeam(userDetails, teamDto);
    }

    @PostMapping("/members")
    public String addMembers(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody TeamDto.addMember teamDto){
        return teamService.addMembers(userDetails, teamDto);
    }
}
