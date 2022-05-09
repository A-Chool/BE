package com.RoutineGongJakSo.BE.teamBoard.controller;

import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.teamBoard.dto.TeamBoardDto;
import com.RoutineGongJakSo.BE.teamBoard.service.TeamBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamBoardController {

    private final TeamBoardService teamBoardService;

    //팀 추가
    @GetMapping("/api/user/teamBoard")
    public TeamBoardDto createTeam(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return teamBoardService.getTeamBoard(userDetails);
    }
}
