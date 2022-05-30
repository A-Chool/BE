package com.RoutineGongJakSo.BE.security.jwt;

import com.RoutineGongJakSo.BE.exception.CustomException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

import static com.RoutineGongJakSo.BE.exception.ErrorCode.*;
import static com.RoutineGongJakSo.BE.security.jwt.JwtTokenUtils.*;

@Component
public class JwtDecoder {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public String decodeUsername(String token) {
        DecodedJWT decodedJWT = isValidToken(token)
                .orElseThrow(() -> new CustomException(BAD_TOKEN));

        Date expiredDate = decodedJWT
                .getClaim(CLAIM_EXPIRED_DATE)
                .asDate();

        Date now = new Date();
        if (expiredDate.before(now)) {
            throw new CustomException(BAD_TOKEN);
        }

        return decodedJWT
                .getClaim(CLAIM_USER_EMAIL)
                .asString();
    }

    // refreshToken 유효성 검사
    public void decodeRefresh(String token) {
        DecodedJWT decodedJWT = isValidToken(token)
                .orElseThrow(() -> new CustomException(TRY_SUBSTRING));

        Date expiredDate = decodedJWT
                .getClaim(CLAIM_EXPIRED_DATE)
                .asDate();

        Date now = new Date();
        if (expiredDate.before(now)) {
            throw new CustomException(BAD_TOKEN);
        }
    }

    public String decodeNickName(String token) {
        DecodedJWT decodedJWT = isValidToken(token)
                .orElseThrow(() -> new CustomException(BAD_TOKEN));

        Date expiredDate = decodedJWT
                .getClaim(CLAIM_EXPIRED_DATE)
                .asDate();

        Date now = new Date();
        if (expiredDate.before(now)) {
            throw new CustomException(BAD_TOKEN);
        }

        return decodedJWT
                .getClaim(CLAIM_USER_NAME)
                .asString();
    }

    public Optional<DecodedJWT> isValidToken(String token) {
        DecodedJWT jwt = null;
        log.info("token : {}", token);
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            JWTVerifier verifier = JWT
                    .require(algorithm)
                    .build();

            jwt = verifier.verify(token);
            log.info("try jwt 부분 {}", jwt);
        } catch (CustomException e) {
//            log.error(e.getMessage());
            log.info("catch jwt 부분 {}", jwt);
            return Optional.ofNullable(jwt);
        }
        log.info("맨 마지막 return jwt 부분 {}", jwt);
        return Optional.ofNullable(jwt);
    }
}
