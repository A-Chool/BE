package com.RoutineGongJakSo.BE.admin.week;

import com.RoutineGongJakSo.BE.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.RoutineGongJakSo.BE.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class WeekValidator {
    public static void checkWeekDuple(Optional<Week> found) {
        if (found.isPresent()) {
            throw new CustomException(ALREADY_EXIST_WEEK_NAME);
        }
    }

    public static void checkNameBlank(String weekName) {
        if (weekName.trim().length() == 0) {
            throw new CustomException(BLANK_WEEK_NAME);
        }
    }

    public static void checkNameLength(String weekName) {
        if (weekName.length() > 6) {
            throw new CustomException(TOO_LONG_WEEK_NAME);
        }
    }

    public static void checkWeekPresent(Optional<Week> found) {
        if (!found.isPresent()) {
            throw new CustomException(NOT_FOUND_WEEK_ID);
        }
    }

    public static void checkWeekDisplay(Week week) {
        if (week.getDisplay()) {
            throw new CustomException(DISPLAY_WEEK_ID);
        }
    }
}
