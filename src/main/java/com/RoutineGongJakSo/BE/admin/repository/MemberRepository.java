package com.RoutineGongJakSo.BE.admin.repository;

import com.RoutineGongJakSo.BE.model.Member;
import com.RoutineGongJakSo.BE.model.User;
import com.RoutineGongJakSo.BE.model.WeekTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByWeekTeam(WeekTeam weekTeam);

    Optional<Member> findByUserAndWeekTeam(User user, WeekTeam weekTeam);


}
