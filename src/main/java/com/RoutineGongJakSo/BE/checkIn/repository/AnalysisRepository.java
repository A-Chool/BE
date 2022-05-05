package com.RoutineGongJakSo.BE.checkIn.repository;

import com.RoutineGongJakSo.BE.model.Analysis;
import com.RoutineGongJakSo.BE.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalysisRepository extends JpaRepository<Analysis, Long> {
    List<Analysis> findByUserAndDate(User user, String date);
}
