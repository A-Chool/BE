package com.RoutineGongJakSo.BE.client.analysis.controller;

import com.RoutineGongJakSo.BE.client.analysis.dto.AnalysisDto;
import com.RoutineGongJakSo.BE.client.analysis.service.AnalysisService;
import com.RoutineGongJakSo.BE.client.analysis.service.CronRank;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/user/analysis")
@RequiredArgsConstructor
public class AnalysisController {
    private final AnalysisService analysisService;
    private final CronRank cronRank;

    @GetMapping("/top")
    public AnalysisDto.TopResponseDto topAnalysis(@AuthenticationPrincipal UserDetailsImpl userDetails) throws ParseException {
        log.info("요청 메소드 [GET] /api/user/analysis/top");
        return analysisService.topAnalysis(userDetails);
    }

    @GetMapping
    public List<AnalysisDto.GandiResponseDto> getGandi(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("요청 메소드 [GET] /api/user/analysis");
        return analysisService.getGandi(userDetails);
    }

    @GetMapping("/line")
    public Map<String, Object> getLineAnalysis(@AuthenticationPrincipal UserDetailsImpl userDetails) throws ParseException {
        log.info("요청 메소드 [GET] /api/user/analysis/line");
        return analysisService.getLineAnalysis(userDetails);
    }

    @GetMapping("/rank")
    public Object getRank(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("요청 메소드 [GET] /api/user/analysis/rank");
        return analysisService.getRank(userDetails);
    }

    @GetMapping("/update")
    public void updateRank() throws ParseException {
        log.info("요청 메소드 [GET] /api/user/analysis/update");
        cronRank.updateRank();
    }


    @GetMapping("/reset")
    public void resetRank(){
        log.info("요청 메소드 [GET] /api/user/analysis/reset");
        cronRank.resetRank();
    }
}
