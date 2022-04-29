package com.RoutineGongJakSo.BE.repository;

import com.RoutineGongJakSo.BE.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String username);
    Optional<User> findByUserEmail(String userEmail);
    Optional<User> findByKakaoId(Long kakaoId);
    Optional<User> findByNaverId(String  naverId);

}
