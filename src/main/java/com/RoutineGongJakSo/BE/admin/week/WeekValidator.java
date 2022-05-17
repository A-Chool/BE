package com.RoutineGongJakSo.BE.admin.week;

import com.RoutineGongJakSo.BE.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.RoutineGongJakSo.BE.exception.ErrorCode.ALREADY_EXIST_WEEK_NAME;
import static com.RoutineGongJakSo.BE.exception.ErrorCode.BLANK_WEEK_NAME;

@Component
@RequiredArgsConstructor
public class WeekValidator {
    public static void checkWeek(Optional<Week> found){
        if (found.isPresent()) {
            throw new CustomException(ALREADY_EXIST_WEEK_NAME);
        }
    }

    public static void checkName(String weekName){
        if(weekName.trim().length() == 0){
            throw new CustomException(BLANK_WEEK_NAME);
        }
    }
}
