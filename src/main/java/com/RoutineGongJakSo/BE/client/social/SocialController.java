package com.RoutineGongJakSo.BE.client.social;

import com.RoutineGongJakSo.BE.client.social.Dto.KakaoUserInfoDto;
import com.RoutineGongJakSo.BE.client.social.Service.KakaoService;
import com.RoutineGongJakSo.BE.client.social.Service.NaverService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;


@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class SocialController {
    private final KakaoService kakaoService;
    private final NaverService naverService;

    //카카오 로그인
    @GetMapping("/kakao/callback")
    public KakaoUserInfoDto kakaoLogin(@RequestParam String code, HttpServletResponse response
    ) throws JsonProcessingException {
        log.info("요청 메서드 [GET] /api/user/kakao/callback");
        return kakaoService.kakaoLogin(code, response);
    }

    //네이버 로그인
    @GetMapping("/naver/callback")
    public void naverLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        log.info("요청 메서드 [GET] /api/user/naver/callback");
        naverService.naverLogin(code, response);
    }
}