package com.RoutineGongJakSo.BE.checkIn;

import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CheckInController {

    private final CheckInService checkInService;

    //체크인
    @PostMapping("/checkIn")
    public String checkIn(@AuthenticationPrincipal UserDetailsImpl userDetails) {
       return checkInService.checkIn(userDetails);
    }
}
