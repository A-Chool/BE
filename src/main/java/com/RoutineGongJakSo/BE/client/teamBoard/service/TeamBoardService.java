package com.RoutineGongJakSo.BE.client.teamBoard.service;

import com.RoutineGongJakSo.BE.admin.member.Member;
import com.RoutineGongJakSo.BE.admin.member.MemberDto;
import com.RoutineGongJakSo.BE.admin.member.MemberRepository;
import com.RoutineGongJakSo.BE.admin.team.Team;
import com.RoutineGongJakSo.BE.admin.team.WeekTeam;
import com.RoutineGongJakSo.BE.admin.team.WeekTeamRepository;
import com.RoutineGongJakSo.BE.client.teamBoard.dto.*;
import com.RoutineGongJakSo.BE.client.toDo.ToDo;
import com.RoutineGongJakSo.BE.client.toDo.ToDoDto;
import com.RoutineGongJakSo.BE.client.toDo.ToDoValidator;
import com.RoutineGongJakSo.BE.client.user.User;
import com.RoutineGongJakSo.BE.client.user.UserDto;
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
    private final WeekTeamRepository weekTeamRepository;
    private final MemberRepository memberRepository;
    private final ToDoValidator toDoValidator;


    public TeamBoardDto getAllTeamBoard(UserDetailsImpl userDetails) {

        User user = validator.userInfo(userDetails);// 유저 정보를 찾음(로그인 하지 않았다면 에러 뜰 것)

        List<Member> userMemberList = user.getMemberList();

        List<WeekTeam> weekTeamList = new ArrayList<>();
        List<WeekTeamDto> weekTeamDtoList = new ArrayList<>();

        List<Team> teamList = new ArrayList<>();
        for (Member member : userMemberList) {
            WeekTeam weekTeam = member.getWeekTeam();
            Team team = member.getTeam();
            weekTeamList.add(weekTeam);
            teamList.add(team);
            weekTeamDtoList.add(new WeekTeamDto(weekTeam));
        }

//        WeekTeam lastTeam = weekTeamList.get(weekTeamList.size() - 1);
        //Todo lastTeam display true 인걸로 수정해야함

        Team lastTeam = teamList.get(0);
        List<ToDo> toDoList = toDoValidator.toDoList(lastTeam);

        List<ToDoDto.OriginToDoDto> originToDoDtoList = new ArrayList<>();

        for (ToDo todo : toDoList) {
            ToDoDto.OriginToDoDto originToDoDto = ToDoDto.OriginToDoDto.builder()
                    .todoId(todo.getToDoId())
                    .todoContent(todo.getTodoContent())
                    .todoCheck(todo.isTodoCheck())
                    .build();
            originToDoDtoList.add(originToDoDto);
        }

        List<Member> teamMemberList = lastTeam.getMemberList();
        List<MemberDto.ResponseDto> teamResponseDtoList = new ArrayList<>();
        for (Member member : teamMemberList) {
            MemberDto.ResponseDto responseDto = new MemberDto.ResponseDto();
            responseDto.setMemberId(member.getMemberId());
            responseDto.setUser(new UserDto(member.getUser()));
            teamResponseDtoList.add(responseDto);
        }
//        TeamBoardDto teamBoardDto = new TeamBoardDto(lastTeam);
        TeamBoardDto teamBoardDto = new TeamBoardDto();

        teamBoardDto.setWeekTeamList(weekTeamDtoList);
        teamBoardDto.setMemberList(teamResponseDtoList);
        teamBoardDto.setToDoList(originToDoDtoList);

        System.out.println("teamBoardDto = " + teamBoardDto);

        return teamBoardDto;
    }

    public void updateGroundRule(UserDetailsImpl userDetails, Long weekTeamId, GroundRuleDto groundRule) {
        validator.userInfo(userDetails);// 유저 정보를 찾음(로그인 하지 않았다면 에러 뜰 것)

        User user = userDetails.getUser();

        WeekTeam weekTeam = weekTeamRepository.findById(weekTeamId).orElseThrow(() -> new IllegalArgumentException("팀없다."));

        memberRepository.findByUserAndWeekTeam(user, weekTeam).orElseThrow(() -> new IllegalArgumentException("이 팀의 멤버가 아닙니다."));

        weekTeam.setGroundRule(groundRule.getGroundRule());

        weekTeamRepository.save(weekTeam);

    }

    public void updateWorkSpace(UserDetailsImpl userDetails, Long weekTeamId, WorkSpaceDto workSpace) {
        validator.userInfo(userDetails);// 유저 정보를 찾음(로그인 하지 않았다면 에러 뜰 것)

        User user = userDetails.getUser();

        WeekTeam weekTeam = weekTeamRepository.findById(weekTeamId)
                .orElseThrow(() -> new IllegalArgumentException("팀없다."));

        memberRepository.findByUserAndWeekTeam(user, weekTeam)
                .orElseThrow(() -> new IllegalArgumentException("이 팀의 멤버가 아닙니다."));

        weekTeam.setWorkSpace(workSpace.getWorkSpace());

        weekTeamRepository.save(weekTeam);
    }

    public TeamBoardDto getTeamBoard(UserDetailsImpl userDetails, Long teamId) {

        User user = validator.userInfo(userDetails);// 유저 정보를 찾음(로그인 하지 않았다면 에러 뜰 것)

//        WeekTeam findWeekTeam = toDoValidator.findWeekTeam(weekTeamId); //Id로 주차팀 찾기

        Team findTeam = toDoValidator.findTeam(teamId);

        List<ToDo> toDoList = toDoValidator.toDoList(findTeam);

        List<ToDoDto.OriginToDoDto> originToDoDtoList = new ArrayList<>();

        for (ToDo todo : toDoList) {
            ToDoDto.OriginToDoDto originToDoDto = ToDoDto.OriginToDoDto.builder()
                    .todoId(todo.getToDoId())
                    .todoContent(todo.getTodoContent())
                    .todoCheck(todo.isTodoCheck())
                    .build();
            originToDoDtoList.add(originToDoDto);
        }

        List<Member> userMemberList = user.getMemberList();

        List<WeekTeam> weekTeamList = new ArrayList<>();
        List<WeekTeamDto> weekTeamDtoList = new ArrayList<>();

        WeekTeam targetTeam = new WeekTeam();

        for (Member member : userMemberList) {
            WeekTeam weekTeam = member.getWeekTeam();
            weekTeamList.add(weekTeam);
            weekTeamDtoList.add(new WeekTeamDto(weekTeam));
            if (teamId.equals(weekTeam.getWeekTeamId())) {
                targetTeam = weekTeam;
            }
        }
        List<Member> teamMemberList = targetTeam.getMemberList();
        List<MemberDto.ResponseDto> teamResponseDtoList = new ArrayList<>();
        for (Member member : teamMemberList) {
            MemberDto.ResponseDto responseDto = new MemberDto.ResponseDto();
            responseDto.setMemberId(member.getMemberId());
            responseDto.setUser(new UserDto(member.getUser()));
            teamResponseDtoList.add(responseDto);
        }
        TeamBoardDto teamBoardDto = new TeamBoardDto(targetTeam);

        teamBoardDto.setWeekTeamList(weekTeamDtoList);
        teamBoardDto.setMemberList(teamResponseDtoList);
        teamBoardDto.setToDoList(originToDoDtoList);

        System.out.println("teamBoardDto = " + teamBoardDto);

        return teamBoardDto;

    }
}
