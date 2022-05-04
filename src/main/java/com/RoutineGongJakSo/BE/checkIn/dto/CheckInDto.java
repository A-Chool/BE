package com.RoutineGongJakSo.BE.checkIn.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CheckInDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class requestDto {
        private String totalTime;
    }
}
