package com.RoutineGongJakSo.BE.security.validator;

import com.RoutineGongJakSo.BE.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Validator {

    //유저 아이디 중복확인
    public static void checkUser(Optional<User> found) {
        if (found.isPresent()) {
            throw new NullPointerException("아이디가 중복됩니다.");
        }
    }

    //관리자 접근 권한 확인
    public static void adminCheck(User user) {
        if (user.getUserLevel() < 5) {
            throw new NullPointerException("접근 권한이 없습니다.");
        }
    }
}
