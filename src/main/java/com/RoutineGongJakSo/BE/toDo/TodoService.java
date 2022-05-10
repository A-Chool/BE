package com.RoutineGongJakSo.BE.toDo;

import com.RoutineGongJakSo.BE.admin.repository.WeekTeamRepository;
import com.RoutineGongJakSo.BE.admin.repository.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.admin.repository.security.validator.Validator;
import com.RoutineGongJakSo.BE.model.WeekTeam;
import com.RoutineGongJakSo.BE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final ToDoRepository toDoRepository;
    private final UserRepository userRepository;
    private final WeekTeamRepository weekTeamRepository;
    private final Validator validator;

    @Transactional
    public void createToDo(UserDetailsImpl userDetails, ToDoDto.RequestToDoDto requestToDoDto) {
        validator.loginCheck(userDetails);

        WeekTeam weekTeam = weekTeamRepository.findById(requestToDoDto.getTeamId()).orElseThrow(
                () -> new NullPointerException("해당 팀이 존재하지 않습니다.")
        );

       ToDo toDo = ToDo.builder()
               .todoContent(requestToDoDto.getTodoContent())
               .weekTeam(weekTeam)
               .build();

        toDoRepository.save(toDo);
    }

    @Transactional
    public void deleteToDo(UserDetailsImpl userDetails, Long todoId) {
        validator.loginCheck(userDetails);

        ToDo toDo = toDoRepository.findById(todoId).orElseThrow(
                () -> new NullPointerException("할 일이 존재하지 않습니다.")
        );
        toDoRepository.delete(toDo);
    }

    @Transactional
    public ToDoDto.ResponseDto updateToDo(UserDetailsImpl userDetails, ToDoDto.UpDateToDoDto dto, Long todoId) {
        validator.loginCheck(userDetails);

        ToDo todo = toDoRepository.findById(todoId).orElseThrow(
                () -> new NullPointerException("할 일이 존재하지 않습니다.")
        );

        todo.setTodoContent(dto.getTodoContent());

        ToDoDto.ResponseDto responseDto = ToDoDto.ResponseDto.builder()
                .todoId(todo.getToDoId())
                .todoContent(todo.getTodoContent())
                .todoCheck(todo.isTodoCheck())
                .build();
        return responseDto;
    }

    @Transactional
    public ToDoDto.ResponseDto updateCheck(UserDetailsImpl userDetails, Long todoId) {
        validator.loginCheck(userDetails);

        ToDo todo = toDoRepository.findById(todoId).orElseThrow(
                () -> new NullPointerException("할 일이 존재하지 않습니다.")
        );

        if (todo.isTodoCheck() == false){
            todo.setTodoCheck(true);
        }else {
            todo.setTodoCheck(false);
        }

        ToDoDto.ResponseDto responseDto = ToDoDto.ResponseDto.builder()
                .todoId(todo.getToDoId())
                .todoContent(todo.getTodoContent())
                .todoCheck(todo.isTodoCheck())
                .build();

        return responseDto;
    }
}
