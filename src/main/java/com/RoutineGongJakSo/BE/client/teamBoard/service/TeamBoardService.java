package com.RoutineGongJakSo.BE.client.teamBoard.service;

import com.RoutineGongJakSo.BE.admin.member.Member;
import com.RoutineGongJakSo.BE.admin.member.MemberDto;
import com.RoutineGongJakSo.BE.admin.member.MemberRepository;
import com.RoutineGongJakSo.BE.admin.team.Team;
import com.RoutineGongJakSo.BE.admin.team.TeamRepository;
import com.RoutineGongJakSo.BE.admin.week.Week;
import com.RoutineGongJakSo.BE.admin.week.WeekRepository;
import com.RoutineGongJakSo.BE.client.teamBoard.dto.GroundRuleDto;
import com.RoutineGongJakSo.BE.client.teamBoard.dto.TeamBoardDto;
import com.RoutineGongJakSo.BE.client.teamBoard.dto.TeamDto;
import com.RoutineGongJakSo.BE.client.teamBoard.dto.WorkSpaceDto;
import com.RoutineGongJakSo.BE.client.toDo.ToDo;
import com.RoutineGongJakSo.BE.client.toDo.ToDoDto;
import com.RoutineGongJakSo.BE.client.toDo.ToDoValidator;
import com.RoutineGongJakSo.BE.client.user.User;
import com.RoutineGongJakSo.BE.client.user.UserDto;
import com.RoutineGongJakSo.BE.exception.CustomException;
import com.RoutineGongJakSo.BE.exception.ErrorCode;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamBoardService {

    private final Validator validator;
    private final MemberRepository memberRepository;
    private final ToDoValidator toDoValidator;
    private final TeamRepository teamRepository;
    private final WeekRepository weekRepository;


    public TeamBoardDto getAllTeamBoard(UserDetailsImpl userDetails) {

        User user = validator.userInfo(userDetails);// 유저 정보를 찾음(로그인 하지 않았다면 에러 뜰 것)

        List<Member> userMemberList = user.getMemberList();

//        List<WeekTeam> weekTeamList = new ArrayList<>();
//        List<Team> teamList = new ArrayList<>();
//        List<TeamDto> teamDtoList = new ArrayList<>();
//
//        for (Member member : userMemberList) {
//            WeekTeam weekTeam = member.getWeekTeam();
//            Team team = member.getTeam();
//            weekTeamList.add(weekTeam);
//            teamList.add(team);
//            teamDtoList.add(new TeamDto(weekTeam));
//        }
//
//        //Todo lastTeam display true 인걸로 수정해야함
//
//        Team lastTeam = teamList.get(0);
//        List<ToDo> toDoList = toDoValidator.toDoList(lastTeam);
//
//        List<ToDoDto.OriginToDoDto> originToDoDtoList = new ArrayList<>();
//
//        for (ToDo todo : toDoList) {
//            ToDoDto.OriginToDoDto originToDoDto = ToDoDto.OriginToDoDto.builder()
//                    .todoId(todo.getToDoId())
//                    .todoContent(todo.getTodoContent())
//                    .todoCheck(todo.isTodoCheck())
//                    .build();
//            originToDoDtoList.add(originToDoDto);
//        }
//
//        List<Member> teamMemberList = lastTeam.getMemberList();
//        List<MemberDto.ResponseDto> teamResponseDtoList = new ArrayList<>();
//        for (Member member : teamMemberList) {
//            MemberDto.ResponseDto responseDto = new MemberDto.ResponseDto();
//            responseDto.setMemberId(member.getMemberId());
//            responseDto.setUser(new UserDto(member.getUser()));
//            teamResponseDtoList.add(responseDto);
//        }
////        TeamBoardDto teamBoardDto = new TeamBoardDto(lastTeam);
        TeamBoardDto teamBoardDto = new TeamBoardDto();

//        teamBoardDto.setTeamDtoList(teamDtoList);
//        teamBoardDto.setMemberList(teamResponseDtoList);
//        teamBoardDto.setToDoList(originToDoDtoList);

        System.out.println("teamBoardDto = " + teamBoardDto);

        return teamBoardDto;
    }

    public void updateGroundRule(UserDetailsImpl userDetails, Long weekTeamId, GroundRuleDto groundRule) {
        validator.userInfo(userDetails);// 유저 정보를 찾음(로그인 하지 않았다면 에러 뜰 것)

        User user = userDetails.getUser();

        Team team = teamRepository.findById(weekTeamId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_TEAM_ID)
        );

        memberRepository.findByUserAndTeam(user, team).orElseThrow(
                () -> new IllegalArgumentException("이 팀의 멤버가 아닙니다.")
        );

        team.setGroundRule(groundRule.getGroundRule());

        teamRepository.save(team);

    }

    public void updateWorkSpace(UserDetailsImpl userDetails, Long weekTeamId, WorkSpaceDto workSpace) {
        validator.userInfo(userDetails);// 유저 정보를 찾음(로그인 하지 않았다면 에러 뜰 것)

        User user = userDetails.getUser();

        Team team = teamRepository.findById(weekTeamId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_TEAM_ID)
        );

        memberRepository.findByUserAndTeam(user, team).orElseThrow(
                () -> new IllegalArgumentException("이 팀의 멤버가 아닙니다.")
        );

        team.setWorkSpace(workSpace.getWorkSpace());

        teamRepository.save(team);
    }

    public TeamBoardDto getTeamBoard(UserDetailsImpl userDetails, Long teamId) {

        User user = validator.userInfo(userDetails);// 유저 정보를 찾음(로그인 하지 않았다면 에러 뜰 것)

        // user 가 member 로 있었던 기록 리스트
        List<Member> userMemberList = user.getMemberList();

        // 팀보드 team 드롭 다운에 들어갈 list
        List<TeamDto> teamDtoList = new ArrayList<>();
        for (Member member : userMemberList) {
            TeamDto _teamDto = new TeamDto(member.getTeam());
            teamDtoList.add(_teamDto);
        }

        // targetTeam 정하기
        Team targetTeam = null;
        if(teamId != null){
            targetTeam = teamRepository.findById(teamId).orElseThrow(
                    ()-> new CustomException(ErrorCode.NOT_FOUND_TEAM_ID)
            );
        } else {
            targetTeam = teamRepository.findByUserAndDisplayWeek(user);
        }

        // memberList dtoList로 변환
        List<Member> memberList = targetTeam.getMemberList();
        List<MemberDto.ResponseDto> memberResponseDtoList = new ArrayList<>();
        for(Member _member : memberList){
            MemberDto.ResponseDto responseDto = new MemberDto.ResponseDto();
            responseDto.setMemberId(_member.getMemberId());
            responseDto.setUser(new UserDto(_member.getUser()));
            memberResponseDtoList.add(responseDto);
        }

        List<ToDo> toDoList = targetTeam.getToDoList();
        List<ToDoDto.OriginToDoDto> toDoResponseDtoList = new ArrayList<>();
        for(ToDo _toDo : toDoList){
            ToDoDto.OriginToDoDto responseDto = ToDoDto.OriginToDoDto.builder()
                    .todoId(_toDo.getToDoId())
                    .todoContent(_toDo.getTodoContent())
                    .todoCheck(_toDo.isTodoCheck())
                    .build();
            toDoResponseDtoList.add(responseDto);
        }

        TeamBoardDto teamBoardDto = new TeamBoardDto(targetTeam);
        teamBoardDto.setTeamDtoList(teamDtoList);
        teamBoardDto.setMemberList(memberResponseDtoList);
        teamBoardDto.setToDoList(toDoResponseDtoList);

        return teamBoardDto;

    }
}
