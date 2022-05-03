package com.RoutineGongJakSo.BE.admin.service;

import com.RoutineGongJakSo.BE.admin.dto.TeamDto;
import com.RoutineGongJakSo.BE.admin.repository.MemberRepository;
import com.RoutineGongJakSo.BE.admin.repository.WeekTeamRepository;
import com.RoutineGongJakSo.BE.model.Member;
import com.RoutineGongJakSo.BE.model.User;
import com.RoutineGongJakSo.BE.model.WeekTeam;
import com.RoutineGongJakSo.BE.repository.UserRepository;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final Validator validator;
    private final WeekTeamRepository weekTeamRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    // 팀 추가
    @Transactional
    public String createTeam(UserDetailsImpl userDetails, TeamDto.createTeamDto teamDto) {
        // 로그인 여부 확인
        validator.loginCheck(userDetails);
        //관리자 접근 권한 확인
        validator.adminCheck(userDetails);
        // ':' 사용 금지
        validator.teamNameCheck(teamDto.getTeamName());

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
                .roomId(teamDto.getWeek() + " " + teamDto.getTeamName()) //1주차 1조
                .build();

        weekTeamRepository.save(weekTeam);

        return "팀 생성 완료!";
    }

    // 팀원 추가
    @Transactional
    public String addMembers(UserDetailsImpl userDetails, TeamDto.addTeamDto addTeamDto) {
        // 로그인 여부 확인
        validator.loginCheck(userDetails);
        //관리자 접근 권한 확인
        validator.adminCheck(userDetails);

        WeekTeam weekTeam = weekTeamRepository.findById(addTeamDto.getTeamId()).orElseThrow(
                () -> new NullPointerException("해당 팀이 존재하지 않습니다.")
        );
        User user = userRepository.findById(addTeamDto.getUserId()).orElseThrow(
                () -> new NullPointerException("해당 유저가 존재하지 않습니다.")
        );

        // 이미 소속된 팀이 존재하는지 확인
        List<WeekTeam> weekTeamList = weekTeamRepository.findByWeek(weekTeam.getWeek());
        for (WeekTeam find : weekTeamList) {
            List<Member> findMember = memberRepository.findByWeekTeam(find);
            for (Member member : findMember) {
                if (member.getUser() == user) {
                    throw new NullPointerException("해당 유저는 이미 소속된 팀이 존재합니다.");
                }
            }
        }

        Member member = Member.builder()
                .weekTeam(weekTeam)
                .user(user)
                .build();

        memberRepository.save(member);

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

    //팀원 삭제
    @Transactional
    public String deleteMember(Long memberId, UserDetailsImpl userDetails) {
        // 로그인 여부 확인
        validator.loginCheck(userDetails);
        //관리자 접근 권한 확인
        validator.adminCheck(userDetails);

        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new NullPointerException("해당 팀원은 존재하지 않습니다.")
        );

        memberRepository.delete(member);

        return "삭제 완료";
    }

    //해당 주차의 모든 팀을 조회
    public Map<String, Object> getTeamList(UserDetailsImpl userDetails, String week) {
        // 로그인 여부 확인
        validator.loginCheck(userDetails);
        //관리자 접근 권한 확인
        validator.adminCheck(userDetails);

        //해당 주차의 모든 팀을 조회
        List<WeekTeam> weekTeamList = weekTeamRepository.findByWeek(week);

        //팀별 팀원 리스트
        Map<String, Object> weekMemberList = new HashMap<>();

        for (WeekTeam p : weekTeamList) {
            List<Member> findMember = memberRepository.findByWeekTeam(p);
            List<TeamDto.getUserList> userLists = new ArrayList<>();
            for (Member getResponse : findMember) {
                TeamDto.getUserList userList = TeamDto.getUserList.builder()
                        .teamId(getResponse.getWeekTeam().getWeekTeamId())
                        .userId(getResponse.getUser().getUserId())
                        .userName(getResponse.getUser().getUserName())
                        .userEmail(getResponse.getUser().getUserEmail())
                        .phoneNumber(getResponse.getUser().getPhoneNumber())
                        .kakaoId(getResponse.getUser().getKakaoId())
                        .createdAt(getResponse.getUser().getCreatedAt())
                        .memberId(getResponse.getMemberId())
                        .build();

                userLists.add(userList);
            }

            weekMemberList.put(p.getTeamName() + ":" + p.getWeekTeamId(), userLists);
        }
        return weekMemberList;
    }

    //주차 정보
    public ArrayList<String> getWeeks(UserDetailsImpl userDetails) {
        // 로그인 여부 확인
        validator.loginCheck(userDetails);
        //관리자 접근 권한 확인
        validator.adminCheck(userDetails);

        List<WeekTeam> findWeek = weekTeamRepository.findAll();
        List<String> weekList = new ArrayList<>();

        for (WeekTeam find : findWeek) {
            weekList.add(find.getWeek());
        }

        //중복 제거
        HashSet<String> responseDto = new HashSet<>();
        responseDto.addAll(weekList);

        //리스트로 변환(정렬)
        ArrayList<String> response = new ArrayList<>(responseDto);

        //정렬
        Collections.sort(response);

        return response;
    }

    public List<TeamDto.getNoMember> getNoMember(UserDetailsImpl userDetails, String week) {
        // 로그인 여부 확인
        validator.loginCheck(userDetails);
        //관리자 접근 권한 확인
        validator.adminCheck(userDetails);

        //해당 주차에 대한 팀 찾기
        List<WeekTeam> weekTeamList = weekTeamRepository.findByWeek(week);

        //모든 유저를 찾기
        List<User> noMemberList = userRepository.findAll();
        //값을 return 할 Dto 만들기
        List<TeamDto.getNoMember> noMembers = new ArrayList<>();

        for (WeekTeam weekTeam : weekTeamList) {
            List<Member> member = memberRepository.findByWeekTeam(weekTeam);
            for (Member find : member){
            User getUser = userRepository.findById(find.getUser().getUserId()).orElseThrow(
                    () -> new NullPointerException("해당 유저가 존재하지 않습니다.")
            );
            //제거 대상 제거
                noMemberList.remove(getUser);
            }
        }

        //return 값 가공하기
        for (User user : noMemberList) {
            TeamDto.getNoMember response = TeamDto.getNoMember.builder()
                    .userId(user.getUserId())
                    .userName(user.getUserName())
                    .build();

            noMembers.add(response);
        }

        return noMembers;
    }
}