package com.RoutineGongJakSo.BE.admin.team;

import com.RoutineGongJakSo.BE.admin.week.Week;
import com.RoutineGongJakSo.BE.client.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByTeamNameAndWeek(String teamName, Week week);

    @Query("select a.team from Member a where a.user = :user and a.team.week.display = true")
    Team findByUserAndDisplayWeek(User user);

    List<Team> findByWeek(Week week);

    @Query("select t from Team t where t.week.weekId = :weekId")
    List<Team> findByWeekId(Long weekId);
}
