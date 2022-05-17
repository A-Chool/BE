package com.RoutineGongJakSo.BE.admin.week;

import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.exception.StatusResponseDto;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class WeekController {
    private final WeekRepository weekRepository;
    private final WeekService weekService;
    private final Validator validator;

    @GetMapping("/api/admin/week")
    public ResponseEntity<Object> getAllWeek(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        log.info("/admin/admin/week");
        //로그인 여부 확인
        validator.loginCheck(userDetails);

        //접근권한 확인
        validator.adminCheck(userDetails);

        List<WeekDto.ResponseDto> weekList = weekService.getAllWeek();
        log.info("/admin/admin/week weekList " + weekList);
        return new ResponseEntity<>(new StatusResponseDto("주차 전체 리스트 조회 성공", weekList), HttpStatus.OK);
    }
}
