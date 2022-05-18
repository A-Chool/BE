package com.RoutineGongJakSo.BE.admin.team;

import com.RoutineGongJakSo.BE.client.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByTeamName(String teamName);

    @Query("select a.team from Member a where a.user = :user and a.team.week.display = true")
    Team findByUserAndDisplayWeek(User user);
}
