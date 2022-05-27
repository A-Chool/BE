package com.RoutineGongJakSo.BE.client.analysis.dto;

import lombok.Builder;
import lombok.Getter;

public class AnalysisDto {

    @Getter
    @Builder
    public static class TopResponseDto {
       private Long startDate;
       private String totalTime;
       private String todayTime;
       private String todayCheckIn;
    }

    @Getter
    @Builder
    public static class GandiResponseDto {
        private String day;
        private String value;
    }

}
