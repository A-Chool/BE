package com.RoutineGongJakSo.BE.admin.team;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WeekTeamRepository extends JpaRepository<WeekTeam, Long> {
    Optional<WeekTeam> findByTeamNameAndWeek(String teamName, String week);

    List<WeekTeam> findByWeek(String week);

}
