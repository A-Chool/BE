package com.RoutineGongJakSo.BE.checkIn.repository;

import com.RoutineGongJakSo.BE.model.Analysis;
import com.RoutineGongJakSo.BE.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnalysisRepository extends JpaRepository<Analysis, Long> {
   Optional<Analysis> findByUserAndDate(User user, String date);

   List<Analysis> findByUser(User user);
}
