package com.RoutineGongJakSo.BE.admin.repository;

import com.RoutineGongJakSo.BE.model.Member;
import com.RoutineGongJakSo.BE.model.WeekTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByWeekTeam(WeekTeam weekTeam);

    List<Member> findByWeekTeamOrderByWeekTeam(WeekTeam weekTeam);


}
