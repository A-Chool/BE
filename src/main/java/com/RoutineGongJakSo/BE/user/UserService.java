package com.RoutineGongJakSo.BE.user;

import com.RoutineGongJakSo.BE.model.User;
import com.RoutineGongJakSo.BE.repository.UserRepository;
import com.RoutineGongJakSo.BE.security.validator.ErrorResult;
import com.RoutineGongJakSo.BE.security.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public ErrorResult join(JoinDto joinDto) {

        // Repository에서 Optional 타입으로 찾는다.
        Optional<User> found = userRepository.findByUserEmail(joinDto.getEmail());

        // 아이디 중복 검사, 비밀번호 확인 검사
        UserValidator.checkUser(found, joinDto);

        String userEmail = joinDto.getEmail();
        String userName = joinDto.getUserName();
        String userPw = passwordEncoder.encode(joinDto.getUserPw());
        String phoneNumber = joinDto.getPhoneNumber();

        User user = User.builder()
                .userEmail(userEmail)
                .userName(userName)
                .userPw(userPw)
                .phoneNumber(phoneNumber)
                .build();

        userRepository.save(user);

        return new ErrorResult(true, "회원가입 완료!");
    }
}
