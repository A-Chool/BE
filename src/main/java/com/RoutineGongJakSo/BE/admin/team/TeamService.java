package com.RoutineGongJakSo.BE.admin.team;

import com.RoutineGongJakSo.BE.admin.member.Member;
import com.RoutineGongJakSo.BE.admin.member.MemberDto;
import com.RoutineGongJakSo.BE.admin.member.MemberRepository;
import com.RoutineGongJakSo.BE.admin.week.Week;
import com.RoutineGongJakSo.BE.admin.week.WeekRepository;
import com.RoutineGongJakSo.BE.client.chat.model.ChatRoom;
import com.RoutineGongJakSo.BE.client.chat.repo.ChatRoomRepository;
import com.RoutineGongJakSo.BE.client.user.User;
import com.RoutineGongJakSo.BE.client.user.UserDto;
import com.RoutineGongJakSo.BE.client.user.UserRepository;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.exception.UserException;
import com.RoutineGongJakSo.BE.security.exception.UserExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static com.RoutineGongJakSo.BE.admin.team.TeamValidator.checkTeamDuple;
import static com.RoutineGongJakSo.BE.admin.week.WeekValidator.checkWeekPresent;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final WeekTeamRepository weekTeamRepository;
    private final UserRepository userRepository;
    private final WeekRepository weekRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final ChatRoomRepository chatRoomRepository;

    // 팀 추가
    @Transactional
    public TeamDto.CreateResponseDto createTeam(Long weekId, TeamDto.CreateTeamDto teamDto) {
        Optional<Week> weekFound = weekRepository.findById(weekId);
        checkWeekPresent(weekFound);
        Week week = weekFound.get();

        String teamName = teamDto.getTeamName();
        Optional<Team> teamFound = teamRepository.findByTeamName(teamName);
        checkTeamDuple(teamFound);

        String groundRule = "";
        String workSpace = "";

        String roomName = week.getWeekName()+ " " + teamDto.getTeamName();
        ChatRoom chatRoom = chatRoomRepository.createChatRoom(roomName);

        Team team = Team.builder()
                .teamName(teamName)
                .week(week)
                .groundRule(groundRule)
                .workSpace(workSpace)
                .roomId(chatRoom.getRoomId())
                .roomName(roomName)
                .build();

        teamRepository.save(team);

        return new TeamDto.CreateResponseDto(team);
    }

    // 팀원 추가
    @Transactional
    public String addMembers(UserDetailsImpl userDetails, TeamDto.AddTeamDto addTeamDto) {
        // 로그인 여부 확인
//        validator.loginCheck(userDetails);
//        //관리자 접근 권한 확인
//        validator.adminCheck(userDetails);

        WeekTeam weekTeam = weekTeamRepository.findById(addTeamDto.getTeamId()).orElseThrow(
                () -> new NullPointerException("해당 팀이 존재하지 않습니다.")
        );

        //유저아이디로 유저정보 찾기
//        User user = validator.findUserIdInfo(addTeamDto.getUserId());

        User user = userDetails.getUser();


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

        user.addMember(member);
        weekTeam.addMember(member);

        memberRepository.save(member);

        return "팀원 추가 완료!";
    }

    //팀 삭제
    @Transactional
    public String deleteTeam(Long teamId, UserDetailsImpl userDetails) {
        // 로그인 여부 확인
//        validator.loginCheck(userDetails);
//        //관리자 접근 권한 확인
//        validator.adminCheck(userDetails);

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
//        validator.loginCheck(userDetails);
//        //관리자 접근 권한 확인
//        validator.adminCheck(userDetails);

        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new NullPointerException("해당 팀원은 존재하지 않습니다.")
        );

        memberRepository.delete(member);

        return "삭제 완료";
    }

    //해당 주차의 모든 팀을 조회
    public List<TeamDto.WeekTeamDto> getTeamList(UserDetailsImpl userDetails, String week) {
        // 로그인 여부 확인
//        validator.loginCheck(userDetails);
//        //관리자 접근 권한 확인
//        validator.adminCheck(userDetails);

        //해당 주차의 모든 팀을 조회
        List<WeekTeam> weekTeamList = weekTeamRepository.findByWeek(week);
        List<TeamDto.WeekTeamDto> weekTeamDtoList = new ArrayList<>();

        for (WeekTeam weekTeam : weekTeamList) {
            List<MemberDto> memberDtoList = new ArrayList<>();

            for (Member member : weekTeam.getMemberList()) { //ToDo getMemberList에 어떤 값이 들어있는지 확인은 어디서 할 수 있을까요?

                MemberDto memberDto = new MemberDto();
                memberDto.setMemberId(member.getMemberId());
                memberDto.setUser(new UserDto(member.getUser()));
                memberDtoList.add(memberDto);
            }

            TeamDto.WeekTeamDto weekTeamDto = TeamDto.WeekTeamDto.builder()
                    .teamId(weekTeam.getWeekTeamId())
                    .teamName(weekTeam.getTeamName())
                    .week(weekTeam.getWeek())
                    .memberList(memberDtoList)
                    .build();
            weekTeamDtoList.add(weekTeamDto);
        }
        return weekTeamDtoList;
    }

    //주차 정보
    public ArrayList<String> getWeeks(UserDetailsImpl userDetails) {
        // 로그인 여부 확인
//        validator.loginCheck(userDetails);
//        //관리자 접근 권한 확인
//        validator.adminCheck(userDetails);

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

    //해당 주차에 멤버아이디가 없는 유저 리스트
    public List<TeamDto.GetNoMember> getNoMember(UserDetailsImpl userDetails, String week) {
//        // 로그인 여부 확인
//        validator.loginCheck(userDetails);
//        //관리자 접근 권한 확인
//        validator.adminCheck(userDetails);

        //해당 주차에 대한 팀 찾기
        List<WeekTeam> weekTeamList = weekTeamRepository.findByWeek(week);

        //모든 유저를 찾기
        List<User> noMemberList = userRepository.findAll();
        //값을 return 할 CheckInListDto 만들기
        List<TeamDto.GetNoMember> noMembers = new ArrayList<>();

        for (WeekTeam weekTeam : weekTeamList) {
            List<Member> member = memberRepository.findByWeekTeam(weekTeam);
            for (Member find : member) {
                User getUser = userRepository.findById(find.getUser().getUserId()).orElseThrow(
                        () -> new UserException(UserExceptionType.NOT_FOUND_MEMBER)
                );
                //제거 대상 제거
                noMemberList.remove(getUser);
            }
        }

        //return 값 가공하기
        for (User user : noMemberList) {
            TeamDto.GetNoMember response = TeamDto.GetNoMember.builder()
                    .userId(user.getUserId())
                    .userName(user.getUserName())
                    .build();

            noMembers.add(response);
        }

        return noMembers;
    }
}
