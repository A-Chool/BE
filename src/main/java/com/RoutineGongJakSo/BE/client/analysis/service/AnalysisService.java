package com.RoutineGongJakSo.BE.client.analysis.service;

import com.RoutineGongJakSo.BE.client.analysis.dto.AnalysisDto;
import com.RoutineGongJakSo.BE.client.analysis.model.Analysis;
import com.RoutineGongJakSo.BE.client.analysis.repository.AnalysisRepository;
import com.RoutineGongJakSo.BE.client.checkIn.CheckInValidator;
import com.RoutineGongJakSo.BE.client.checkIn.model.CheckIn;
import com.RoutineGongJakSo.BE.client.checkIn.repository.CheckInRepository;
import com.RoutineGongJakSo.BE.client.user.User;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisService {
    private final Validator validator;
    private final AnalysisRepository analysisRepository;
    private final CheckInRepository checkInRepository;
    private final CheckInValidator checkInValidator;
    public AnalysisDto.TopResponseDto topAnalysis(UserDetailsImpl userDetails) throws ParseException {
        User user = validator.userInfo(userDetails);

        String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString(); // 현재 서울 날짜

        Calendar setDay = checkInValidator.todayCalender(date); //오늘 기준 캘린더
        checkInValidator.setCalendarTime(setDay); // yyyy-MM-dd 05:00:00(당일 오전 5시) 캘린더에 적용

        Calendar today = checkInValidator.todayCalender(date); //현재 시간 기준 날짜
        checkInValidator.todayCalendarTime(today); // String yyyy-MM-dd HH:mm:ss 현재시간

        Calendar startDay = checkInValidator.todayCalender(user.getCreatedAt()); //가입일

        if (today.compareTo(setDay) < 0) {
            today.add(Calendar.DATE, -1); //오전 5시보다 과거라면, 현재 날짜에서 -1
        }

        String setToday = checkInValidator.DateFormat(today); //Date -> String 변환

        Long start = startDay.getTimeInMillis() / (24 * 60 * 60 * 1000);
        Long standardDate = today.getTimeInMillis() / (24 * 60 * 60 * 1000);

        Long startDate = standardDate - start; //공부를 시작한 일수

        List<Analysis> userAllAnalysis = analysisRepository.findByUser(user);
        String daySum = "0";
        String totalTime = "0";

        Analysis todayAnalysis = new Analysis();
        for (Analysis find : userAllAnalysis) {
            if (find.getDate().equals(setToday)){
                todayAnalysis = find;
            }
        }

        if (userAllAnalysis.size() >= 1) {
            daySum = todayAnalysis.getDaySum();
            totalTime = checkInValidator.totalTime(userAllAnalysis);
        }

        List<CheckIn> firstCheckIn = checkInRepository.findByUserAndDate(user, setToday);
        String todayCheckIn = firstCheckIn.get(0).getCheckIn();

        if (firstCheckIn.size() < 1){
            todayCheckIn = "00:00:00";
        }

        AnalysisDto.TopResponseDto responseDto = AnalysisDto.TopResponseDto.builder()
                .startDate(startDate)
                .totalTime(totalTime)
                .todayTime(daySum)
                .todayCheckIn(todayCheckIn)
                .build();
        log.info("상단 통계 {}", responseDto);
        return responseDto;
    }
}
