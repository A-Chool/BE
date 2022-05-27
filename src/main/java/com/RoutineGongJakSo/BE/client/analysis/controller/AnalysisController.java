package com.RoutineGongJakSo.BE.client.analysis.controller;

import com.RoutineGongJakSo.BE.client.analysis.dto.AnalysisDto;
import com.RoutineGongJakSo.BE.client.analysis.service.AnalysisService;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@Slf4j
@RestController
@RequestMapping("/api/user/analysis")
@RequiredArgsConstructor
public class AnalysisController {
    private final AnalysisService analysisService;

    @GetMapping("/top")
    public AnalysisDto.TopResponseDto topAnalysis(@AuthenticationPrincipal UserDetailsImpl userDetails) throws ParseException {
        log.info("요청 메소드 [GET] /api/user/analysis/top");
        return analysisService.topAnalysis(userDetails);
    }
}
