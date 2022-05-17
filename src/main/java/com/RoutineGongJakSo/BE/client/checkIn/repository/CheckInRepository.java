package com.RoutineGongJakSo.BE.client.checkIn.repository;

import com.RoutineGongJakSo.BE.client.checkIn.model.CheckIn;
import com.RoutineGongJakSo.BE.client.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    List<CheckIn> findByUserAndDate(User user, String date);

    List<CheckIn> findByDate(String date);

//exists
}
