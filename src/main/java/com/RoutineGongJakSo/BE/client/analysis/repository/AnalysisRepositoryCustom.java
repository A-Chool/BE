package com.RoutineGongJakSo.BE.client.analysis.repository;

import com.RoutineGongJakSo.BE.client.analysis.model.Analysis;
import com.RoutineGongJakSo.BE.client.user.User;

import java.util.List;

public interface AnalysisRepositoryCustom {

    List<Analysis> findByAnalysisWithUser(User user);
}
