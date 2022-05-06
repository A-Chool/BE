package com.RoutineGongJakSo.BE.teamBoard;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/teamBoard")
@RequiredArgsConstructor
public class TeamBoardController {

    private final TeamBoardService teamBoardService;


    //워크스페이스 생성
    @GetMapping("/workSpace/save")
    public String save() {

        return "save";
    }

    @PostMapping("/workSpace/save")
    public String save(TeamBoard teamBoard) {

        teamBoardService.save(String.valueOf(teamBoard));

        return "";
    }

    @GetMapping("/workSpace/view")
    public String boardView(Model model, Long teamID) {

        model.addAttribute("teamBoard", teamBoardService.teamBoard);
        return "teamBoard";
    }

    //워크스페이스 수정
    @GetMapping("/workSpace/update")
    public String update(@PathVariable("teamId") Long teamId, Model model) {

        model.addAttribute("teamBoard", teamBoardService.update(teamId);
        return "update";
    }

    @PostMapping("/workSpace")
    public String update(@PathVariable("teamId") Long teamId, TeamBoard teamBoard) {

        TeamBoard teamBoardTemp = teamBoardService.save(String.valueOf(teamBoard));
        teamBoardTemp.setWorkSpace(teamBoard.getWorkSpace());

        return "redirect:/workSpace/save";
    }
}
