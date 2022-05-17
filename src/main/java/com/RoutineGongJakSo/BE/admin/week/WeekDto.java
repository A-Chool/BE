package com.RoutineGongJakSo.BE.admin.week;

import lombok.Getter;
import lombok.Setter;

public class WeekDto {

    @Getter
    @Setter
    public static class ResponseDto{
        private Long weekId;
        private String weekName;
        private boolean display;

        public ResponseDto(Week week) {
            this.weekId = week.getWeekId();
            this.weekName = week.getWeekName();
            this.display = week.getDisplay();
        }
    }

    @Getter
    @Setter
    public static class RequestDto {
        private String weekName;
    }
}
