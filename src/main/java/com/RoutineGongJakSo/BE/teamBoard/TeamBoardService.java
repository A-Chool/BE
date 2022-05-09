package com.RoutineGongJakSo.BE.teamBoard;

import com.RoutineGongJakSo.BE.model.Member;
import com.RoutineGongJakSo.BE.model.WeekTeam;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import com.RoutineGongJakSo.BE.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class TeamBoardService {

    private final TeamBoardRepository teamBoardRepository;
    private final Validator validator;

    //팀정보 보기
    @Transactional
    public List<TeamBoardDto.WeekTeamDto> getWeekTeamList(UserDetailsImpl userDetails, String week) {
        // 로그인 여부 확인
        validator.loginCheck(userDetails);

        //해당 주차의 팀 조회
        List<WeekTeam> weekTeamList = TeamBoardRepository.findByWeek(week);
        List<TeamBoardtDto.WeekteamDto> weekteamDtoList = new ArrayList<>();

        for (WeekTeam weekTeam : weekTeamList) {
            List<MemberDto> memberDtoList = new ArrayList<>();

            for (Member member : weekTeam.getMemberList()) {

                MemberDto memberDto = new MemberDto();
                memberDto.setMemberId(member.getMemberId());
                memberDto.getUser(new UserDto(member.getUser()));
                memberDtoList.add(memberDto);
            }

            TeamBoardtDto.WeekTeamDto weekTeamDto = TeamBoardtDto.WeekTeamDto.builder()
                    .teamId(weekTeam.getWeekTeamId())
                    .teamName(weekTeam.getTeamName())
                    .week(weekTeam.getWeek())
                    .weekTeamList(MemberDtoList)
                    .build();
            weekteamDtoList.add(weekTeamDto);
        }
    }

    //그라운드룰
    @Transactional

    public Long updateGroundrule(Long teamId, TeamBoardDto.requestDto requestDto) {
        TeamBoard entity = TeamBoardRepository.findById(teamId);

        entity.updateGroundrule(requestDto.getTeamId(), requestDto.getGroundrule());

        return TeamBoardRepository.save(entity).getTeamId();
    }

    //워크스페이스
    @Transactional
    public Long updateWorkSpace(Long teamId, TeamBoardtDto.RequestDto requestDto) {
        TeamBoard entity = TeamBoardRepository.findById(teamId);

        entity.updateWorkSpace(requestDto.getTeamId(), requestDto.getWorkSpace());

        return TeamBoardRepository.save(entity.getTeamId());
    }
}