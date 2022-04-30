package com.RoutineGongJakSo.BE.security.validator;

import com.RoutineGongJakSo.BE.model.User;
import com.RoutineGongJakSo.BE.model.WeekTeam;
import com.RoutineGongJakSo.BE.repository.UserRepository;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Validator {

    private final UserRepository userRepository;

    //유저 아이디 중복확인
    public static void checkUser(Optional<User> found) {
        if (found.isPresent()) {
            throw new NullPointerException("아이디가 중복됩니다.");
        }
    }

    //관리자 접근 권한 확인
    public void adminCheck(UserDetailsImpl userDetails) {

        User user = userRepository.findByUserEmail(userDetails.getUserEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 userId 입니다."));

        if (user.getUserLevel() < 5) {
            throw new NullPointerException("접근 권한이 없습니다.");
        }
    }

    //로그인 유저 확인
    public void loginCheck(UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new NullPointerException("로그인 된 사용자가 아닙니다.");
        }
    }

    //이미 만들어진 팀이 있는지 확인
    public void teamCheck(Optional<WeekTeam> teamCheck){
        if (teamCheck.isPresent()){
            throw new NullPointerException("이미 만들어진 팀이 존재합니다.");
        }

    }
}
