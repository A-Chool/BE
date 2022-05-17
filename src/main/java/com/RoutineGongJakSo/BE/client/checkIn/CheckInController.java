package com.RoutineGongJakSo.BE.client.checkIn;

import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CheckInController {

    private final CheckInService checkInService;

    //체크인 Start
    @PostMapping("/checkIn")
    public String checkIn(@AuthenticationPrincipal UserDetailsImpl userDetails) throws ParseException{
       return checkInService.checkIn(userDetails);
    }

    //if 로그인된 사용자가 이미 start를 누른 상태라면, 값을 내려준다.
    @GetMapping("/checkIn")
    public CheckInListDto.CheckInDto getCheckIn(@AuthenticationPrincipal UserDetailsImpl userDetails) throws ParseException {
       return checkInService.getCheckIn(userDetails);
    }

    //체크아웃
    @PostMapping("/checkOut")
    public CheckInListDto.CheckInDto checkOut(@AuthenticationPrincipal UserDetailsImpl userDetails) throws ParseException{
        return checkInService.checkOut(userDetails);
    }

    @GetMapping("/checkInList/{week}")
    public List<CheckInListDto.TeamListDto> getAllCheckList(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable String week) throws ParseException {
       return checkInService.getAllCheckList(userDetails, week);
    }

}