package com.RoutineGongJakSo.BE.client.checkIn;

import com.RoutineGongJakSo.BE.admin.member.Member;
import com.RoutineGongJakSo.BE.admin.team.Team;
import com.RoutineGongJakSo.BE.admin.team.TeamRepository;
import com.RoutineGongJakSo.BE.admin.week.Week;
import com.RoutineGongJakSo.BE.admin.week.WeekRepository;
import com.RoutineGongJakSo.BE.client.analysis.model.Analysis;
import com.RoutineGongJakSo.BE.client.analysis.repository.AnalysisRepository;
import com.RoutineGongJakSo.BE.client.checkIn.model.CheckIn;
import com.RoutineGongJakSo.BE.client.checkIn.repository.CheckInRepository;
import com.RoutineGongJakSo.BE.client.user.User;
import com.RoutineGongJakSo.BE.exception.CustomException;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.exception.UserException;
import com.RoutineGongJakSo.BE.security.exception.UserExceptionType;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.RoutineGongJakSo.BE.util.CalendarUtil.*;
import static com.RoutineGongJakSo.BE.exception.ErrorCode.*;
import static com.RoutineGongJakSo.BE.util.Formatter.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckInService {

    private final CheckInRepository checkInRepository;
    private final AnalysisRepository analysisRepository;
    private final TeamRepository teamRepository;
    private final WeekRepository weekRepository;
    private final Validator validator;

    //[POST]체크인
    @Transactional
    public String checkIn(UserDetailsImpl userDetails) throws ParseException {

        User user = validator.userInfo(userDetails);// 유저 정보를 찾음(로그인 하지 않았다면 에러 뜰 것)

        String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString(); // 현재 서울 날짜

        Calendar setDay = todayCalender(date); //오늘 기준 캘린더
        setCalendarTime(setDay); // yyyy-MM-dd 05:00:00(당일 오전 5시) 캘린더에 적용

        Calendar today = todayCalender(date); //현재 시간 기준 날짜
        todayCalendarTime(today); // String yyyy-MM-dd HH:mm:ss 현재시간

        //compareTo() > 0 : 인자보다 미래
        //compareTo() < 0 : 인자보다 과거
        //compareTo() == 0 : 인자와 같은 시간
        if (today.compareTo(setDay) < 0) {
            today.add(Calendar.DATE, -1); //오전 5시보다 과거라면, 현재 날짜에서 -1
        }

        String setToday = DateFormat(today); //Date -> String 변환

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

        String nowTime = nowTime(); //현재 서울 시간
        CheckIn checkIn = CheckIn.builder()
                .user(user)
                .date(setToday)
                .checkIn(nowTime)
                .build();
        checkInRepository.save(checkIn);

        log.info("체크인 {}", response);

        return response;
    }

    //[GET]사용자가 이미 start를 누른 상태라면, 값을 내려주는 곳(당일)
    public CheckInListDto.CheckInDto getCheckIn(UserDetailsImpl userDetails) throws ParseException {

        User user = validator.userInfo(userDetails); // 유저 정보를 찾음(로그인 하지 않았다면 에러 뜰 것)

        String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString(); // 현재 서울 날짜

        Calendar setDay = todayCalender(date); //오늘 기준 캘린더
        setCalendarTime(setDay); // yyyy-MM-dd 05:00:00(당일 오전 5시) 캘린더에 적용

        Calendar today = todayCalender(date); //현재 시간 기준 날짜
        todayCalendarTime(today); // String yyyy-MM-dd HH:mm:ss 현재시간

        if (today.compareTo(setDay) < 0) {
            today.add(Calendar.DATE, -1); //오전 5시보다 과거라면, 현재 날짜에서 -1
        }

        String setToday = DateFormat(today); //Date -> String 변환

        Optional<Analysis> analysis = analysisRepository.findByUserAndDate(user, setToday);

        List<CheckIn> findCheckIn = checkInRepository.findByUserAndDate(user, setToday);

        List<CheckInListDto.TodayLogDto> todayLogDtoList = new ArrayList<>(); // 그 날의 로그 기록

        if (findCheckIn.size() == 0) { //기록이 없는 경우

            List<Analysis> allUserList = analysisRepository.findByUser(user);

            String total = totalTime(allUserList); //총 누적 공부시간

            CheckInListDto.CheckInDto checkInDto = CheckInListDto.CheckInDto.builder()
                    .daySumTime("00:00:00")
                    .totalSumTime(total)
                    .todayLog(todayLogDtoList)
                    .build();

            log.info("체크인 기록이 없음 {}", checkInDto);

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

        String daySum = analysisCheck(analysis, today); //누적 시간 계산

        List<Analysis> allUserList = analysisRepository.findByUser(user);

        String total = totalTime(allUserList); //총 누적 공부시간

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

            log.info("체크인 기록이 1회 이상 있음 {}", checkInDto);
            return checkInDto;
        }

        //현재 시간 + 누적 시간
        CheckInListDto.CheckInDto checkInDto = CheckInListDto.CheckInDto.builder()
                .daySumTime(daySum)
                .totalSumTime(total)
                .todayLog(todayLogDtoList)
                .build();

        log.info("체크인 기록이 있음 {}", checkInDto);

        return checkInDto;
    }

    //[POST]체크아웃
    @Transactional
    public CheckInListDto.CheckInDto checkOut(UserDetailsImpl userDetails) throws ParseException {

        User user = validator.userInfo(userDetails);   // 유저 정보를 찾음(로그인 하지 않았다면 에러 뜰 것)

        String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString(); // 현재 서울 날짜

        Calendar setDay = todayCalender(date); //오늘 기준 캘린더
        setCalendarTime(setDay); // yyyy-MM-dd 05:00:00(당일 오전 5시) 캘린더에 적용

        Calendar today = todayCalender(date); //현재 시간 기준 날짜
        todayCalendarTime(today); // String yyyy-MM-dd HH:mm:ss 현재시간

        ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

        if (today.compareTo(setDay) < 0) {
            today.add(Calendar.DATE, -1); //오전 5시보다 과거라면, 현재 날짜에서 -1
        }

        String setToday = DateFormat(today); //Date -> String 변환

        List<CheckIn> findCheckIns = checkInRepository.findByUserAndDate(user, setToday);
        CheckIn lastCheckIn = findCheckIns.get(findCheckIns.size() - 1); //마지막번째 기록을 get

        Optional<Analysis> findAnalysis = analysisRepository.findByUserAndDate(user, setToday);

        String daySum = getCheckIn(userDetails).getDaySumTime(); //총 공부시간

        if (lastCheckIn.getCheckOut() != null) {
            throw new CustomException(TRY_START);
        }

        if (findAnalysis.isPresent()) {
            lastCheckIn.setCheckOut(nowSeoul.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            lastCheckIn.setAnalysis(findCheckIns.get(0).getAnalysis());
            findAnalysis.get().setDaySum(daySum); // 총 공부 시간

            log.info("체크아웃 {}", getCheckIn(userDetails));

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

        log.info("체크아웃 {}", getCheckIn(userDetails));

        return getCheckIn(userDetails);
    }

    //해당 주차의 모든 유저들의 기록
    public List<CheckInListDto.TeamListDto> getAllCheckList(UserDetailsImpl userDetails) throws ParseException {

        validator.loginCheck(userDetails); // 로그인 여부 확인

        String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString(); // 현재 서울 날짜

        Calendar setDay = todayCalender(date); //오늘 기준 캘린더
        setCalendarTime(setDay); // yyyy-MM-dd 05:00:00(당일 오전 5시) 캘린더에 적용

        Calendar today = todayCalender(date); //현재 시간 기준 날짜
        todayCalendarTime(today); // String yyyy-MM-dd HH:mm:ss 현재시간

        if (today.compareTo(setDay) < 0) {
            today.add(Calendar.DATE, -1); //오전 5시보다 과거라면, 현재 날짜에서 -1
        }

        String setToday = DateFormat(today); //Date -> String 변환

        // display 한 주차 찾기
        Week week = weekRepository.findByDisplay(true).orElseThrow(
                ()-> new CustomException(NO_WEEK)
        );
        List<Team> teamList = teamRepository.findByWeek(week);

//        List<WeekTeam> weekTeams = weekTeamRepository.findByWeek(week); // 해당 주차 팀 찾기
        List<CheckInListDto.TeamListDto> teamListDtos = new ArrayList<>(); //최종 return 값이 담길 곳

        for (Team team : teamList) {
            List<CheckInListDto.UserDto> userDtos = new ArrayList<>();

            for (Member member : team.getMemberList()) {
                List<CheckIn> findCheckIns = checkInRepository.findByUserAndDate(member.getUser(), setToday);

                boolean online = onlineCheck(findCheckIns); //온라인 여부
                boolean lateCheck = lateCheck(findCheckIns); //지각 여부

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
                    .teamId(team.getTeamId())
                    .teamName(team.getTeamName())
                    .weekId(team.getWeek().getWeekId())
                    .weekName(team.getWeek().getWeekName())
                    .memberList(userDtos)
                    .build();

            teamListDtos.add(teamListDto);
        }
        log.info("해당 주차 모든 유저의 기록 {}", teamListDtos);
        return teamListDtos;
    }

    //String 현재시간
    private String nowTime() {
        ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        return nowSeoul.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    private boolean onlineCheck(List<CheckIn> findCheckIns) {
        if (findCheckIns.size() == 0 || findCheckIns.get(findCheckIns.size() - 1).getCheckOut() != null) {
            return false;
        }
        return true;
    }

    //지각 여부 확인
    private boolean lateCheck(List<CheckIn> findCheckIns) {
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

    // 사용자가 1회 이상 체크인 체크아웃 완성된 행이 있을 경우 시간 계산
    private String analysisCheck(Optional<Analysis> analysis, Calendar today) throws ParseException {
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
}