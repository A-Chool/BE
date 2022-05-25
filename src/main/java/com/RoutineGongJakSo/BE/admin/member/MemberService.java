package com.RoutineGongJakSo.BE.admin.member;

import com.RoutineGongJakSo.BE.admin.team.Team;
import com.RoutineGongJakSo.BE.admin.team.TeamRepository;
import com.RoutineGongJakSo.BE.admin.week.Week;
import com.RoutineGongJakSo.BE.admin.week.WeekRepository;
import com.RoutineGongJakSo.BE.client.tag.Tag;
import com.RoutineGongJakSo.BE.client.user.User;
import com.RoutineGongJakSo.BE.client.user.UserDto;
import com.RoutineGongJakSo.BE.client.user.UserRepository;
import com.RoutineGongJakSo.BE.exception.CustomException;
import com.RoutineGongJakSo.BE.exception.ErrorCode;
import com.RoutineGongJakSo.BE.security.exception.UserExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final WeekRepository weekRepository;

    // 팀원 추가
    @Transactional
    public List<MemberDto.ResponseDto> addMembers(Long weekId, MemberDto.RequestDto addTeamDto) {

        Long targetTeamId = addTeamDto.getTeamId();
        Long targetUserId = addTeamDto.getUserId();

        Week targetWeek = weekRepository.findById(weekId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_WEEK_ID)
        );

        Team targetTeam = teamRepository.findById(targetTeamId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_TEAM_ID)
        );

        User targetUser = userRepository.findById(targetUserId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER_ID)
        );

        // 이미 소속된 팀이 존재하는지 확인
        List<Team> teamList = targetWeek.getTeamList();

        if (!teamList.contains(targetTeam)) {
            throw new CustomException(ErrorCode.NOT_FOUND_TEAM_IN_WEEK);
        }

        for (Team _team : teamList) {
            List<Member> memberList = _team.getMemberList();

            for (Member _member : memberList) {
                if (_member.getUser() == targetUser) {
                    throw new CustomException(ErrorCode.SOLD_OUT_USER);
                }
            }
        }

        Member member = Member.builder()
                .team(targetTeam)
                .user(targetUser)
                .build();

        targetUser.addMember(member);
        targetTeam.addMember(member);

        // 팀원 추가
        memberRepository.save(member);

        List<Member> memberList = targetTeam.getMemberList();
        List<MemberDto.ResponseDto> responseDtoList = new ArrayList<>();

        for (Member _member : memberList) {
            List<String> tagList = new ArrayList<>();
            for (Tag t : _member.getUser().getTagList()){
                tagList.add(t.getTag());
            }
            MemberDto.ResponseDto responseDto = new MemberDto.ResponseDto();
            responseDto.setMemberId(_member.getMemberId());
            responseDto.setUser(new UserDto(_member.getUser(), tagList));
            responseDtoList.add(responseDto);
        }

        log.info("추가된 팀원 리스트 {}", responseDtoList);

        return responseDtoList;
    }

    //팀원 삭제
    @Transactional
    public String deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_ID)
        );

        memberRepository.delete(member);

        log.info("삭제된 멤버 {}", member);

        return "삭제 완료";
    }


    //해당 주차에 멤버아이디가 없는 유저 리스트
    public List<MemberDto.GetNoMember> getNoMember(Long weekId) {
        log.info("getNoMember weekId : {}", weekId);

        // 쿼리 최적화 0524 HB
        List<User> allUserList = userRepository.findAll();
        List<Team> teamList = teamRepository.findByWeekId(weekId);

        for (Team team : teamList) {
            for (Member _member : team.getMemberList()) {
                allUserList.remove(_member.getUser());
            }
        }
        //return 값 가공하기

        //값을 return 할 CheckInListDto 만들기
        List<MemberDto.GetNoMember> noMembers = new ArrayList<>();

        for (User user : allUserList) {
            MemberDto.GetNoMember response = MemberDto.GetNoMember.builder()
                    .userId(user.getUserId())
                    .userName(user.getUserName())
                    .build();

            noMembers.add(response);
        }

        return noMembers;
    }
}
