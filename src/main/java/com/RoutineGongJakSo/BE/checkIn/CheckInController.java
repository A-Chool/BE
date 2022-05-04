package com.RoutineGongJakSo.BE.checkIn;

import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CheckInController {

    private final CheckInService checkInService;

    //체크인 Start
    @PostMapping("/checkIn")
    public String checkIn(@AuthenticationPrincipal UserDetailsImpl userDetails) {
       return checkInService.checkIn(userDetails);
    }

    //if 로그인된 사용자가 이미 start를 누른 상태라면, 값을 내려준다.
    @GetMapping("/checkIn")
    public void getCheckIn(@AuthenticationPrincipal UserDetailsImpl userDetails) throws ParseException {
        checkInService.getCheckIn(userDetails);
    }
}
