package com.RoutineGongJakSo.BE.teamBoard;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/teamBoard")
@RequiredArgsConstructor
public class TeamBoardController {

    private final TeamBoardService teamBoardService;

    @GetMapping("/groundRule{teamID}")
    public Long save(@RequestBody TeamBoardtDto.requestDto requestDto) {
        return teamBoarService.save(requestDto);
    }

    @PutMapping("/goundRule/{teamId}")
    public Long updateGroundrule(@PathVariable long teamId, @RequestBody TeamBoardDto.requestDto requestDto) {
        return teamBoardService.updateGroundrule(teamId, requestDto);
    }

    @PutMapping("/workSpace/{teamId}")
    public Long updateWorkSpace(@PathVariable Long teamId, @RequestBody TeamBoardtDto.requestDto requestDto) {
        return teamBoardService.updateWorkSpace(teamId, requestDto);
    }
}