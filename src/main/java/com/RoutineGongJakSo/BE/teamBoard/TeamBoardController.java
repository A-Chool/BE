package com.RoutineGongJakSo.BE.teamBoard;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/teamBoard")
@RequiredArgsConstructor
public class TeamBoardController {

    private final TeamBoardService teamBoardService;

    @PostMapping("/groundRule")
    public Long save(@RequestBody TeamBoardtDto requestDto) {
        return teamBoarService.save(requestDto);
    }

    @PutMapping("/goundRule/{teamId}")
    public Long update(@PathVariable long teamId, @RequestBody TeamBoardDto requestDto) {
        return teamBoardService.update(teamId, requestDto);
    }
}