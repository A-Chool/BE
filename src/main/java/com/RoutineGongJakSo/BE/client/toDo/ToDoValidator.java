package com.RoutineGongJakSo.BE.client.toDo;

import com.RoutineGongJakSo.BE.admin.team.WeekTeam;
import com.RoutineGongJakSo.BE.admin.team.WeekTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ToDoValidator {

    private final ToDoRepository toDoRepository;
    private final WeekTeamRepository weekTeamRepository;

    //Id 값으로 할 일 찾기
    public ToDo findToDo(Long todoId) {
        ToDo todo = toDoRepository.findById(todoId).orElseThrow(
                () -> new NullPointerException("할 일이 존재하지 않습니다.")
        );
        return todo;
    }

    //Id로 주차팀 찾기
    public WeekTeam findWeekTeam(Long weekTeamId) {
        WeekTeam weekTeam = weekTeamRepository.findById(weekTeamId).orElseThrow(
                () -> new NullPointerException("해당 팀이 존재하지 않습니다."));
        return weekTeam;
    }

    public List<ToDo> toDoList(WeekTeam weekTeam) {
        List<ToDo> toDoList = toDoRepository.findByWeekTeam(weekTeam);
        return toDoList;
    }
}
