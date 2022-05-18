package com.RoutineGongJakSo.BE.admin.week;

import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.exception.StatusResponseDto;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class WeekController {
    private final WeekService weekService;
    private final Validator validator;

    // 모든 주차 조회
    @GetMapping("/api/admin/week")
    public ResponseEntity<Object> getAllWeek(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        log.info("GET /admin/admin/week");
        //로그인 여부 확인
        validator.loginCheck(userDetails);

        //접근권한 확인
        validator.adminCheck(userDetails);

        List<WeekDto.ResponseDto> weekList = weekService.getAllWeek();
        log.info("GET /admin/admin/week weekList " + weekList);
        return new ResponseEntity<>(new StatusResponseDto("주차 전체 리스트 조회 성공", weekList), HttpStatus.OK);
    }

    @PostMapping("/api/admin/week")
    public ResponseEntity<Object> createWeek(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody WeekDto.RequestDto weekName) {

        log.info("POST /admin/admin/week");
        //로그인 여부 확인
        validator.loginCheck(userDetails);

        //접근권한 확인
        validator.adminCheck(userDetails);

        WeekDto.ResponseDto week = weekService.createWeek(weekName);

        log.info("POST /admin/admin/week week " + week);
        return new ResponseEntity<>(new StatusResponseDto("주차 추가 성공", week), HttpStatus.OK);
    }

    @DeleteMapping("/api/admin/week/{weekId}")
    public ResponseEntity<Object> deleteWeek(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long weekId) {

        log.info("Delete /admin/admin/week " + weekId);
        //로그인 여부 확인
        validator.loginCheck(userDetails);

        //접근권한 확인
        validator.adminCheck(userDetails);

        weekService.deleteWeek(weekId);

        log.info("Delete /admin/admin/week " + weekId);
        return new ResponseEntity<>(new StatusResponseDto("주차 삭제 성공", weekId), HttpStatus.OK);
    }

    @PostMapping("/api/admin/week/{weekId}")
    public ResponseEntity<Object> displayWeek(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long weekId) {

        log.info("POST /admin/admin/week/" + weekId);
        //로그인 여부 확인
        validator.loginCheck(userDetails);

        //접근권한 확인
        validator.adminCheck(userDetails);

        WeekDto.ResponseDto week = weekService.displayWeek(weekId);

        log.info("Delete /admin/admin/" + weekId);
        return new ResponseEntity<>(new StatusResponseDto("주차 display 성공", week), HttpStatus.OK);
    }

    @PutMapping("/api/admin/week/{weekId}")
    public ResponseEntity<Object> changeWeekName(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long weekId, @RequestBody WeekDto.RequestDto weekName) {

        log.info("PUT /admin/admin/week/" + weekId);
        //로그인 여부 확인
        validator.loginCheck(userDetails);

        //접근권한 확인
        validator.adminCheck(userDetails);

        WeekDto.ResponseDto week = weekService.changeWeekName(weekId, weekName);

        log.info("Delete /admin/admin/" + weekId);
        return new ResponseEntity<>(new StatusResponseDto("주차 display 성공", week), HttpStatus.OK);

    }

}
