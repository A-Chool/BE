package com.RoutineGongJakSo.BE.client.toDo;

import com.RoutineGongJakSo.BE.admin.team.Team;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {

    private final ToDoRepository toDoRepository;
    private final Validator validator;
    private final ToDoValidator toDoValidator;

    @Transactional
    public void createToDo(UserDetailsImpl userDetails, ToDoDto.RequestToDoDto requestToDoDto) {
        validator.loginCheck(userDetails);

//        WeekTeam weekTeam = toDoValidator.findWeekTeam(requestToDoDto.getTeamId()); //Id로 주차팀 찾기
        Team team = toDoValidator.findTeam(requestToDoDto.getTeamId());
        ToDo toDo = ToDo.builder()
                .todoContent(requestToDoDto.getTodoContent())
//                .weekTeam(weekTeam)
                .team(team)
                .build();

//        weekTeam.addToDo(toDo);
        team.addToDo(toDo);

        log.info("추가된 할 일 {}", toDo);
        toDoRepository.save(toDo);
    }

    @Transactional
    public void deleteToDo(UserDetailsImpl userDetails, Long todoId) {
        validator.loginCheck(userDetails);

        ToDo todo = toDoValidator.findToDo(todoId); //Id 값으로 할 일 찾기

        log.info("삭제된 할 일 {}", todo);

        toDoRepository.delete(todo);
    }

    @Transactional
    public ToDoDto.ResponseDto updateToDo(UserDetailsImpl userDetails, ToDoDto.UpDateToDoDto dto, Long todoId) {
        validator.loginCheck(userDetails);

        ToDo todo = toDoValidator.findToDo(todoId); //Id 값으로 할 일 찾기

        todo.setTodoContent(dto.getTodoContent());

        ToDoDto.ResponseDto responseDto = ToDoDto.ResponseDto.builder()
                .todoId(todo.getToDoId())
                .todoContent(todo.getTodoContent())
                .todoCheck(todo.isTodoCheck())
                .build();

        log.info("수정된 할 일 {}", responseDto);
        return responseDto;
    }

    @Transactional
    public ToDoDto.ResponseDto updateCheck(UserDetailsImpl userDetails, Long todoId) {
        validator.loginCheck(userDetails);

        ToDo todo = toDoValidator.findToDo(todoId); //Id 값으로 할 일 찾기

        if (todo.isTodoCheck() == false) {
            todo.setTodoCheck(true);
        } else {
            todo.setTodoCheck(false);
        }

        ToDoDto.ResponseDto responseDto = ToDoDto.ResponseDto.builder()
                .todoId(todo.getToDoId())
                .todoContent(todo.getTodoContent())
                .todoCheck(todo.isTodoCheck())
                .build();

        log.info("완료 여부 {}", responseDto);
        return responseDto;
    }
}
