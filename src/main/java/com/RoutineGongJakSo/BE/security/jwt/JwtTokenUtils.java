package com.RoutineGongJakSo.BE.security.jwt;

import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtils {

    // JWT 토큰의 생명: 30분만 살고 죽는다. (단위: milliseconds)
    private static final int JWT_TOKEN_VALID_MILLI_SEC = 60 * 60 * 60 * 1000; // 60 * 30 * 1000; //todo 배포할때 수정해야함
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
            log.info("일반유저 로그인 try 토큰 {}", token);
        } catch (Exception e) {
            log.info("일반유저 로그인 catch 토큰 {}", token);
            System.out.println(e.getMessage());
        }
        log.info("일반유저 로그인 마지막 return 토큰 {}", token);
        return token;
    }

    private static Algorithm generateAlgorithm() {
        return Algorithm.HMAC256(JWT_SECRET);
    }

    //관리자용 로그인 토큰 생성
    public static String generateAdminJwtToken(String userName, String userEmail, int userLevel) {
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
            log.info("유틸 어드민 토큰 try {} 부분", token);
        } catch (Exception e) {
            log.info("유틸 어드민 토큰 catch 부분 {}", e.getMessage());
            System.out.println(e.getMessage());
        }
        log.info("어드민 트라이 캐치 빠져나오고, 맨 마지막 return {}", token);
        return token;
    }

    // Refresh Token
    public static String generateRefreshToken(){
        String token = null;
        try{
            long refreshToken = 60 * 60 * 60 * 1000;
            token = JWT.create()
                    .withIssuer("Mr.A-Chool")
                    .withClaim(CLAIM_EXPIRED_DATE, new Date((System.currentTimeMillis() + refreshToken)))
                    .sign(generateAlgorithm());
            log.info("유틸 리프레쉬 토큰 try {} 부분", token);
        } catch (Exception e) {
            log.info("유틸 어드민 토큰 catch {} 부분", token);
            System.out.println(e.getMessage());
        }
        log.info("리프레쉬 트라이 캐치 빠져나오고, 맨 마지막 return {}", token);
        return token;
    }

    public static String generateReJwtToken(String userName, String userEmail, int userLevel) {
        String token = null;
        try {
            token = JWT.create()
                    .withIssuer("Mr.A-Chool")
                    .withClaim(CLAIM_USER_NAME, userName)
                    .withClaim(CLAIM_USER_EMAIL, userEmail)
                    .withClaim(CLAIM_USER_LEVEL, userLevel)
                    // 토큰 만료 일시 = 현재 시간 + 토큰 유효기간)
                    .withClaim(CLAIM_EXPIRED_DATE, new Date(System.currentTimeMillis() + JWT_TOKEN_VALID_MILLI_SEC))
                    .sign(generateAlgorithm());
            log.info("유틸 jwt 재발행 try {} 부분", token);
        } catch (Exception e) {
            log.info("유틸 jwt 재발행 catch {} 부분", token);
            System.out.println(e.getMessage());
        }
        log.info("jwt 재발행 트라이 캐치 빠져나오고, 맨 마지막 return {}", token);
        return token;
    }
}
