package com.RoutineGongJakSo.BE.toDo;

import com.RoutineGongJakSo.BE.model.WeekTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {

    List<ToDo> findByWeekTeam(WeekTeam weekTeam);
}
