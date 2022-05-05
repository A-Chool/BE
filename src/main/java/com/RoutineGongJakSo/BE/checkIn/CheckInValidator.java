package com.RoutineGongJakSo.BE.checkIn;

import com.RoutineGongJakSo.BE.model.CheckIn;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CheckInValidator {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
    ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

    //전날 기준 캘린더
    public Calendar yesterDayCalender(String date) throws ParseException {
        //전일 날짜를 구함
        Calendar yesterDay = Calendar.getInstance(); //캘린더를 만들어 줌
        yesterDay.setTime(sdf.parse(date)); //오늘 날짜 기준으로 캘린더 셋팅
        yesterDay.add(Calendar.DATE, -1); //오늘 날짜에서 하루를 뺌. 즉, 전 날로 셋팅
        return yesterDay;
    }

    //날짜 yyyy-MM-dd 형식에 맞게 포맷
    public String DateFormat(Calendar calendar){
        return sdf.format(calendar.getTime());
    }

    //전 날 오전 5시 기준 데이터 포맷
    public Date dateTimeFormat(String setDateTime) throws ParseException{
        String sumTomorrow = setDateTime + "05:00:00"; //어제 날짜 + 오전5시 -> 조건을 걸기 위해 만들어줌
        return formatter.parse(sumTomorrow);
    }

    //String 현재시간
    public String nowTime(){
        return nowSeoul.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    //전일 새벽 5시 기준으로 비교(하루가 넘어갔을 때, 필요한 로)
    public List<CheckIn> setTimeCheck( List<CheckIn> yesterDayCheckList, List<CheckIn> checkInList, Calendar setFormat )throws ParseException{
        for (CheckIn checkIn :  yesterDayCheckList){
            String sumDateTime = checkIn.getDate() + checkIn.getCheckIn();
            Date sumFormatter = formatter.parse(sumDateTime); //체크인 시간
            Calendar sumFormat = Calendar.getInstance(); //체크인 시간
            sumFormat.setTime(sumFormatter); //체크인 시간
            if (setFormat.compareTo(sumFormat) > 0){ // 인자보다 미래일 경우
                checkInList.add(checkIn); //해당 유저의 해당 날짜 전체 기록에 추가
            }
        }
        return checkInList;
    }

    //String yyyy-MM-dd HH:mm:ss 형식으로 return;
    public String sumDateTime(){
        String nowYear = String.valueOf(nowSeoul.getYear());
        String nowMonth = String.valueOf(nowSeoul.getMonthValue());
        String nowDay = String.valueOf(nowSeoul.getDayOfMonth());
        String nowTime = nowSeoul.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return nowYear+ "-" + nowMonth+ "-" + nowDay+ " " + nowTime;
    }
}
