package com.RoutineGongJakSo.BE.security.jwt;

import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtils {

    // JWT 토큰의 생명: 30분만 살고 죽는다. (단위: milliseconds)
    private static final int JWT_TOKEN_VALID_MILLI_SEC = 60 * 30 * 1000;
    public static final String CLAIM_EXPIRED_DATE = "EXPIRED_DATE";
    public static final String CLAIM_USER_NAME = "USER_NAME";
    public static final String CLAIM_USER_EMAIL = "USER_EMAIL";
    public static final String CLAIM_USER_LEVEL = "USER_LEVEL";
    public static final String JWT_SECRET = "jwt_secret_!@#$%";

    public static String generateJwtToken(UserDetailsImpl userDetails) {
        String token = null;
        try {
            token = JWT.create()
                    .withIssuer("Mr.A-Chool")
                    .withClaim(CLAIM_USER_NAME, userDetails.getUsername())
                    .withClaim(CLAIM_USER_EMAIL, userDetails.getUserEmail())
                    .withClaim(CLAIM_USER_LEVEL, userDetails.getUserLevel())
                    // 토큰 만료 일시 = 현재 시간 + 토큰 유효기간)
                    .withClaim(CLAIM_EXPIRED_DATE, new Date(System.currentTimeMillis() + JWT_TOKEN_VALID_MILLI_SEC))
                    .sign(generateAlgorithm());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return token;
    }

    private static Algorithm generateAlgorithm() {
        return Algorithm.HMAC256(JWT_SECRET);
    }

    //관리자용 로그인 토큰 생성
    public String generateAdminJwtToken(String userName, String userEmail, int userLevel) {
        String token = null;
        try {
            //어드민용 토큰 유효시간
            long adminTokenTime = 60 * 60 * 60 * 1000L;
            token = JWT.create()
                    .withIssuer("Mr.A-Chool")
                    .withClaim(CLAIM_USER_NAME, userName)
                    .withClaim(CLAIM_USER_EMAIL, userEmail)
                    .withClaim(CLAIM_USER_LEVEL, userLevel)
                    // 토큰 만료 일시 = 현재 시간 + 토큰 유효기간)
                    .withClaim(CLAIM_EXPIRED_DATE, new Date(System.currentTimeMillis() + adminTokenTime))
                    .sign(generateAlgorithm());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return token;
    }
}
