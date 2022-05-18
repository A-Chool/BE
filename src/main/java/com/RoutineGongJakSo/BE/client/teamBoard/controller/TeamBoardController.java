package com.RoutineGongJakSo.BE.client.teamBoard.controller;

import com.RoutineGongJakSo.BE.client.teamBoard.dto.GroundRuleDto;
import com.RoutineGongJakSo.BE.client.teamBoard.dto.TeamBoardDto;
import com.RoutineGongJakSo.BE.client.teamBoard.dto.WorkSpaceDto;
import com.RoutineGongJakSo.BE.client.teamBoard.service.TeamBoardService;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TeamBoardController {

    private final TeamBoardService teamBoardService;

    // TeamBoard 클릭 시
    @GetMapping("/api/user/teamBoard")
    public TeamBoardDto getAllTeamBoard(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return teamBoardService.getAllTeamBoard(userDetails);
    }

    // TeamBoard 클릭 시
    @GetMapping("/api/user/teamBoard/{teamId}")
    public TeamBoardDto getTeamBoard(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long teamId) {
        return teamBoardService.getTeamBoard(userDetails, teamId);
    }

    // 그라운드 룰 수정
    @PutMapping("/api/user/teamBoard/groundRule/{teamId}")
    public void updateGroundRule(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long teamId,
            @RequestBody GroundRuleDto groundRule) {
        teamBoardService.updateGroundRule(userDetails, teamId, groundRule);
    }

    // 워크스페이스 수정
    @PutMapping("/api/user/teamBoard/workSpace/{teamId}")
    public void updateWorkSpace(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long teamId,
            @RequestBody WorkSpaceDto workSpace) {
        teamBoardService.updateWorkSpace(userDetails, teamId, workSpace);
    }

}
