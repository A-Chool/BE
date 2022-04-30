package com.RoutineGongJakSo.BE.admin.service;

import com.RoutineGongJakSo.BE.admin.dto.TeamDto;
import com.RoutineGongJakSo.BE.admin.repository.MemberRepository;
import com.RoutineGongJakSo.BE.admin.repository.WeekTeamRepository;
import com.RoutineGongJakSo.BE.model.WeekTeam;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final Validator validator;
    private final WeekTeamRepository weekTeamRepository;
    private final MemberRepository memberRepository;

    // 팀 추가
    @Transactional
    public String createTeam(UserDetailsImpl userDetails, TeamDto.createTeamDto teamDto) {

        // 로그인 여부 확인
        validator.loginCheck(userDetails);

        //관리자 접근 권한 확인
        validator.adminCheck(userDetails);

        //중복 팀 체크
        Optional<WeekTeam> teamCheck = weekTeamRepository.findByTeamNameAndWeek(teamDto.getTeamName(), teamDto.getWeek());

        //이미 만들어진 팀이 있는지 확인
        validator.teamCheck(teamCheck);

        String groundRole = "";
        String workSpace = "";

        WeekTeam weekTeam = WeekTeam.builder()
                .teamName(teamDto.getTeamName())
                .week(teamDto.getWeek())
                .groundRole(groundRole)
                .workSpace(workSpace)
                .roomId(teamDto.getWeek() + "주차 " + teamDto.getTeamName()) //1주차 1조
                .build();

        weekTeamRepository.save(weekTeam);

        return "팀 생성 완료!";
    }

    // 팀원 추가
    @Transactional
    public String addMembers(UserDetailsImpl userDetails, TeamDto.addMember teamDto) {

        // 로그인 여부 확인
        validator.loginCheck(userDetails);

        //관리자 접근 권한 확인
        validator.adminCheck(userDetails);

        return "팀원 추가 완료!";
    }

    //팀 삭제
    @Transactional
    public String deleteTeam(Long teamId, UserDetailsImpl userDetails) {

        // 로그인 여부 확인
        validator.loginCheck(userDetails);

        //관리자 접근 권한 확인
        validator.adminCheck(userDetails);

        WeekTeam weekTeam = weekTeamRepository.findById(teamId).orElseThrow(
                () -> new NullPointerException("해당 팀이 존재하지 않습니다.")
        );

        weekTeamRepository.delete(weekTeam);

        return "삭제 완료";
    }
}
