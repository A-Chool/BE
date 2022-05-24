package com.RoutineGongJakSo.BE.client.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserEmail(String userEmail);

    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findByNaverId(String naverId);
}
