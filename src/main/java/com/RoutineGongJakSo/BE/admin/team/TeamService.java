package com.RoutineGongJakSo.BE.admin.team;

import com.RoutineGongJakSo.BE.admin.member.Member;
import com.RoutineGongJakSo.BE.admin.member.MemberDto;
import com.RoutineGongJakSo.BE.admin.week.Week;
import com.RoutineGongJakSo.BE.admin.week.WeekRepository;
import com.RoutineGongJakSo.BE.client.chat.model.ChatRoom;
import com.RoutineGongJakSo.BE.client.chat.repo.ChatRoomRepository;
import com.RoutineGongJakSo.BE.client.user.UserDto;
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
    private final WeekRepository weekRepository;
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
        Week week = weekRepository.findById(weekId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_WEEK_ID)
        );

        List<Team> teamList = week.getTeamList();

        List<TeamDto.WeekTeamDto> weekTeamDtoList = new ArrayList<>();
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


    //팀 삭제
    @Transactional
    public String deleteTeam(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_TEAM_ID)
        );

        teamRepository.delete(team);

        return "삭제 완료";
    }

}
