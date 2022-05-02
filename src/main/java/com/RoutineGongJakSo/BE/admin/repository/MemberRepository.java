package com.RoutineGongJakSo.BE.admin.repository;

import com.RoutineGongJakSo.BE.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
