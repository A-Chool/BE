package com.RoutineGongJakSo.BE.checkIn;

import com.RoutineGongJakSo.BE.checkIn.repository.CheckInRepository;
import com.RoutineGongJakSo.BE.model.CheckIn;
import com.RoutineGongJakSo.BE.model.User;
import com.RoutineGongJakSo.BE.repository.UserRepository;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckInService {

    private final UserRepository userRepository;
    private final CheckInRepository checkInRepository;
    private final Validator validator;

    //체크인
    @Transactional
    public String checkIn(UserDetailsImpl userDetails) {
        // 로그인 여부 확인
        validator.loginCheck(userDetails);

        User user = userRepository.findByUserEmail(userDetails.getUserEmail()).orElseThrow(
                () -> new NullPointerException("해당 사용자가 존재하지 않습니다.")
        );

        List<CheckIn> checkInList = checkInRepository.findByUser(user);

        for (CheckIn check : checkInList) {
            if (check.getCheckOut() == null) {
                throw new NullPointerException("체크아웃을 먼저 해주세요");
            }
        }
        //현재 서울 날짜
        String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString();
        //현재 시간
        ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

        log.info("현재 서울 날짜 : " + date);
        log.info("현재 서울 시간 : " + nowSeoul);

        CheckIn checkIn = CheckIn.builder()
                .user(user)
                .date(date)
                .checkIn(nowSeoul.format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .build();

        checkInRepository.save(checkIn);

        return "Start!";
    }

    //사용자가 이미 start를 누른 상태라면, 값을 내려주는 곳(당일)
    public String getCheckIn(UserDetailsImpl userDetails) throws ParseException {
        // 로그인 여부 확인
        validator.loginCheck(userDetails);

        User user = userRepository.findByUserEmail(userDetails.getUserEmail()).orElseThrow(
                () -> new NullPointerException("해당 사용자가 존재하지 않습니다.")
        );
        //오늘 날짜
        String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString();
        //해당 유저의 해당 날짜의 전체 기록을 찾기
        List<CheckIn> checkInList = checkInRepository.findByUserAndDate(user, date);

        //기록 정보가 없을 때 return
        if (checkInList == null) {
            return "금일 기록된 정보가 없습니다.";
        }

        //마지막 체크인 시간 확인
        CheckIn lastCheckIn = checkInList.get(checkInList.size() - 1);
        String[] test = lastCheckIn.getCheckIn().split(":");
        log.info("나눈다" + Arrays.deepToString(test));

        for (int i = 0; i < test.length; i++){
            int test1 =  Integer.parseInt(test[i]);
        }

        //마지막 기록의 체크아웃 값이 있는지 확인
        if (lastCheckIn.getCheckOut() != null) {
            throw new NullPointerException("아직 Start 를 누르지 않았습니다.");
        }

        //시간을 예쁘게 만들기
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        SimpleDateFormat calenderFormatter = new SimpleDateFormat("HH:mm:ss");
        //체크인 시 기록된 시간, 날짜
        Date timeFormatter = formatter.parse(lastCheckIn.getDate() + " " + lastCheckIn.getCheckIn());
        log.info("마지막으로 체크인된 시간 : " + timeFormatter);

        //현재 시간을 구하고
        ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        log.info("현재 년 : " + nowSeoul.getYear());
        log.info("현재 월 " + nowSeoul.getMonthValue());
        log.info("현재 일 " + nowSeoul.getDayOfMonth());
        log.info("현재 시간 : " + nowSeoul);

        //Date 포맷을 위해 년, 월, 일을 하나씩 적출해서 string 으로 변환하고
        String nowYear = String.valueOf(nowSeoul.getYear());
        String nowMonth = String.valueOf(nowSeoul.getMonthValue());
        String nowDay = String.valueOf(nowSeoul.getDayOfMonth());
        log.info("현재 y : " + nowYear);
        log.info("현재 m " + nowMonth);
        log.info("현재 d " + nowDay);

        //현재 시간을 스트링으로 변환하고
        String nowTime = nowSeoul.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        log.info("가공된 시간 : " + nowTime);
        String sumDateTime = nowYear+ "-" + nowMonth+ "-" + nowDay+ " " + nowTime;
        log.info("믿는다 : " + sumDateTime);

        //Date 형식으로 포맷
        Date nowFormatter = formatter.parse(sumDateTime);
        log.info("Date 형식으로 포맷된 현재시간 : " + nowFormatter);


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowFormatter);

//        calendar.add(Calendar.HOUR, -timeFormatter);
        calendar.add(Calendar.MINUTE, -nowSeoul.getMinute());
        calendar.add(Calendar.SECOND, -nowSeoul.getSecond());

        log.info("최종 계산 시간 : " + calenderFormatter.format(calendar.getTime()));


        //solution 1. split 으로 : 단위로 쪼개기 -> 각 파트마다 초로 환산하기 -> 현재 시간이랑 빼기 -> 다시포시,분,초로 변환 -> string -> return;
        //solution 2. 날짜까지 통째로 저장하 -> timeZone 변환 -> 초 단위로 계산

        //1. 문자열 -> 시간으로 포맷
        //2. 시간 -> ss로 환산
        //3. 현재시간 -> ss로 환산
        //4. 3 -2 -> 결과값을 HH-mm-ss 로 변환
        //5. return;


        // 찍힌 시간에서 현재 시간 빼서 리턴
        // 밀리 초 단위로 계산해서 시간 형식으로 포맷해서 리턴(시간 양식 지키기)
        return "정상 작동";
    }
}
