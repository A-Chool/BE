package com.RoutineGongJakSo.BE.client.checkIn;

import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CheckInController {

    private final CheckInService checkInService;

    //체크인 Start
    @PostMapping("/checkIn")
    public String checkIn(@AuthenticationPrincipal UserDetailsImpl userDetails) throws ParseException{
        log.info("요청 메서드 [POST] /api/checkIn");
       return checkInService.checkIn(userDetails);
    }

    //if 로그인된 사용자가 이미 start를 누른 상태라면, 값을 내려준다.
    @GetMapping("/checkIn")
    public CheckInListDto.CheckInDto getCheckIn(@AuthenticationPrincipal UserDetailsImpl userDetails) throws ParseException {
        log.info("요청 메서드 [GET] /api/checkIn");
       return checkInService.getCheckIn(userDetails);
    }

    //체크아웃
    @PostMapping("/checkOut")
    public CheckInListDto.CheckInDto checkOut(@AuthenticationPrincipal UserDetailsImpl userDetails) throws ParseException{
        log.info("요청 메서드 [POST] /api/checkOut");
        return checkInService.checkOut(userDetails);
    }

    //유저 로그인 한 후 checkin page 접속시 나가는 것
    @GetMapping("/checkInList")
    public List<CheckInListDto.TeamListDto> getAllCheckList(@AuthenticationPrincipal UserDetailsImpl userDetails) throws ParseException {
        log.info("요청 메서드 [GET] /api/checkInList");
       return checkInService.getAllCheckList(userDetails);
    }

}
