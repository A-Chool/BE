package com.RoutineGongJakSo.BE.teamBoard;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/teamBoard")
@RequiredArgsConstructor
public class TeamBoardController {

    private final TeamBoardService teamBoardService;


    //워크스페이스 생성
    @PostMapping("/workSpace")
    public String save(@RequestBody final TeamBoardRequestDto params) {
        return teamBoardService.save(params);
    }


    //워크스페이스 수정
    @PatchMapping("/workSpace")
    public String update(@PathVariable final String workSpace, @RequestBody final TeamBoardRequestDto params) {
        return teamBoardService.update(workSpace, params);
    }
}


//
//    //그라운드룰 수정
//    @PutMapping("/api/user/teamBoard/groundRule") {
//
//    }
//    //팀정보 보기
//    @GetMapping
//    public List<TeamDto.weekTeamDto> getTeamList(@PathVariable String week) {
//        return TeamBoardService.getWeekTeamList(userDetails, week);
//    }

