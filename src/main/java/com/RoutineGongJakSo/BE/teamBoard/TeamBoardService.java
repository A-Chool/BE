package com.RoutineGongJakSo.BE.teamBoard;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//팀 대시보드
public class TeamBoardService {

    @Autowired
    private final TeamBoardRepository teamBoardRepository;

    //생성
    public TeamBoard save(String teamBoard) {

        teamBoardRepository.save(teamBoard);
    }

    //수정


    public void update(String teamBoard) {

        teamBoardRepository.update(teamBoard);
    }

}
