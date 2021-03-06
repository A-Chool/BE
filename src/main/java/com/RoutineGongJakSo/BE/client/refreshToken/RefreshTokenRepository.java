package com.RoutineGongJakSo.BE.client.refreshToken;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByUserEmail(String userEmail);

    RefreshToken findByRefreshToken(String refreshToken);


}
