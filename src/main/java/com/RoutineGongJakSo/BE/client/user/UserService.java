package com.RoutineGongJakSo.BE.client.user;

import com.RoutineGongJakSo.BE.exception.CustomException;
import com.RoutineGongJakSo.BE.security.jwt.JwtTokenUtils;
import com.RoutineGongJakSo.BE.security.validator.ErrorResult;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.RoutineGongJakSo.BE.exception.ErrorCode.NOT_FOUND_USER_ID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Value("${logging.slack.webhook-uri}")
    private String slackUrl;

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
        joinAlert(user);
        return new ErrorResult(true, "회원가입 완료!");
    }

    public void joinAlert(User user) {
        log.info("joinAlert");
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type", "application/json; charset=utf-8");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", user.getUserName() + " 신규회원 가입 " + new DateTime());
        String body = jsonObject.toString();

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.exchange(slackUrl, HttpMethod.POST, requestEntity, String.class);

        HttpStatus httpStatus = responseEntity.getStatusCode();
        int status = httpStatus.value();
        String response = responseEntity.getBody();
    }

    @Transactional
    public Map<String, String> bodyToken(BodyTokenDto bodyTokenDto) {
        User user = userRepository.findByUserEmail(bodyTokenDto.getEmail()).orElseThrow(
                () -> new CustomException(NOT_FOUND_USER_ID)
        );

        if (!passwordEncoder.matches(bodyTokenDto.getPassword(), user.getUserPw())) {
            throw new CustomException(NOT_FOUND_USER_ID);
        }

        Map<String, String> result = new HashMap<>();

        result.put("Authorization", JwtTokenUtils.generateAdminJwtToken(user.getUserName(), user.getUserEmail(), user.getUserLevel()));
        result.put("RefreshToken", JwtTokenUtils.generateRefreshToken());

        log.info("바디토큰{}", result);

        return result;
    }
}
