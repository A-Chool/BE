package com.RoutineGongJakSo.BE.admin.team;

import com.RoutineGongJakSo.BE.admin.week.Week;
import com.RoutineGongJakSo.BE.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.RoutineGongJakSo.BE.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class TeamValidator {
    public static void checkTeamDuple(Optional<Team> found){
        if (found.isPresent()) {
            throw new CustomException(ALREADY_EXIST_TEAM_NAME);
        }
    }
}
