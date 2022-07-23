package com.RoutineGongJakSo.BE.validator;

import com.RoutineGongJakSo.BE.admin.admin.AdminDto;
import com.RoutineGongJakSo.BE.client.user.User;
import com.RoutineGongJakSo.BE.client.user.UserRepository;
import com.RoutineGongJakSo.BE.security.exception.UserException;
import com.RoutineGongJakSo.BE.security.exception.UserExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;


@RequiredArgsConstructor
public class AdminCheckValidator {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //유저 인증
    public User userAuthentication(String email) {
        return userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_MEMBER));
    }

    //admin 권한 체크
    public static void adminAuthorization(int level) {
        if (level < 5) {
            throw new UserException(UserExceptionType.LOW_LEVER);
        }
    }

    //passwordCheck
    public void passwordMatches(String password, String checkPW) {
        if (!passwordEncoder.matches(checkPW, password)) {
            throw new UserException(UserExceptionType.WRONG_PASSWORD);
        }
    }

}
