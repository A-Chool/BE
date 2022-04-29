package com.RoutineGongJakSo.BE.security.validator;

import com.RoutineGongJakSo.BE.user.JoinDto;
import com.RoutineGongJakSo.BE.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class UserValidator {

    public static void checkUser(Optional<User> found, JoinDto joinDto) {
        Matcher match = Pattern.compile("(\\w)\\1\\1").matcher(joinDto.getUserPw());
//        if(match.find()){
//            throw new NullPointerException("비밀번호는 중복된 글자가 3개 미만이어야 합니다.");
//        }
        if(found.isPresent()){
            throw new NullPointerException("아이디가 중복됩니다.");
        }
//        if(!joinDto.getUserPwCheck().equals(joinDto.getUserPw())){
//            throw new NullPointerException("비밀번호 확인이 다릅니다.");
//        }
    }
}
