package com.RoutineGongJakSo.BE.checkIn;

import com.RoutineGongJakSo.BE.checkIn.dto.CheckInDto;
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

    //[POST]체크인
    @Transactional
    public String checkIn(UserDetailsImpl userDetails) throws ParseException{
        // 로그인 여부 확인
        validator.loginCheck(userDetails);
        // 유저 정보를 찾음
        User user = validator.userInfo(userDetails);

        //현재 서울 날짜
        String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString();

        //체크인 테이블에서 해당 유저 + 오늘 날짜의 해당하는 친구들을 리스트에 담음
        List<CheckIn> checkInList = checkInRepository.findByUserAndDate(user, date);

        Calendar yesterDay = checkInValidator.yesterDayCalender(date); //전 날 기준 캘린더
        String strYesterDay = checkInValidator.DateFormat(yesterDay); //어제 날짜 str yyyy-MM-dd 형식
        Date setFormatter = checkInValidator.dateTimeFormat(strYesterDay); //yyyy-MM-dd 05:00:00(전일 오전 5시)

        //해당 유저의 전날 날짜의 전체 기록 찾기
        List<CheckIn> yesterDayCheckList = checkInRepository.findByUserAndDate(user, strYesterDay);

        Calendar setFormat = Calendar.getInstance(); // 초기화 시간 05시 기준의 캘린더를 만들어 줌
        setFormat.setTime(setFormatter); // 전일 오전 5시 기준으로 캘린더 셋팅

        //현재시간보다 과거일 때,
        List<CheckIn> finalCheckInList = checkInValidator.setTimeCheck(yesterDayCheckList, checkInList, setFormat);

        //체크아웃을 하지 않은 상태에서 체크인을 시도할 경우 NPE
        for (CheckIn check : finalCheckInList) {
            if (check.getCheckOut() == null) {
                throw new NullPointerException("체크아웃을 먼저 해주세요");
            }
        }

        String nowTime = checkInValidator.nowTime(); //현재 서울 시간
        CheckIn checkIn = CheckIn.builder()
                .user(user)
                .date(date)
                .checkIn(nowTime)
                .build();

        checkInRepository.save(checkIn);

        return "Start!";
    }

    //[GET]사용자가 이미 start를 누른 상태라면, 값을 내려주는 곳(당일)
    public String getCheckIn(UserDetailsImpl userDetails) throws ParseException {

        validator.loginCheck(userDetails);  // 로그인 여부 확인
        User user = validator.userInfo(userDetails);   // 유저 정보를 찾음

        //시간을 형식에 맞게 포맷
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        SimpleDateFormat calenderFormatter = new SimpleDateFormat("HH:mm:ss");

        //[서울]현재 날짜
        String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString();

        Calendar yesterDay = checkInValidator.yesterDayCalender(date); //전 날 기준 캘린더
        String strYesterDay = checkInValidator.DateFormat(yesterDay); //어제 날짜 str yyyy-MM-dd 형식
        Date setFormatter = checkInValidator.dateTimeFormat(strYesterDay); //yyyy-MM-dd 05:00:00(전일 오전 5시)

        //해당 유저의 해당 날짜의 전체 기록 찾기
        List<CheckIn> checkInList = checkInRepository.findByUserAndDate(user, date);
        //해당 유저의 전날 날짜의 전체 기록 찾기
        List<CheckIn> yesterDayCheckList = checkInRepository.findByUserAndDate(user, strYesterDay);

        Calendar setFormat = Calendar.getInstance(); // 초기화 시간 05시
        setFormat.setTime(setFormatter); // 전일 오전 5시 기준으로 캘린더 셋팅

        //새벽 다섯시 전까지의 모든 친구들을 담은 리스트
        List<CheckIn> finalCheckInList = checkInValidator.setTimeCheck(yesterDayCheckList, checkInList, setFormat);

        //기록 정보가 없을 때 return
        if (finalCheckInList.size() < 1) {
            return "00:00:00";
        }

        //마지막 체크인 시간 확인
        CheckIn lastCheckIn = finalCheckInList.get(finalCheckInList.size()-1);
        String strLastCheckOut = lastCheckIn.getCheckOut();
        List<Analysis> analysis = analysisRepository.findByUserAndDate(user, date);

        //마지막 기록의 체크아웃 값이 있는지 확인
        if (strLastCheckOut != null) {
            //ToDo 해당 부분 추가로 수정하기!!
            //return Analysis analysis = 해당 유저의 해당 날짜의 마지막 sumTime;
            if (analysis.size() > 0){ // 만약 analysis 테이블에 그 날의 기록된 daySum이 있다면, 그 값 내려주기
                return analysis.get(0).getDaySum();
            }
            throw new NullPointerException("아직 Start 를 누르지 않았습니다.");
        }

        //analysis 테이블의 해당 날짜의 기록이 있는 경우
        if (analysis.size() > 0) {
            strLastCheckOut = analysis.get(0).getDaySum();
            return strLastCheckOut;
        }

        String[] timeStamp = lastCheckIn.getCheckIn().split(":"); //시, 분, 초 나누기

        int HH = Integer.parseInt(timeStamp[0]); //시
        int mm = Integer.parseInt(timeStamp[1]); //분
        int ss = Integer.parseInt(timeStamp[2]); //초

        String sumDateTime = checkInValidator.sumDateTime(); //

        //Date 형식으로 포맷
        Date nowFormatter = formatter.parse(sumDateTime);

        //캘린더에 기준 시간(현재시간)을 넣어준다.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowFormatter);

        //기록된 체크인 시간을 시, 분, 초 단위로 쪼개서 빼준다.
        calendar.add(Calendar.HOUR, -HH);
        calendar.add(Calendar.MINUTE, -mm);
        calendar.add(Calendar.SECOND, -ss);

        return calenderFormatter.format(calendar.getTime());
    }

    //체크아웃
    @Transactional
    public String checkOut(UserDetailsImpl userDetails, CheckInDto.requestDto requestDto) throws ParseException{

        validator.loginCheck(userDetails);  // 로그인 여부 확인
        User user = validator.userInfo(userDetails);   // 유저 정보를 찾음

        //현재 서울 날짜
        String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString();

        //전일 날짜를 구함
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar yesterDay = Calendar.getInstance();
        yesterDay.setTime(sdf.parse(date));
        yesterDay.add(Calendar.DATE, -1);//전날
        String strYesterDay = sdf.format(yesterDay.getTime());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        String sumTomorrow = strYesterDay + "05:00:00"; //전일 오전5시 기준
        Date setFormatter = formatter.parse(sumTomorrow);

        Calendar setFormat = Calendar.getInstance(); // 초기화 시간 05시
        setFormat.setTime(setFormatter);

        //현재 시간
        ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

        //Date 포맷을 위해 년, 월, 일을 하나씩 적출해서 string 으로 변환하고
        String nowYear = String.valueOf(nowSeoul.getYear());
        String nowMonth = String.valueOf(nowSeoul.getMonthValue());
        String nowDay = String.valueOf(nowSeoul.getDayOfMonth());

        //현재 시간을 스트링으로 변환하고
        String nowTime = nowSeoul.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        //시간과 년월일을 합친다
        String sumDateTime = nowYear+ "-" + nowMonth+ "-" + nowDay+ " " + nowTime;

        //Date 형식으로 포맷
        Date nowFormatter = formatter.parse(sumDateTime);

        //캘린더에 기준 시간(현재시간)을 넣어준다.
        Calendar toDay = Calendar.getInstance();
        toDay.setTime(nowFormatter);

        String daysum = requestDto.getTotalTime();

        if (setFormat.compareTo(toDay) < 0){ // 인자보다 과거일 경우
            List<Analysis> find = analysisRepository.findByUserAndDate(user, date);
            List<CheckIn> checkInList = checkInRepository.findByUserAndDate(user, date);
            CheckIn check = checkInList.get(checkInList.size()-1);

            if (check.getCheckOut() != null){
                throw new NullPointerException("Start 를 먼저 눌러주세요.");
            }

            if (find.size() < 1) {
                check.setCheckOut(nowSeoul.format(DateTimeFormatter.ofPattern("HH:mm:ss")));//ToDo 이 위치에서 있어야 함
                Analysis analysis = Analysis.builder()
                        .daySum(daysum)
                        .date(date)
                        .user(user)
                        .checkIns(checkInList)
                        .build();
                analysisRepository.save(analysis);
                check.setAnalysis(analysis);
            }else { //!null이라면, 수정
                find.get(0).setDaySum(requestDto.getTotalTime());
                check.setCheckOut(nowSeoul.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            }
            return "수고하셨습니다.";
        }
        return "수고하셨습니다.";
    }
}
