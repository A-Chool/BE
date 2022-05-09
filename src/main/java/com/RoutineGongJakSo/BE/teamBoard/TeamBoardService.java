package com.RoutineGongJakSo.BE.teamBoard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor

public class TeamBoardService {

    private final TeamBoardRepository teamBoardRepository;

    //그라운드룰
    @Transactional
    public Long save(TeamBoardtDto requestDto) {

        return TeamBoardtDto.save(requestDto.toEntity()).getTeamId();
    }

    @Transactional
    public Long updateGroundrule(Long teamId, TeamBoardtDto.RequestDto requestDto) {
        TeamBoard entity = TeamBoardRepository.findById(teamId);

        entity.updateGroundrule(requestDto.getTeamId(), requestDto.getGroundrule());

        return TeamBoardRepository.save(entity).getTeamId();
    }

    @Transactional
    public Long updateWorkSpace(Long teamID, TeamBoardtDto.RequestDto requestDto) {
        TeamBoard entity = TeamBoardRepository.findById(teamID);

        entity.updateWorkSpace(requestDto.getTeamId(), requestDto.getWorkSpace());

        return TeamBoardRepository.save(entity.getTeamId());
    }
}