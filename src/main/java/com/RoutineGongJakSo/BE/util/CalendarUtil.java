package com.RoutineGongJakSo.BE.util;

import com.RoutineGongJakSo.BE.client.analysis.model.Analysis;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.RoutineGongJakSo.BE.util.Formatter.*;

@Slf4j
public class CalendarUtil {

    // 캘린더 생성
    public static Calendar todayCalender(String date) throws ParseException {
        Calendar today = Calendar.getInstance(); //캘린더를 만들어 줌
        today.setTime(sdf.parse(date)); //파라미터 기준으로 캘린더 셋팅
        return today;
    }

    //날짜 yyyy-MM-dd 형식에 맞게 포맷
    public static String DateFormat(Calendar calendar) {
        return sdf.format(calendar.getTime());
    }

    // 당일 오전 다섯시로 셋팅
    public static void setCalendarTime(Calendar setTime) throws ParseException {
        String strToday = DateFormat(setTime); //오늘 날짜 str yyyy-MM-dd 형식
        Date setFormatter = dateTimeFormat(strToday); //yyyy-MM-dd 05:00:00(당일 오전 5시)
        setTime.setTime(setFormatter); //yyyy-MM-dd 05:00:00(당일 오전 5시) 캘린더에 적용
    }

    // 현재 시간 기준으로 셋팅
    public static void todayCalendarTime(Calendar setTime) throws ParseException {
        String sumDateTime = sumDateTime(); //String yyyy-MM-dd HH:mm:ss 현재시간
        Date nowFormatter = formatter.parse(sumDateTime); //Date 형식으로 포맷
        setTime.setTime(nowFormatter);
    }

    public static String totalTime(List<Analysis> allUserList) {

        int total = 0; //총 누적 공부시간이 담김

        for (Analysis find : allUserList) {
            String[] arrayFind = find.getDaySum().split(":");
            int allHH = Integer.parseInt(arrayFind[0]);
            int allMM = allHH * 60;
            int alltotal = allMM + Integer.parseInt(arrayFind[1]); //분으로 환산
            total += alltotal;
        }

        String totalHH = String.valueOf(total / 60);
//        String totalMM = String.valueOf(total % 60); 나중에 분 필요하다 그러면, 이거 내려주면됨
//        String totalSumTime = totalHH + ":" + totalMM;
        return totalHH;
    }

    //String yyyy-MM-dd HH:mm:ss 형식으로 return; 현재 시간
    private static String sumDateTime() {
        ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        String nowYear = String.valueOf(nowSeoul.getYear());
        String nowMonth = String.valueOf(nowSeoul.getMonthValue());
        String nowDay = String.valueOf(nowSeoul.getDayOfMonth());
        String nowTime = nowSeoul.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return nowYear + "-" + nowMonth + "-" + nowDay + " " + nowTime;
    }

    //전 날 오전 5시 기준 데이터 포맷
    private static Date dateTimeFormat(String setDateTime) throws ParseException {
        String sumToday = setDateTime + "05:00:00"; //어제 날짜 + 오전5시 -> 조건을 걸기 위해 만들어줌
        return formatter.parse(sumToday);
    }
}
