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

import javax.transaction.Transactional;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisService {
    private final Validator validator;
    private final AnalysisRepository analysisRepository;
    private final CheckInRepository checkInRepository;
    private final CheckInValidator checkInValidator;

    //상단 통계
    @Transactional
    public AnalysisDto.TopResponseDto topAnalysis(UserDetailsImpl userDetails) throws ParseException {
        User user = validator.userInfo(userDetails);

        String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString(); // 현재 서울 날짜
        System.out.println(date);

        Calendar setDay = checkInValidator.todayCalender(date); //오늘 기준 캘린더
        checkInValidator.setCalendarTime(setDay); // yyyy-MM-dd 05:00:00(당일 오전 5시) 캘린더에 적용

        Calendar today = checkInValidator.todayCalender(date); //현재 시간 기준 날짜
        checkInValidator.todayCalendarTime(today); // String yyyy-MM-dd HH:mm:ss 현재시간

        if (today.compareTo(setDay) < 0) {
            today.add(Calendar.DATE, -1); //오전 5시보다 과거라면, 현재 날짜에서 -1
        }

        String[] startArr = user.getCreatedAt().split("-");
        String[] standardArr = date.split("-");
        LocalDate startDate = LocalDate.of(Integer.parseInt(startArr[0]), Integer.parseInt(startArr[1]), Integer.parseInt(startArr[2]));
        LocalDate standardDate = LocalDate.of(Integer.parseInt(standardArr[0]), Integer.parseInt(standardArr[1]), Integer.parseInt(standardArr[2]));

        Long days = ChronoUnit.DAYS.between(startDate, standardDate); //공부 시작 일수

        String setToday = checkInValidator.DateFormat(today); //Date -> String 변환

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
                .startDate(days)
                .totalTime(totalTime)
                .todayTime(daySum)
                .todayCheckIn(todayCheckIn)
                .build();

        log.info("상단 통계 {}", responseDto);
        return responseDto;
    }

    // 잔디용 통계
    @Transactional
    public List<AnalysisDto.GandiResponseDto> getGandi(UserDetailsImpl userDetails) {
        User user = validator.userInfo(userDetails);
        List<Analysis> userAllAnalysis = analysisRepository.findByUser(user);
        List<AnalysisDto.GandiResponseDto> gandi = new ArrayList<>();

        for (Analysis find : userAllAnalysis) {
            String[] arrA = find.getDaySum().split(":");
            String value = arrA[0];

            AnalysisDto.GandiResponseDto response = AnalysisDto.GandiResponseDto.builder()
                    .day(find.getDate())
                    .value(value)
                    .build();
            gandi.add(response);
        }
        log.info("잔디 통계 {}", gandi);
        return gandi;
    }

    @Transactional
    public Map<String, Object> getLineAnalysis(UserDetailsImpl userDetails) throws ParseException {

        User user = validator.userInfo(userDetails);

        String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString(); // 현재 서울 날짜

        Calendar today = checkInValidator.todayCalender(date); //현재 시간 기준 날짜
        checkInValidator.todayCalendarTime(today); // String yyyy-MM-dd HH:mm:ss 현재시간

        int lastDay = today.getActualMaximum(Calendar.DAY_OF_MONTH); //달의 마지막 날

        List<Analysis> allUserAnalysis = analysisRepository.findAll();

        List<Analysis> targetUser = new ArrayList<>();

        for (Analysis t : allUserAnalysis) {
            if (t.getUser() == user){
                targetUser.add(t);
            }
        }

        Map<String, Object> response = new HashMap<>();

        List<String> getMyTotal = new ArrayList<>(); //본인 일 공부시간
        List<String> getUsersAvg = new ArrayList<>(); //전체 평균 일 공부시간

        //본인 일별 공부시간
        for (int i = 1; i <= lastDay; i++){
            String getDay = String.valueOf(i);
            String day ="";
            for (Analysis find : targetUser) {
                String[] getDate = find.getDate().split("-");
                if (getDay.equals(getDate[2])) {
                    String[] getDaySum = find.getDaySum().split(":");
                    day = getDay + ":" + Integer.parseInt(getDaySum[0]);
                } else {
                    day = getDay + ":" + "0";
                }
            }
            getMyTotal.add(day);
        }

        //전체 평균 일 공부시간
        for (int i = 1; i <= lastDay; i++) {
            String dayAvg = "";
            String getDay = String.valueOf(i);
            int totalMM = 0;
            int cnt = 0;
            for (Analysis t : allUserAnalysis){
                String[] targetDate = t.getDate().split("-");
                if (getDay.equals(targetDate[2])){
                    String[] arr = t.getDaySum().split(":");

                    int allHH = Integer.parseInt(arr[0]);
                    int allMM = allHH * 60;
                    int alltotal = allMM + Integer.parseInt(arr[1]); //분으로 환산
                    totalMM += alltotal;
                    cnt+=1;
                }
            }
            int totalHH = totalMM / 60;
            int avg = 0;
            if (cnt != 0){
                avg = totalHH / cnt;
            }

            dayAvg = getDay + ":" + String.valueOf(avg);
            getUsersAvg.add(dayAvg);
        }

        response.put("usersAvg", getUsersAvg);
        response.put("myTotal", getMyTotal);

        return response;
    }
}
