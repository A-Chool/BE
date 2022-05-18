package com.RoutineGongJakSo.BE.admin.member;

import com.RoutineGongJakSo.BE.admin.team.Team;
import com.RoutineGongJakSo.BE.admin.team.WeekTeam;
import com.RoutineGongJakSo.BE.client.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByWeekTeam(WeekTeam weekTeam);

    List<Member> findAllByUser(User user);

    Optional<Long> findByUserAndWeekTeam(User user, WeekTeam weekTeam);

    Optional<Long> findByUserAndTeam(User user, Team team);
}
