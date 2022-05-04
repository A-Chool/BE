package com.RoutineGongJakSo.BE.checkIn;

import com.RoutineGongJakSo.BE.checkIn.dto.CheckInDto;
import com.RoutineGongJakSo.BE.model.CheckIn;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

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
    public String getCheckIn(@AuthenticationPrincipal UserDetailsImpl userDetails) throws ParseException {
       return checkInService.getCheckIn(userDetails);
    }

    @PostMapping("/checkOut")
    public String checkOut(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CheckInDto.requestDto requestDto) throws ParseException{
        return checkInService.checkOut(userDetails, requestDto);
    }
}
