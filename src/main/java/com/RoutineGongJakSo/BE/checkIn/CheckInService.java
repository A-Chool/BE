package com.RoutineGongJakSo.BE.checkIn;

import com.RoutineGongJakSo.BE.checkIn.repository.CheckInRepository;
import com.RoutineGongJakSo.BE.model.CheckIn;
import com.RoutineGongJakSo.BE.model.User;
import com.RoutineGongJakSo.BE.repository.UserRepository;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
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

      for (CheckIn check : checkInList){
          if (check.getCheckOut() == null){
              throw new NullPointerException("체크아웃을 먼저 해주세요");
          }
      }

        //현재 서울 날짜
        LocalDate date = LocalDate.now(ZoneId.of("Asia/Seoul"));

        //현재 시간
        ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));


        CheckIn checkIn = CheckIn.builder()
                .user(user)
                .date(date)
                .checkIn(nowSeoul.format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .build();

        checkInRepository.save(checkIn);

        return "Start!";
    }
}
