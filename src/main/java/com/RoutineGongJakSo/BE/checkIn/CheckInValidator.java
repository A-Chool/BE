package com.RoutineGongJakSo.BE.checkIn;

import com.RoutineGongJakSo.BE.model.Analysis;
import com.RoutineGongJakSo.BE.model.CheckIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckInValidator {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
    SimpleDateFormat calenderFormatter = new SimpleDateFormat("HH:mm:ss");

    // 캘린더 생성
    public Calendar todayCalender(String date) throws ParseException {
        Calendar today = Calendar.getInstance(); //캘린더를 만들어 줌
        today.setTime(sdf.parse(date)); //파라미터 기준으로 캘린더 셋팅
        return today;
    }

    //String 현재시간
    public String nowTime() {
        ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        return nowSeoul.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    //날짜 yyyy-MM-dd 형식에 맞게 포맷
    public String DateFormat(Calendar calendar) {
        return sdf.format(calendar.getTime());
    }

    //전 날 오전 5시 기준 데이터 포맷
    public Date dateTimeFormat(String setDateTime) throws ParseException {
        String sumToday = setDateTime + "05:00:00"; //어제 날짜 + 오전5시 -> 조건을 걸기 위해 만들어줌
        return formatter.parse(sumToday);
    }

    //String yyyy-MM-dd HH:mm:ss 형식으로 return; 현재 시간
    public String sumDateTime() {
        ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        String nowYear = String.valueOf(nowSeoul.getYear());
        String nowMonth = String.valueOf(nowSeoul.getMonthValue());
        String nowDay = String.valueOf(nowSeoul.getDayOfMonth());
        String nowTime = nowSeoul.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return nowYear + "-" + nowMonth + "-" + nowDay + " " + nowTime;
    }

    // 당일 오전 다섯시로 셋팅
    public void setCalendarTime(Calendar setTime) throws ParseException {
        String strToday = DateFormat(setTime); //오늘 날짜 str yyyy-MM-dd 형식
        Date setFormatter = dateTimeFormat(strToday); //yyyy-MM-dd 05:00:00(당일 오전 5시)
        setTime.setTime(setFormatter); //yyyy-MM-dd 05:00:00(당일 오전 5시) 캘린더에 적용
    }

    // 현재 시간 기준으로 셋팅
    public void todayCalendarTime(Calendar setTime) throws ParseException {
        String sumDateTime = sumDateTime(); //String yyyy-MM-dd HH:mm:ss 현재시간
        Date nowFormatter = formatter.parse(sumDateTime); //Date 형식으로 포맷
        setTime.setTime(nowFormatter);
    }

    //사용자가 1회 이상 체크인 체크아웃 완성된 행이 있을 경우 시간 계산
    public String analysisCheck(Optional<Analysis> analysis, Calendar today) throws ParseException {
        if (analysis.isPresent()) {
            String daySum = calenderFormatter.format(today.getTime());
            analysis.get().getDaySum();
            Calendar analysisDay = todayCalender(analysis.get().getDate()); //analysis 기준 calendar 만들기
            String setTime = analysis.get().getDate() + " " + analysis.get().getDaySum();
            Date setFromatter = formatter.parse(setTime);
            analysisDay.setTime(setFromatter); // analysisa 의 daySum 기준시간으로 셋팅

            String[] reTimeStamp = daySum.split(":");

            int reHH = Integer.parseInt(reTimeStamp[0]); //시
            int remm = Integer.parseInt(reTimeStamp[1]); //분
            int ress = Integer.parseInt(reTimeStamp[2]); //초

            analysisDay.add(Calendar.HOUR, reHH);
            analysisDay.add(Calendar.MINUTE, remm);
            analysisDay.add(Calendar.SECOND, ress);

            daySum = calenderFormatter.format(analysisDay.getTime());
            return daySum;
        }
        return calenderFormatter.format(today.getTime());
    }

    //온라인 여부 확인
    public boolean onlineCheck(List<CheckIn> findCheckIns) {
        if (findCheckIns.size() == 0 || findCheckIns.get(findCheckIns.size() - 1).getCheckOut() != null) {
            return false;
        }
        return true;
    }

    //지각 여부 확인
    public boolean lateCheck(List<CheckIn> findCheckIns) {
        int HH = 0; // 첫번째 체크인 시간

        if (findCheckIns.size() != 0) {
            String timeCheck = findCheckIns.get(0).getCheckIn();
            String[] arrayTime = timeCheck.split(":");
            HH = Integer.parseInt(arrayTime[0]);
            if (HH < 5) { //다음 날로 넘아간 새벽시간에 체크인 했을 때, 지각 처리를 하기 위한 조건
                HH += 24;
            }
        }
        if (findCheckIns.size() == 0 || HH >= 9) {
            return true;
        }
        return false;
    }

    public String totalTime(List<Analysis> allUserList) {

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
}
