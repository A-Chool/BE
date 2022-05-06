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
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckInService {

    private final CheckInRepository checkInRepository;
    private final AnalysisRepository analysisRepository;
    private final CheckInValidator checkInValidator;
    private final Validator validator;

    ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
    SimpleDateFormat calenderFormatter = new SimpleDateFormat("HH:mm:ss");
    String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString(); // 현재 서울 날짜

    //ToDo : 현재시간 이상하게 나오니까, checkInValidator.sumDateTime() 사용해서 초기화하기

    //[POST]체크인
    @Transactional
    public String checkIn(UserDetailsImpl userDetails) throws ParseException{

        User user = validator.userInfo(userDetails);// 유저 정보를 찾음(로그인 하지 않았다면 에러 뜰 것)

        Calendar setDay = checkInValidator.todayCalender(date); //오늘 기준 캘린더
        String strToday = checkInValidator.DateFormat(setDay); //오늘 날짜 str yyyy-MM-dd 형식
        Date setFormatter = checkInValidator.dateTimeFormat(strToday); //yyyy-MM-dd 05:00:00(당일 오전 5시)
        setDay.setTime(setFormatter); //yyyy-MM-dd 05:00:00(당일 오전 5시) 캘린더에 적용

        Calendar today = checkInValidator.todayCalender(date); //현재 시간 기준 날짜
        String sumDateTime = checkInValidator.sumDateTime(); //String yyyy-MM-dd HH:mm:ss 현재시간
        Date nowFormatter = formatter.parse(sumDateTime); //Date 형식으로 포맷
        today.setTime(nowFormatter);

        //compareTo() > 0 : 인자보다 미래
        //compareTo() < 0 : 인자보다 과거
        //compareTo() == 0 : 인자와 같은 시간
        if (today.compareTo(setDay) < 0){
            today.add(Calendar.DATE, -1); //오전 5시보다 과거라면, 현재 날짜에서 -1
        }

        String setToday = checkInValidator.DateFormat(today); //Date -> String 변환

        //체크인 테이블에서 해당 유저 + 오늘 날짜의 해당하는 친구들을 리스트에 담음
        List<CheckIn> checkInList = checkInRepository.findByUserAndDate(user, setToday);
        Optional<Analysis> analysis = analysisRepository.findByUserAndDate(user, setToday);

        //체크아웃을 하지 않은 상태에서 체크인을 시도할 경우 NPE
        for (CheckIn check : checkInList) {
            if (check.getCheckOut() == null) {
                throw new NullPointerException("체크아웃을 먼저 해주세요");
            }
        }

        String response = "00:00:00";

        if (analysis.isPresent()){
            response = analysis.get().getDaySum();
        }

        String nowTime = checkInValidator.nowTime(); //현재 서울 시간
        CheckIn checkIn = CheckIn.builder()
                .user(user)
                .date(setToday)
                .checkIn(nowTime)
                .build();

        checkInRepository.save(checkIn);

        return response;
    }

    //[GET]사용자가 이미 start를 누른 상태라면, 값을 내려주는 곳(당일)
    public String getCheckIn(UserDetailsImpl userDetails) throws ParseException {

        User user = validator.userInfo(userDetails); // 유저 정보를 찾음(로그인 하지 않았다면 에러 뜰 것)

        Calendar setDay = checkInValidator.todayCalender(date); //오늘 기준 캘린더(오전 5시로 셋팅됨)
        String strToday = checkInValidator.DateFormat(setDay); //오늘 날짜 str yyyy-MM-dd 형식
        Date setFormatter = checkInValidator.dateTimeFormat(strToday); //yyyy-MM-dd 05:00:00(당일 오전 5시)
        setDay.setTime(setFormatter); //yyyy-MM-dd 05:00:00(당일 오전 5시) 캘린더에 적용

        Calendar today = checkInValidator.todayCalender(date); //현재 시간 기준 날짜
        String sumDateTime = checkInValidator.sumDateTime(); //String yyyy-MM-dd HH:mm:ss 현재시간
        Date nowFormatter = formatter.parse(sumDateTime); //Date 형식으로 포맷
        today.setTime(nowFormatter);

        //compareTo() > 0 : 인자보다 미래
        //compareTo() < 0 : 인자보다 과거
        //compareTo() == 0 : 인자와 같은 시간
        if (today.compareTo(setDay) < 0){
            today.add(Calendar.DATE, -1); //오전 5시보다 과거라면, 현재 날짜에서 -1
        }

        String setToday = checkInValidator.DateFormat(today); //Date -> String 변환

        Optional<Analysis> analysis = analysisRepository.findByUserAndDate(user, setToday);

        List<CheckIn> findCheckIn = checkInRepository.findByUserAndDate(user, setToday);
        CheckIn firstCheckIn = findCheckIn.get(findCheckIn.size()-1); // 처음이 아니라면 analysis 에 값이 있을거고, 그렇다면 위의 조건문에서 return 당함

        if (firstCheckIn == null){ //기록이 없는 경우
            return "00:00:00";
        }

        //체크인, 체크아웃 기록된 세트가 1회 이상 있는 경우
        if (findCheckIn.get(findCheckIn.size()-1).getCheckOut() != null){
            return analysis.get().getDaySum();
        }

        Calendar checkInCalendar = Calendar.getInstance(); //checkIn 시간 기준용
        String setCheckIn = firstCheckIn.getDate() + " " + firstCheckIn.getCheckIn(); //yyyy-MM-dd HH:mm:ss
        checkInCalendar.setTime(formatter.parse(setCheckIn)); //checkIn 시간 기준으로 캘린더 셋팅

        String[] timeStamp = firstCheckIn.getCheckIn().split(":"); //시, 분, 초 나누기

        int HH = Integer.parseInt(timeStamp[0]); //시
        int mm = Integer.parseInt(timeStamp[1]); //분
        int ss = Integer.parseInt(timeStamp[2]); //초

        today.add(Calendar.HOUR, -HH);
        today.add(Calendar.MINUTE, -mm);
        today.add(Calendar.SECOND, -ss);

        //=========================================================================================
        //analysis !=null && 마지막 체크아웃 값이 !=null 일 때만 실행
        if (analysis.isPresent()){
            String daySum = calenderFormatter.format(today.getTime());
            analysis.get().getDaySum();
            Calendar analysisDay = checkInValidator.todayCalender(analysis.get().getDate()); //analysis 기준 calendar 만들기
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
//=========================================================================================

        return calenderFormatter.format(today.getTime());
    }

    //[POST]체크아웃
    @Transactional
    public String checkOut(UserDetailsImpl userDetails) throws ParseException{

        User user = validator.userInfo(userDetails);   // 유저 정보를 찾음(로그인 하지 않았다면 에러 뜰 것)

        Calendar setDay = checkInValidator.todayCalender(date); //오늘 기준 캘린더(오전 5시로 셋팅됨)
        String strToday = checkInValidator.DateFormat(setDay); //오늘 날짜 str yyyy-MM-dd 형식
        Date setFormatter = checkInValidator.dateTimeFormat(strToday); //yyyy-MM-dd 05:00:00(당일 오전 5시)
        setDay.setTime(setFormatter); //yyyy-MM-dd 05:00:00(당일 오전 5시) 캘린더에 적용

        Calendar today = checkInValidator.todayCalender(date); //현재 시간 기준 날짜
        String sumDateTime = checkInValidator.sumDateTime(); //String yyyy-MM-dd HH:mm:ss 현재시간
        Date nowFormatter = formatter.parse(sumDateTime); //Date 형식으로 포맷
        today.setTime(nowFormatter);

        //compareTo() > 0 : 인자보다 미래
        //compareTo() < 0 : 인자보다 과거
        //compareTo() == 0 : 인자와 같은 시간
        if (today.compareTo(setDay) < 0){
            today.add(Calendar.DATE, -1); //오전 5시보다 과거라면, 현재 날짜에서 -1
        }

        String setToday = checkInValidator.DateFormat(today); //Date -> String 변환

        List<CheckIn> findCheckIns = checkInRepository.findByUserAndDate(user, setToday);
        CheckIn lastCheckIn = findCheckIns.get(findCheckIns.size()-1); //마지막번째 기록을 get

        Optional<Analysis> findAnalysis = analysisRepository.findByUserAndDate(user, setToday);

        //==============================================================위의 로직 수정 후 재 츄라이
        String daySum = getCheckIn(userDetails);
        if (findAnalysis.isPresent()){ //값이 없다면, 처음 기록이라는 뜻이니까, 위에 서비스단에서 만들어 놓은 로직 재활용 하면 됨
            Calendar analysisDay = checkInValidator.todayCalender(findAnalysis.get().getDate()); //analysis 기준 calendar 만들기
            String setTime = findAnalysis.get().getDate() + " " + findAnalysis.get().getDaySum();
            Date setFromatter = formatter.parse(setTime);
            analysisDay.setTime(setFromatter); // analysisa 의 daySum 기준시간으로 셋팅

            String[] timeStamp = daySum.split(":");

            int HH = Integer.parseInt(timeStamp[0]); //시
            int mm = Integer.parseInt(timeStamp[1]); //분
            int ss = Integer.parseInt(timeStamp[2]); //초

            analysisDay.add(Calendar.HOUR, HH);
            analysisDay.add(Calendar.MINUTE, mm);
            analysisDay.add(Calendar.SECOND, ss);

            log.info("총 공부 시간" + calenderFormatter.format(analysisDay.getTime()));
            daySum = calenderFormatter.format(analysisDay.getTime());
        }

        //==============================================================
//        String daySum = checkInValidator.totalDayTime(findAnalysis, userDetails); //당일 누적 공부시간

        if (lastCheckIn.getCheckOut() != null){
            throw new NullPointerException("Start 를 먼저 눌러주세요");
        }

        if (findAnalysis.isPresent()){
            lastCheckIn.setCheckOut(nowSeoul.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            findAnalysis.get().setDaySum(daySum); // 총 공부 시간
            return "수고 하셨습니다.";
        }
        lastCheckIn.setCheckOut(nowSeoul.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        Analysis analysis = Analysis.builder()
                .daySum(daySum)
                .date(setToday)
                .user(user)
                .checkIns(findCheckIns)
                .build();
        lastCheckIn.setAnalysis(analysis);
        analysisRepository.save(analysis);
        return "수고 하셨습니다.";
    }


    //ToDo 총 공부 시간 계산하기
//    public void daySum(UserDetailsImpl userDetails) throws ParseException {
//
//        User user = validator.userInfo(userDetails);
//
//        Calendar yesterDay = checkInValidator.yesterDayCalender(date); //전 날 기준 캘린더
//        String strYesterDay = checkInValidator.DateFormat(yesterDay); //어제 날짜 str yyyy-MM-dd 형식
//        Date setFormatter = checkInValidator.dateTimeFormat(strYesterDay); //yyyy-MM-dd 05:00:00(전일 오전 5시)
//
//        //해당 유저의 해당 날짜의 전체 기록 찾기
//        List<CheckIn> checkInList = checkInRepository.findByUserAndDate(user, date);
//        //해당 유저의 전날 날짜의 전체 기록 찾기
//        List<CheckIn> yesterDayCheckList = checkInRepository.findByUserAndDate(user, strYesterDay);
//
//        Calendar setFormat = Calendar.getInstance(); // 초기화 시간 05시
//        setFormat.setTime(setFormatter); // 전일 오전 5시 기준으로 캘린더 셋팅
//
//        //새벽 다섯시 전까지의 모든 친구들을 담은 리스트
//        List<CheckIn> finalCheckInList = checkInValidator.setTimeCheck(yesterDayCheckList, checkInList, setFormat);
//
//        //마지막 체크인 시간 확인
//        CheckIn lastCheckIn = finalCheckInList.get(finalCheckInList.size()-1);
//        String strLastCheckOut = lastCheckIn.getCheckOut();
//
//        Optional<Analysis> analysis = analysisRepository.findByUserAndDate(user, date);
//        Optional<Analysis> yesterDayAnalysis = analysisRepository.findByUserAndDate(user, strYesterDay);
//
//        //Analysis daySum이 마지막으로 업데이트 된 시간 -> 체크인리스트의 마지막번째 체크아웃 시간
//        //전날 Analysis의 값과 시간이 오전 5시 이전이라면, 그 값이 마지막에 기록된 친구가 될 수 있도록 셋팅
//        String sumAnalysisDateTime = yesterDayAnalysis.get().getDate() + lastCheckIn.getCheckOut();
//        Date sumFormatter = formatter.parse(sumAnalysisDateTime); //Analysis 마지막 저장 시간
//        Calendar sumFormat = Calendar.getInstance(); //체크인 시간
//        sumFormat.setTime(sumFormatter); //체크인 시간
//        if (setFormat.compareTo(sumFormat) > 0){
//            analysis = yesterDayAnalysis;
//        }
//
//        //마지막 기록의 체크아웃 값이 있는지 확인
//        if (strLastCheckOut != null) {
//            throw new NullPointerException("아직 Start 를 누르지 않았습니다.");
//        }
//
//        String[] timeStamp = lastCheckIn.getCheckIn().split(":"); //시, 분, 초 나누기
//
//        int HH = Integer.parseInt(timeStamp[0]); //시
//        int mm = Integer.parseInt(timeStamp[1]); //분
//        int ss = Integer.parseInt(timeStamp[2]); //초
//
//        String sumDateTime = checkInValidator.sumDateTime();
//
//        //Date 형식으로 포맷
//        Date nowFormatter = formatter.parse(sumDateTime);
//
//        //캘린더에 기준 시간(현재시간)을 넣어준다.
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(nowFormatter);
//
//        //기록된 체크인 시간을 시, 분, 초 단위로 쪼개서 빼준다.
//        calendar.add(Calendar.HOUR, -HH);
//        calendar.add(Calendar.MINUTE, -mm);
//        calendar.add(Calendar.SECOND, -ss);
//
//        String sumtime = calenderFormatter.format(calendar.getTime()); //마지막 체크인 시간 기준 체크아웃 시점의 공부시간
//        log.info("마지막 공부시간 : " + sumtime);
//        String[] reSumTime = sumtime.split(":");
//        int RHH = Integer.parseInt(reSumTime[0]); // Analysis 시
//        int Rmm = Integer.parseInt(reSumTime[1]); //Analysis 분
//        int Rss = Integer.parseInt(reSumTime[2]); //Analysis 초
//
//
//        String[] daySumTime = analysis.get().getDaySum().split(":");
//
//        int DHH = Integer.parseInt(daySumTime[0]); // Analysis 시
//        int Dmm = Integer.parseInt(daySumTime[1]); //Analysis 분
//        int Dss = Integer.parseInt(daySumTime[2]); //Analysis 초
//
//        int hour = RHH + DHH;
//        int minute = Rmm + Dmm;
//        int second = Rss + Dss;
//
//        String strH = String.valueOf(hour);
//        String strM = String.valueOf(minute);
//        String strS = String.valueOf(second);
//
//        String daySum = strH + ":" + strM + ":" + strS;
//        log.info("daySum : " + daySum);
//    }



}
