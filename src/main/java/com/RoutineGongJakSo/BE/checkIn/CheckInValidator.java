package com.RoutineGongJakSo.BE.checkIn;

import com.RoutineGongJakSo.BE.checkIn.repository.AnalysisRepository;
import com.RoutineGongJakSo.BE.checkIn.repository.CheckInRepository;
import com.RoutineGongJakSo.BE.model.Analysis;
import com.RoutineGongJakSo.BE.model.CheckIn;
import com.RoutineGongJakSo.BE.model.User;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
    ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString(); // 현재 서울 날짜

    private final Validator validator;
    private final CheckInRepository checkInRepository;
    private final AnalysisRepository analysisRepository;

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

    //String yyyy-MM-dd HH:mm:ss 형식으로 return; 현재 시간
    public String sumDateTime(){
        String nowYear = String.valueOf(nowSeoul.getYear());
        String nowMonth = String.valueOf(nowSeoul.getMonthValue());
        String nowDay = String.valueOf(nowSeoul.getDayOfMonth());
        String nowTime = nowSeoul.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return nowYear+ "-" + nowMonth+ "-" + nowDay+ " " + nowTime;
    }

    //총 공부 시간 계산하기
    public String daySum(User user) throws ParseException {
        Calendar yesterDay = yesterDayCalender(date); //전 날 기준 캘린더
        String strYesterDay = DateFormat(yesterDay); //어제 날짜 str yyyy-MM-dd 형식
        Date setFormatter = dateTimeFormat(strYesterDay); //yyyy-MM-dd 05:00:00(전일 오전 5시)

        //해당 유저의 해당 날짜의 전체 기록 찾기
        List<CheckIn> checkInList = checkInRepository.findByUserAndDate(user, date);
        //해당 유저의 전날 날짜의 전체 기록 찾기
        List<CheckIn> yesterDayCheckList = checkInRepository.findByUserAndDate(user, strYesterDay);

        Calendar setFormat = Calendar.getInstance(); // 초기화 시간 05시
        setFormat.setTime(setFormatter); // 전일 오전 5시 기준으로 캘린더 셋팅

        //새벽 다섯시 전까지의 모든 친구들을 담은 리스트
        List<CheckIn> finalCheckInList = setTimeCheck(yesterDayCheckList, checkInList, setFormat);

        //마지막 체크인 시간 확인
        CheckIn lastCheckIn = finalCheckInList.get(finalCheckInList.size()-1);
        String strLastCheckOut = lastCheckIn.getCheckIn();

        Optional<Analysis> analysis = analysisRepository.findByUserAndDate(user, date);
        Optional<Analysis> yesterDayAnalysis = analysisRepository.findByUserAndDate(user, strYesterDay);

        //Analysis daySum이 마지막으로 업데이트 된 시간 -> 체크인리스트의 마지막번째 체크아웃 시간
        //전날 Analysis의 값과 시간이 오전 5시 이전이라면, 그 값이 마지막에 기록된 친구가 될 수 있도록 셋팅
        String sumAnalysisDateTime = analysis.get().getDate() + lastCheckIn.getCheckOut();

        if (yesterDayAnalysis.isPresent()){
            sumAnalysisDateTime = yesterDayAnalysis.get().getDate() + lastCheckIn.getCheckOut();
        }

        ///////////////////////////////////////////////////////////////////////////////////////

        Date sumFormatter = formatter.parse(sumAnalysisDateTime); //Analysis 마지막 저장 시간
        Calendar sumFormat = Calendar.getInstance(); //체크인 시간
        sumFormat.setTime(sumFormatter); //체크인 시간
        if (setFormat.compareTo(sumFormat) > 0){
            analysis = yesterDayAnalysis;
        }

        //마지막 기록의 체크아웃 값이 있는지 확인
        if (strLastCheckOut != null) {
            throw new NullPointerException("아직 Start 를 누르지 않았습니다.");
        }

        String[] timeStamp = lastCheckIn.getCheckIn().split(":"); //시, 분, 초 나누기

        int HH = Integer.parseInt(timeStamp[0]); //시
        int mm = Integer.parseInt(timeStamp[1]); //분
        int ss = Integer.parseInt(timeStamp[2]); //초

        String sumDateTime = sumDateTime();

        //Date 형식으로 포맷
        Date nowFormatter = formatter.parse(sumDateTime);

        //캘린더에 기준 시간(현재시간)을 넣어준다.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowFormatter);

        //기록된 체크인 시간을 시, 분, 초 단위로 쪼개서 빼준다.
        calendar.add(Calendar.HOUR, -HH);
        calendar.add(Calendar.MINUTE, -mm);
        calendar.add(Calendar.SECOND, -ss);

        String sumtime = calenderFormatter.format(calendar.getTime()); //마지막 체크인 시간 기준 체크아웃 시점의 공부시간
        log.info("마지막 공부시간 : " + sumtime);
        String[] reSumTime = sumtime.split(":");
        int RHH = Integer.parseInt(reSumTime[0]); // Analysis 시
        int Rmm = Integer.parseInt(reSumTime[1]); //Analysis 분
        int Rss = Integer.parseInt(reSumTime[2]); //Analysis 초

        String[] daySumTime = analysis.get().getDaySum().split(":");

        int DHH = Integer.parseInt(daySumTime[0]); // Analysis 시
        int Dmm = Integer.parseInt(daySumTime[1]); //Analysis 분
        int Dss = Integer.parseInt(daySumTime[2]); //Analysis 초

        int hour = RHH + DHH;
        int minute = Rmm + Dmm;
        int second = Rss + Dss;

        String strH = String.valueOf(hour);
        String strM = String.valueOf(minute);
        String strS = String.valueOf(second);

        String daySum = strH + ":" + strM + ":" + strS;
        log.info("daySum : " + daySum);
        return daySum;
    }
}
