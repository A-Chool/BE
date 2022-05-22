package com.RoutineGongJakSo.BE.client.toDo;

import com.RoutineGongJakSo.BE.admin.team.Team;
import com.RoutineGongJakSo.BE.admin.team.TeamRepository;
import com.RoutineGongJakSo.BE.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.RoutineGongJakSo.BE.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class ToDoValidator {

    private final ToDoRepository toDoRepository;
    private final TeamRepository teamRepository;

    //Id 값으로 할 일 찾기
    public ToDo findToDo(Long todoId) {
        ToDo toDo = toDoRepository.findById(todoId).orElseThrow(
                () -> new CustomException(NOT_FOUND_TODO)
        );
        return toDo;
    }

    //Id로 팀 찾기
    public Team findTeam(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new CustomException(NOT_FOUND_TEAM_ID));
        return team;
    }

    public List<ToDo> toDoList(Team team) {
        List<ToDo> toDoList = toDoRepository.findByTeam(team);
        return toDoList;
    }

}
