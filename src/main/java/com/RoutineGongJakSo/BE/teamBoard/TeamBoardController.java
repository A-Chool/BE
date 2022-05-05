package com.RoutineGongJakSo.BE.teamBoard;

import com.RoutineGongJakSo.BE.admin.dto.TeamDto;
import com.RoutineGongJakSo.BE.admin.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TeamBoardController {

    private final TeamService teamService;
    private final TeamBoardService teamBoardService;

    //팀정보 보기
    @GetMapping("/api/user/teamBoard")
    public List<TeamDto.weekTeamDto> getTeamList(@PathVariable String week) {
        return TeamBoardService.getWeekTeamList(userDetails, week);
    }

//    //워크스페이스 수정
//    @PutMapping("/api/user/teamBoard/workSpace") {
//
//    }
//
//    //그라운드룰 수정
//    @PutMapping("/api/user/teamBoard/groundRule") {
//
//    }


}
