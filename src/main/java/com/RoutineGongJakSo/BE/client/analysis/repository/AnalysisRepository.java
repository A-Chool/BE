package com.RoutineGongJakSo.BE.client.analysis.repository;

import com.RoutineGongJakSo.BE.client.analysis.model.Analysis;
import com.RoutineGongJakSo.BE.client.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnalysisRepository extends JpaRepository<Analysis, Long>, AnalysisRepositoryCustom {
   Optional<Analysis> findByUserAndDate(User user, String date);
   List<Analysis> findByUser(User user);
}
