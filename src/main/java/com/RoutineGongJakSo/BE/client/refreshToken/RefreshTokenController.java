package com.RoutineGongJakSo.BE.client.refreshToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;

    // Token 재발급
    @GetMapping("/aip/refreshToken")
    public HttpHeaders regenerateToken(@RequestHeader("RefreshAuthorization") String refreshToken){
        log.info("요청 메서드 [GET] /aip/refreshToken");
        return refreshTokenService.regenerateToken(refreshToken);
    }
}
