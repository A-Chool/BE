package com.RoutineGongJakSo.BE.sse;

import com.RoutineGongJakSo.BE.client.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupEvent {
    private User user;
    private Boolean checkIn;
}
