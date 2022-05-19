package com.RoutineGongJakSo.BE.security;

import com.RoutineGongJakSo.BE.security.jwt.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// todo: FormLogin 이 성공했을 때 호출된다.
@Slf4j
public class FormLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    public static final String AUTH_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "RefreshAuthorization";
    public static final String TOKEN_TYPE = "BEARER";

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) {
        final UserDetailsImpl userDetails = ((UserDetailsImpl) authentication.getPrincipal());
        // Token 생성
        final String token = JwtTokenUtils.generateJwtToken(userDetails);
        final String refreshToken = JwtTokenUtils.generateRefreshToken(userDetails.getUserEmail());
        log.info("refreshToken : " + refreshToken);
        log.info("accessToken : " + token);
        // todo: generateJwtToken - JWT 토큰이 만들어진다.
        response.addHeader(AUTH_HEADER, TOKEN_TYPE + " " + token);
        response.addHeader(REFRESH_HEADER,TOKEN_TYPE + " " + refreshToken);
    }
}
