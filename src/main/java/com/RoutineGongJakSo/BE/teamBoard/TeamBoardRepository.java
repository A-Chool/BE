package com.RoutineGongJakSo.BE.teamBoard;

import com.RoutineGongJakSo.BE.model.WeekTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamBoardRepository extends JpaRepository<TeamBoard, Long> {

    List<WeekTeam> findByWeek(String week);
}
