package com.RoutineGongJakSo.BE.client.refreshToken;

import com.RoutineGongJakSo.BE.client.user.User;
import com.RoutineGongJakSo.BE.client.user.UserRepository;
import com.RoutineGongJakSo.BE.exception.CustomException;
import com.RoutineGongJakSo.BE.security.jwt.JwtDecoder;
import com.RoutineGongJakSo.BE.security.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.RoutineGongJakSo.BE.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JwtDecoder jwtDecoder;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public HttpHeaders regenerateToken(String refreshToken) {
        String token = refreshToken.substring(7); //Bearer 없애고,
        jwtDecoder.decodeRefresh(token); //token 유효성 검사

        RefreshToken findRefresh = refreshTokenRepository.findByRefreshToken(token);

        User user = userRepository.findByUserEmail(findRefresh.getUserEmail()).orElseThrow(
                () -> new CustomException(NOT_FOUND_USER_ID)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "BEARER " + JwtTokenUtils.generateReJwtToken(user.getUserName(), user.getUserEmail(), user.getUserLevel()));
        headers.add("RefreshAuthorization", "BEARER " + JwtTokenUtils.generateRefreshToken());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        findRefresh.setRefreshToken(JwtTokenUtils.generateRefreshToken());

        return headers;
    }
}
