package com.RoutineGongJakSo.BE.checkIn.repository;

import com.RoutineGongJakSo.BE.model.CheckIn;
import com.RoutineGongJakSo.BE.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    List<CheckIn> findByUser(User user);

    List<CheckIn> findByUserAndDate(User user, String date);
}
