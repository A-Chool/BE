package com.RoutineGongJakSo.BE.teamBoard;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/teamBoard")
@RequiredArgsConstructor
public class TeamBoardController {

    private final TeamBoardService teamBoardService;

    @GetMapping("/groundRule/{teamId}")
    public List<TeamBoardDto.WeekTeamDto> getWeekTeamList(@PathVariable Long teamId, @RequestBody TeamBoardDto.WeekTeamDto weekTeamDto) {
        return teamBoardService.getWeekTeamList();
    }

    @PutMapping("/goundRule/{teamId}")
    public Long updateGroundrule(@PathVariable long teamId, @RequestBody TeamBoardtDto.requestDto requestDto) {
        return teamBoardService.updateGroundrule(teamId, requestDto);
    }

    @PutMapping("/workSpace/{teamId}")
    public Long updateWorkSpace(@PathVariable Long teamId, @RequestBody TeamBoardtDto.requestDto requestDto) {
        return teamBoardService.updateWorkSpace(teamId, requestDto);
    }
}