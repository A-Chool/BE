package com.RoutineGongJakSo.BE.client.user;

import com.RoutineGongJakSo.BE.security.validator.ErrorResult;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import com.RoutineGongJakSo.BE.util.SlackAlert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SlackAlert slackAlert;


    @Transactional
    public ErrorResult join(JoinDto joinDto) {
        log.info("join");
        // Repository에서 Optional 타입으로 찾는다.
        Optional<User> found = userRepository.findByUserEmail(joinDto.getEmail());

        // 아이디 중복 검사, 비밀번호 확인 검사
        Validator.checkUser(found);

        String userEmail = joinDto.getEmail();
        String userName = joinDto.getUserName();
        String userPw = passwordEncoder.encode(joinDto.getUserPw());
        String phoneNumber = joinDto.getPhoneNumber();

        log.info("userEmail : {}", userEmail);
        log.info("userName : {}", userName);
        log.info("userPw : {}", userPw);
        log.info("phoneNumber : {}", phoneNumber);

        User user = User.builder()
                .userEmail(userEmail)
                .userName(userName)
                .userPw(userPw)
                .phoneNumber(phoneNumber)
                .build();

        userRepository.save(user);
        slackAlert.joinAlert(user);
        return new ErrorResult(true, "회원가입 완료!");
    }

}
