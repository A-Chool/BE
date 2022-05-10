package com.RoutineGongJakSo.BE.checkIn;

import com.RoutineGongJakSo.BE.admin.repository.WeekTeamRepository;
import com.RoutineGongJakSo.BE.checkIn.repository.AnalysisRepository;
import com.RoutineGongJakSo.BE.checkIn.repository.CheckInRepository;
import com.RoutineGongJakSo.BE.model.*;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.exception.UserException;
import com.RoutineGongJakSo.BE.security.exception.UserExceptionType;
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

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");

    //ToDo : 현재시간 이상하게 나오니까, checkInValidator.sumDateTime() 사용해서 초기화하기

    //[POST]체크인
    @Transactional
    public String checkIn(UserDetailsImpl userDetails) throws ParseException {

        User user = validator.userInfo(userDetails);// 유저 정보를 찾음(로그인 하지 않았다면 에러 뜰 것)

        String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString(); // 현재 서울 날짜

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
                throw new UserException(UserExceptionType.TRY_CHECKOUT);
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
    public CheckInListDto.CheckInDto getCheckIn(UserDetailsImpl userDetails) throws ParseException {

        User user = validator.userInfo(userDetails); // 유저 정보를 찾음(로그인 하지 않았다면 에러 뜰 것)

        String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString(); // 현재 서울 날짜

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

        List<CheckInListDto.TodayLogDto> todayLogDtoList = new ArrayList<>(); // 그 날의 로그 기록

        if (findCheckIn.size() == 0) { //기록이 없는 경우

            List<Analysis> allUserList = analysisRepository.findByUser(user);

            String total = checkInValidator.totalTime(allUserList); //총 누적 공부시간

            CheckInListDto.CheckInDto checkInDto = CheckInListDto.CheckInDto.builder()
                    .daySumTime("00:00:00")
                    .totalSumTime(total)
                    .todayLog(todayLogDtoList)
                    .build();

            return checkInDto;

        }

        CheckIn firstCheckIn = findCheckIn.get(findCheckIn.size() - 1); // 처음이 아니라면 analysis 에 값이 있을거고, 그렇다면 위의 조건문에서 return 당함

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

        String daySum = checkInValidator.analysisCheck(analysis, today); //누적 시간 계산

        List<Analysis> allUserList = analysisRepository.findByUser(user);

        String total = checkInValidator.totalTime(allUserList); //총 누적 공부시간

        for (CheckIn check : findCheckIn) {
            CheckInListDto.TodayLogDto todayLogDto = CheckInListDto.TodayLogDto.builder()
                    .checkIn(check.getCheckIn())
                    .checkOut(check.getCheckOut())
                    .build();
            todayLogDtoList.add(todayLogDto);
        }

        //체크인, 체크아웃 기록된 세트가 1회 이상 있는 경우
        if (findCheckIn.get(findCheckIn.size() - 1).getCheckOut() != null) {
            String todaySum = analysis.get().getDaySum();
            CheckInListDto.CheckInDto checkInDto = CheckInListDto.CheckInDto.builder()
                    .daySumTime(todaySum)
                    .totalSumTime(total)
                    .todayLog(todayLogDtoList)
                    .build();
            return checkInDto;
        }

        //현재 시간 + 누적 시간
        CheckInListDto.CheckInDto checkInDto = CheckInListDto.CheckInDto.builder()
                .daySumTime(daySum)
                .totalSumTime(total)
                .todayLog(todayLogDtoList)
                .build();

        return checkInDto;
    }

    //[POST]체크아웃
    @Transactional
    public CheckInListDto.CheckInDto checkOut(UserDetailsImpl userDetails) throws ParseException {

        User user = validator.userInfo(userDetails);   // 유저 정보를 찾음(로그인 하지 않았다면 에러 뜰 것)

        String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString(); // 현재 서울 날짜

        Calendar setDay = checkInValidator.todayCalender(date); //오늘 기준 캘린더
        checkInValidator.setCalendarTime(setDay); // yyyy-MM-dd 05:00:00(당일 오전 5시) 캘린더에 적용

        Calendar today = checkInValidator.todayCalender(date); //현재 시간 기준 날짜
        checkInValidator.todayCalendarTime(today); // String yyyy-MM-dd HH:mm:ss 현재시간

        ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

        if (today.compareTo(setDay) < 0) {
            today.add(Calendar.DATE, -1); //오전 5시보다 과거라면, 현재 날짜에서 -1
        }

        String setToday = checkInValidator.DateFormat(today); //Date -> String 변환

        List<CheckIn> findCheckIns = checkInRepository.findByUserAndDate(user, setToday);
        CheckIn lastCheckIn = findCheckIns.get(findCheckIns.size() - 1); //마지막번째 기록을 get

        Optional<Analysis> findAnalysis = analysisRepository.findByUserAndDate(user, setToday);

        String daySum = getCheckIn(userDetails).getDaySumTime(); //총 공부시간

        if (lastCheckIn.getCheckOut() != null) {
            throw new NullPointerException("Start 를 먼저 눌러주세요");
        }

        if (findAnalysis.isPresent()) {
            lastCheckIn.setCheckOut(nowSeoul.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            lastCheckIn.setAnalysis(findCheckIns.get(0).getAnalysis());
            findAnalysis.get().setDaySum(daySum); // 총 공부 시간
            return getCheckIn(userDetails);
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

        return getCheckIn(userDetails);
    }

    //해당 주차의 모든 유저들의 기록
    public List<CheckInListDto.TeamListDto> getAllCheckList(UserDetailsImpl userDetails, String week) throws ParseException {

        validator.loginCheck(userDetails); // 로그인 여부 확인

        String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString(); // 현재 서울 날짜

        Calendar setDay = checkInValidator.todayCalender(date); //오늘 기준 캘린더
        checkInValidator.setCalendarTime(setDay); // yyyy-MM-dd 05:00:00(당일 오전 5시) 캘린더에 적용

        Calendar today = checkInValidator.todayCalender(date); //현재 시간 기준 날짜
        checkInValidator.todayCalendarTime(today); // String yyyy-MM-dd HH:mm:ss 현재시간

        if (today.compareTo(setDay) < 0) {
            today.add(Calendar.DATE, -1); //오전 5시보다 과거라면, 현재 날짜에서 -1
        }

        String setToday = checkInValidator.DateFormat(today); //Date -> String 변환

        List<WeekTeam> weekTeams = weekTeamRepository.findByWeek(week); // 해당 주차 팀 찾기
        List<CheckInListDto.TeamListDto> teamListDtos = new ArrayList<>(); //최종 return 값이 담길 곳

        for (WeekTeam weekTeam : weekTeams) {
            List<CheckInListDto.UserDto> userDtos = new ArrayList<>();

            for (Member member : weekTeam.getMemberList()) {
                List<CheckIn> findCheckIns = checkInRepository.findByUserAndDate(member.getUser(), setToday);

                boolean online = checkInValidator.onlineCheck(findCheckIns); //온라인 여부
                boolean lateCheck = checkInValidator.lateCheck(findCheckIns); //지각 여부

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
