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
    public Long update(Long teamId, TeamBoardtDto requestDto) {
        TeamBoard entity = TeamBoardRepository.findById(teamId);

        entity.update(requestDto.getTeamId(), requestDto.getGroundrule());

        return TeamBoardRepository.save(entity).getTeamId();
    }
}