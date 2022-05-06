package com.RoutineGongJakSo.BE.checkIn;

import com.RoutineGongJakSo.BE.admin.dto.MemberDto;
import com.RoutineGongJakSo.BE.admin.dto.TeamDto;
import com.RoutineGongJakSo.BE.admin.repository.WeekTeamRepository;
import com.RoutineGongJakSo.BE.checkIn.repository.AnalysisRepository;
import com.RoutineGongJakSo.BE.checkIn.repository.CheckInRepository;
import com.RoutineGongJakSo.BE.model.*;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import com.RoutineGongJakSo.BE.user.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Check;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckInService {

    private final CheckInRepository checkInRepository;
    private final AnalysisRepository analysisRepository;
    private final CheckInValidator checkInValidator;
    private final WeekTeamRepository weekTeamRepository;
    private final Validator validator;

    ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
    String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString(); // 현재 서울 날짜

    //ToDo : 현재시간 이상하게 나오니까, checkInValidator.sumDateTime() 사용해서 초기화하기

    //[POST]체크인
    @Transactional
    public String checkIn(UserDetailsImpl userDetails) throws ParseException {

        User user = validator.userInfo(userDetails);// 유저 정보를 찾음(로그인 하지 않았다면 에러 뜰 것)

        Calendar setDay = checkInValidator.todayCalender(date); //오늘 기준 캘린더
        checkInValidator.setCalendarTime(setDay); // yyyy-MM-dd 05:00:00(당일 오전 5시) 캘린더에 적용

        Calendar today = checkInValidator.todayCalender(date); //현재 시간 기준 날짜
        checkInValidator.todayCalendarTime(today); // String yyyy-MM-dd HH:mm:ss 현재시간

        //compareTo() > 0 : 인자보다 미래
        //compareTo() < 0 : 인자보다 과거
        //compareTo() == 0 : 인자와 같은 시간
        if (today.compareTo(setDay) < 0) {
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

        if (analysis.isPresent()) {
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

        Calendar setDay = checkInValidator.todayCalender(date); //오늘 기준 캘린더
        checkInValidator.setCalendarTime(setDay); // yyyy-MM-dd 05:00:00(당일 오전 5시) 캘린더에 적용

        Calendar today = checkInValidator.todayCalender(date); //현재 시간 기준 날짜
        checkInValidator.todayCalendarTime(today); // String yyyy-MM-dd HH:mm:ss 현재시간

        if (today.compareTo(setDay) < 0) {
            today.add(Calendar.DATE, -1); //오전 5시보다 과거라면, 현재 날짜에서 -1
        }

        String setToday = checkInValidator.DateFormat(today); //Date -> String 변환

        Optional<Analysis> analysis = analysisRepository.findByUserAndDate(user, setToday);

        List<CheckIn> findCheckIn = checkInRepository.findByUserAndDate(user, setToday);

        if (findCheckIn.size() == 0) { //기록이 없는 경우
            return "00:00:00";
        }

        CheckIn firstCheckIn = findCheckIn.get(findCheckIn.size() - 1); // 처음이 아니라면 analysis 에 값이 있을거고, 그렇다면 위의 조건문에서 return 당함

        //체크인, 체크아웃 기록된 세트가 1회 이상 있는 경우
        if (findCheckIn.get(findCheckIn.size() - 1).getCheckOut() != null) {
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

        //사용자가 1회 이상 체크인 체크아웃 기록이 있을 경우 시간 계산
        return checkInValidator.analysisCheck(analysis, today);
    }

    //[POST]체크아웃
    @Transactional
    public String checkOut(UserDetailsImpl userDetails) throws ParseException {

        User user = validator.userInfo(userDetails);   // 유저 정보를 찾음(로그인 하지 않았다면 에러 뜰 것)

        Calendar setDay = checkInValidator.todayCalender(date); //오늘 기준 캘린더
        checkInValidator.setCalendarTime(setDay); // yyyy-MM-dd 05:00:00(당일 오전 5시) 캘린더에 적용

        Calendar today = checkInValidator.todayCalender(date); //현재 시간 기준 날짜
        checkInValidator.todayCalendarTime(today); // String yyyy-MM-dd HH:mm:ss 현재시간

        if (today.compareTo(setDay) < 0) {
            today.add(Calendar.DATE, -1); //오전 5시보다 과거라면, 현재 날짜에서 -1
        }

        String setToday = checkInValidator.DateFormat(today); //Date -> String 변환

        List<CheckIn> findCheckIns = checkInRepository.findByUserAndDate(user, setToday);
        CheckIn lastCheckIn = findCheckIns.get(findCheckIns.size() - 1); //마지막번째 기록을 get

        Optional<Analysis> findAnalysis = analysisRepository.findByUserAndDate(user, setToday);

        String daySum = getCheckIn(userDetails); //총 공부시간

        if (lastCheckIn.getCheckOut() != null) {
            throw new NullPointerException("Start 를 먼저 눌러주세요");
        }

        if (findAnalysis.isPresent()) {
            lastCheckIn.setCheckOut(nowSeoul.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            lastCheckIn.setAnalysis(findCheckIns.get(0).getAnalysis());
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

    //해당 주차의 모든 유저들의 기록
    public List<CheckInListDto.TeamListDto> getAllCheckList(UserDetailsImpl userDetails, String week) throws ParseException {

        validator.loginCheck(userDetails); // 로그인 여부 확인

        Calendar setDay = checkInValidator.todayCalender(date); //오늘 기준 캘린더
        checkInValidator.setCalendarTime(setDay); // yyyy-MM-dd 05:00:00(당일 오전 5시) 캘린더에 적용

        Calendar today = checkInValidator.todayCalender(date); //현재 시간 기준 날짜
        checkInValidator.todayCalendarTime(today); // String yyyy-MM-dd HH:mm:ss 현재시간

        if (today.compareTo(setDay) < 0) {
            today.add(Calendar.DATE, -1); //오전 5시보다 과거라면, 현재 날짜에서 -1
        }

        String setToday = checkInValidator.DateFormat(today); //Date -> String 변환

        List<WeekTeam> weekTeams = weekTeamRepository.findByWeek(week); // 해당 주차 팀 찾기
        List<CheckInListDto.TeamListDto> teamListDtos = new ArrayList<>();

        for (WeekTeam weekTeam : weekTeams) {
            List<CheckInListDto.UserDto> userDtos = new ArrayList<>();

            for (Member member : weekTeam.getMemberList()) {
                List<CheckIn> findCheckIns = checkInRepository.findByUserAndDate(member.getUser(), setToday);

                boolean online = true; //온라인 여부
                boolean lateCheck = false; //지각 여부
                int HH = 0; // 첫번째 체크인 시간
                if (findCheckIns.size() != 0){
                    String timeCheck = findCheckIns.get(0).getCheckIn();
                    String[] arrayTime = timeCheck.split(":");
                    HH = Integer.parseInt(arrayTime[0]);
                    if (HH < 5){ //다음 날로 넘아간 새벽시간에 체크인 했을 때, 지각 처리를 하기 위한 조건
                        HH += 24;
                    }
                }

                //온라인 여부 확인
                if (findCheckIns.size() == 0 || findCheckIns.get(findCheckIns.size()-1).getCheckOut() != null){
                    online = false;
                }

                //지각 여부 확인
                if (findCheckIns.size() == 0 || HH > 9){
                    lateCheck = true;
                }

                CheckInListDto.UserDto userDto = CheckInListDto.UserDto.builder()
                        .memberId(member.getMemberId())
                        .userName(member.getUser().getUserName())
                        .userEmail(member.getUser().getUserEmail())
                        .phoneNumber(member.getUser().getPhoneNumber())
                        .online(online)
                        .lateCheck(lateCheck)
                        .build();
                userDtos.add(userDto);
            }

            CheckInListDto.TeamListDto teamListDto = CheckInListDto.TeamListDto.builder()
                    .teamId(weekTeam.getWeekTeamId())
                    .teamName(weekTeam.getTeamName())
                    .week(weekTeam.getWeek())
                    .memberList(userDtos)
                    .build();

            teamListDtos.add(teamListDto);
        }
        return teamListDtos;

    }
    }
