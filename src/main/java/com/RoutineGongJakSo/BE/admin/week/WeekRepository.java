package com.RoutineGongJakSo.BE.admin.week;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeekRepository extends JpaRepository<Week, Long> {
    Optional<Week> findByWeekName(String weekName);
}
