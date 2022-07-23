package com.RoutineGongJakSo.BE.validator;

import com.RoutineGongJakSo.BE.client.refreshToken.RefreshToken;
import com.RoutineGongJakSo.BE.client.refreshToken.RefreshTokenRepository;

public class TokenValidator {
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findRefreshToken(String email) {
        return refreshTokenRepository.findByUserEmail(email);
    }
}
