package com.RoutineGongJakSo.BE.admin.team;

import com.RoutineGongJakSo.BE.admin.member.Member;
import com.RoutineGongJakSo.BE.admin.member.MemberDto;
import com.RoutineGongJakSo.BE.admin.member.MemberRepository;
import com.RoutineGongJakSo.BE.admin.week.Week;
import com.RoutineGongJakSo.BE.admin.week.WeekRepository;
import com.RoutineGongJakSo.BE.client.chat.model.ChatRoom;
import com.RoutineGongJakSo.BE.client.chat.repo.ChatRoomRepository;
import com.RoutineGongJakSo.BE.client.user.UserDto;
import com.RoutineGongJakSo.BE.client.user.UserRepository;
import com.RoutineGongJakSo.BE.exception.CustomException;
import com.RoutineGongJakSo.BE.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        String roomName = week.getWeekName() + " " + teamDto.getTeamName();
        // Todo createChatRoom redis 서버 테스트 해야함
//        ChatRoom chatRoom = chatRoomRepository.createChatRoom(roomName);
        ChatRoom chatRoom = ChatRoom.create(roomName);
        Team team = Team.builder()
                .teamName(teamName)
                .week(week)
                .groundRule(groundRule)
                .workSpace(workSpace)
                .roomId(chatRoom.getRoomId())
                .roomName(roomName)
                .build();

        week.addTeam(team);

        teamRepository.save(team);

        return new TeamDto.CreateResponseDto(team);
    }

    //해당 주차의 모든 팀을 조회
    public List<TeamDto.WeekTeamDto> getTeamList(Long weekId) {
        //해당 주차의 모든 팀을 조회
//        List<WeekTeam> weekTeamList = weekTeamRepository.findByWeek(week);
        Week week = weekRepository.findById(weekId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_WEEK_ID)
        );

        List<Team> teamList = week.getTeamList();

        List<TeamDto.WeekTeamDto> weekTeamDtoList = new ArrayList<>();
//        List<TeamDto.
        for (Team team : teamList) {
            List<MemberDto.ResponseDto> responseDtoList = new ArrayList<>();

            for (Member member : team.getMemberList()) { //ToDo getMemberList에 어떤 값이 들어있는지 확인은 어디서 할 수 있을까요?
                MemberDto.ResponseDto responseDto = new MemberDto.ResponseDto();
                responseDto.setMemberId(member.getMemberId());
                responseDto.setUser(new UserDto(member.getUser()));
                responseDtoList.add(responseDto);
            }

            TeamDto.WeekTeamDto weekTeamDto = TeamDto.WeekTeamDto.builder()
                    .teamId(team.getTeamId())
                    .teamName(team.getTeamName())
                    .weekId(week.getWeekId())
                    .weekName(week.getWeekName())
                    .memberList(responseDtoList)
                    .build();
            weekTeamDtoList.add(weekTeamDto);
        }
        return weekTeamDtoList;
    }

//
//    //팀 삭제
//    @Transactional
//    public String deleteTeam(Long teamId, UserDetailsImpl userDetails) {
//        // 로그인 여부 확인
////        validator.loginCheck(userDetails);
////        //관리자 접근 권한 확인
////        validator.adminCheck(userDetails);
//
//        WeekTeam weekTeam = weekTeamRepository.findById(teamId).orElseThrow(
//                () -> new NullPointerException("해당 팀이 존재하지 않습니다.")
//        );
//
//        weekTeamRepository.delete(weekTeam);
//
//        return "삭제 완료";
//    }
//
//    //팀원 삭제
//    @Transactional
//    public String deleteMember(Long memberId, UserDetailsImpl userDetails) {
//        // 로그인 여부 확인
////        validator.loginCheck(userDetails);
////        //관리자 접근 권한 확인
////        validator.adminCheck(userDetails);
//
//        Member member = memberRepository.findById(memberId).orElseThrow(
//                () -> new NullPointerException("해당 팀원은 존재하지 않습니다.")
//        );
//
//        memberRepository.delete(member);
//
//        return "삭제 완료";
//    }
//
//
//    //주차 정보
//    public ArrayList<String> getWeeks(UserDetailsImpl userDetails) {
//        // 로그인 여부 확인
////        validator.loginCheck(userDetails);
////        //관리자 접근 권한 확인
////        validator.adminCheck(userDetails);
//
//        List<WeekTeam> findWeek = weekTeamRepository.findAll();
//        List<String> weekList = new ArrayList<>();
//
//        for (WeekTeam find : findWeek) {
//            weekList.add(find.getWeek());
//        }
//
//        //중복 제거
//        HashSet<String> responseDto = new HashSet<>();
//        responseDto.addAll(weekList);
//
//        //리스트로 변환(정렬)
//        ArrayList<String> response = new ArrayList<>(responseDto);
//
//        //정렬
//        Collections.sort(response);
//
//        return response;
//    }
//
//    //해당 주차에 멤버아이디가 없는 유저 리스트
//    public List<TeamDto.GetNoMember> getNoMember(UserDetailsImpl userDetails, String week) {
////        // 로그인 여부 확인
////        validator.loginCheck(userDetails);
////        //관리자 접근 권한 확인
////        validator.adminCheck(userDetails);
//
//        //해당 주차에 대한 팀 찾기
//        List<WeekTeam> weekTeamList = weekTeamRepository.findByWeek(week);
//
//        //모든 유저를 찾기
//        List<User> noMemberList = userRepository.findAll();
//        //값을 return 할 CheckInListDto 만들기
//        List<TeamDto.GetNoMember> noMembers = new ArrayList<>();
//
//        for (WeekTeam weekTeam : weekTeamList) {
//            List<Member> member = memberRepository.findByWeekTeam(weekTeam);
//            for (Member find : member) {
//                User getUser = userRepository.findById(find.getUser().getUserId()).orElseThrow(
//                        () -> new UserException(UserExceptionType.NOT_FOUND_MEMBER)
//                );
//                //제거 대상 제거
//                noMemberList.remove(getUser);
//            }
//        }
//
//        //return 값 가공하기
//        for (User user : noMemberList) {
//            TeamDto.GetNoMember response = TeamDto.GetNoMember.builder()
//                    .userId(user.getUserId())
//                    .userName(user.getUserName())
//                    .build();
//
//            noMembers.add(response);
//        }
//
//        return noMembers;
//    }
}
