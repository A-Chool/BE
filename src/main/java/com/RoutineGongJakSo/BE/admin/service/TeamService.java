package com.RoutineGongJakSo.BE.admin.service;

import com.RoutineGongJakSo.BE.admin.dto.TeamDto;
import com.RoutineGongJakSo.BE.admin.repository.MemberRepository;
import com.RoutineGongJakSo.BE.admin.repository.WeekTeamRepository;
import com.RoutineGongJakSo.BE.model.User;
import com.RoutineGongJakSo.BE.model.WeekTeam;
import com.RoutineGongJakSo.BE.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final WeekTeamRepository weekTeamRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public String createTeam(UserDetailsImpl userDetails, TeamDto.createTeamDto teamDto) {

        // 로그인 여부 확인
        validator.loginCheck(userDetails);

        User admin = userRepository.findByUserEmail(userDetails.getUserEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 userId 입니다."));

        //관리자 접근 권한 확인
        validator.adminCheck(admin);

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
                .roomId(teamDto.getWeek() + "주차 " + teamDto.getTeamName())
                .build();

        weekTeamRepository.save(weekTeam);

        return "팀 생성 완료!";
    }

    @Transactional
    public String addMembers(UserDetailsImpl userDetails, TeamDto.addMember teamDto) {

        // 로그인 여부 확인
        validator.loginCheck(userDetails);

        User admin = userRepository.findByUserEmail(userDetails.getUserEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 userId 입니다."));

        //관리자 접근 권한 확인
        validator.adminCheck(admin);

        return "팀원 추가 완료!";
    }
}
