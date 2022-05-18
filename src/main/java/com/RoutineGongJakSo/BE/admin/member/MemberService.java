package com.RoutineGongJakSo.BE.admin.member;

import com.RoutineGongJakSo.BE.admin.team.Team;
import com.RoutineGongJakSo.BE.admin.team.TeamDto;
import com.RoutineGongJakSo.BE.admin.team.TeamRepository;
import com.RoutineGongJakSo.BE.admin.team.WeekTeam;
import com.RoutineGongJakSo.BE.admin.week.Week;
import com.RoutineGongJakSo.BE.admin.week.WeekRepository;
import com.RoutineGongJakSo.BE.client.user.User;
import com.RoutineGongJakSo.BE.client.user.UserController;
import com.RoutineGongJakSo.BE.client.user.UserRepository;
import com.RoutineGongJakSo.BE.exception.CustomException;
import com.RoutineGongJakSo.BE.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final WeekRepository weekRepository;

    // 팀원 추가
    @Transactional
    public String addMembers(Long weekId, MemberDto.RequestDto addTeamDto) {

        Long targetTeamId = addTeamDto.getTeamId();
        Long targetUserId = addTeamDto.getUserId();

        Week targetWeek = weekRepository.findById(weekId).orElseThrow(
                ()-> new CustomException(ErrorCode.NOT_FOUND_WEEK_ID)
        );

        Team targetTeam = teamRepository.findById(targetTeamId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_TEAM_ID)
        );

        User targetUser = userRepository.findById(targetUserId).orElseThrow(
                ()-> new CustomException(ErrorCode.NOT_FOUND_USER_ID)
        );

        // 이미 소속된 팀이 존재하는지 확인
        List<Team> teamList = targetWeek.getTeamList();
        for(Team _team : teamList){
            List<Member> memberList = _team.getMemberList();
            System.out.println("_team.getTeamId() = " + _team.getTeamId());
            System.out.println("_team.getTeamName() = " + _team.getTeamName());

            for(Member _member : memberList){
                if(_member.getUser() == targetUser){
                    System.out.println("_member.getUser().getUserName() = " + _member.getUser().getUserName());
                    System.out.println("targetUser.getUserName() = " + targetUser.getUserName());
                    throw new CustomException(ErrorCode.SOLD_OUT_USER);
                }
            }
        }

        Member member = Member.builder()
                .weekTeam(null)
                .team(targetTeam)
                .user(targetUser)
                .build();

        targetUser.addMember(member);
        targetTeam.addMember(member);

        memberRepository.save(member);

        return "팀원 추가 완료!";
    }
}
