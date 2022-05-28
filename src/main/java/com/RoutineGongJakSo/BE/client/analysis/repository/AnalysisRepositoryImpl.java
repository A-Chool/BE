package com.RoutineGongJakSo.BE.client.analysis.repository;

import com.RoutineGongJakSo.BE.client.analysis.model.Analysis;
import com.RoutineGongJakSo.BE.client.analysis.model.QAnalysis;
import com.RoutineGongJakSo.BE.client.user.QUser;
import com.RoutineGongJakSo.BE.client.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class AnalysisRepositoryImpl implements AnalysisRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public AnalysisRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Analysis> findByAnalysisWithUser(User user) {
        return queryFactory
                .selectFrom(QAnalysis.analysis)
                .join(QAnalysis.analysis.user, QUser.user)
                .fetchJoin()
                .where(QAnalysis.analysis.user.eq(user))
                .fetch();
    }
}
