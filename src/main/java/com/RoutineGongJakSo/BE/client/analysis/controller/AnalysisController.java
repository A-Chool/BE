package com.RoutineGongJakSo.BE.client.analysis.controller;

import com.RoutineGongJakSo.BE.client.analysis.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AnalysisController {
    private final AnalysisService analysisService;
}
