package com.RoutineGongJakSo.BE.security.validator;

import com.RoutineGongJakSo.BE.model.User;
import com.RoutineGongJakSo.BE.model.WeekTeam;
import com.RoutineGongJakSo.BE.repository.UserRepository;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.exception.UserException;
import com.RoutineGongJakSo.BE.security.exception.UserExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Validator {

    private final UserRepository userRepository;

    //유저 아이디 중복확인
    public static void checkUser(Optional<User> found) {
        if (found.isPresent()) {
            throw new UserException(UserExceptionType.ALREADY_EXIST_USERNAME);
        }
    }

    //관리자 접근 권한 확인
    public void adminCheck(UserDetailsImpl userDetails) {

        User user = userRepository.findByUserEmail(userDetails.getUserEmail())
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_MEMBER));

        if (user.getUserLevel() < 5) {
            throw new UserException(UserExceptionType.LOW_LEVER);
        }
    }

    //로그인 유저 확인
    public void loginCheck(UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new UserException(UserExceptionType.NOT_ONLINE);
        }
    }

    //이미 만들어진 팀이 있는지 확인
    public void teamCheck(Optional<WeekTeam> teamCheck) {
        if (teamCheck.isPresent()) {
            throw new UserException(UserExceptionType.YES_MEMBER);
        }
    }

    // ':' 사용 금지
    public void teamNameCheck(String teamName) {
        if (teamName.contains(":")) {
            throw new UserException(UserExceptionType.NOT_COLUM);
        }
    }

    //유저 정보를 찾음
    public User userInfo(UserDetailsImpl userDetails) {
        User user = userRepository.findByUserEmail(userDetails.getUserEmail()).orElseThrow(
                () -> new UserException(UserExceptionType.NOT_FOUND_MEMBER)
        );
        return user;
    }

    //유저아이디로 유저정보 찾기
    public User findUserIdInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserException(UserExceptionType.NOT_FOUND_MEMBER)
        );
        return user;
    }
}
