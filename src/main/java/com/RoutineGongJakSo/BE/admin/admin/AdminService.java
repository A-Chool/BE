package com.RoutineGongJakSo.BE.admin.admin;

import com.RoutineGongJakSo.BE.client.refreshToken.RefreshToken;
import com.RoutineGongJakSo.BE.client.refreshToken.RefreshTokenRepository;
import com.RoutineGongJakSo.BE.client.user.User;
import com.RoutineGongJakSo.BE.client.user.UserRepository;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.jwt.JwtTokenUtils;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import com.RoutineGongJakSo.BE.validator.AdminCheckValidator;
import com.RoutineGongJakSo.BE.validator.TokenValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;
    private final AdminCheckValidator adminCheckValidator;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenValidator tokenValidator;

    //관리자 로그인
    @Transactional
    public HttpHeaders login(AdminDto.RequestDto adminDto) {
        User user = adminCheckValidator.userAuthentication(adminDto.getEmail());

        adminCheckValidator.passwordMatches(adminDto.getPassword(), user.getUserPw());
        AdminCheckValidator.adminAuthorization(user.getUserLevel());

        //Token -> Headers로 보내기
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "BEARER " + JwtTokenUtils.generateAdminJwtToken(user.getUserName(), user.getUserEmail(), user.getUserLevel()));
        headers.add("RefreshAuthorization", "BEARER " + JwtTokenUtils.generateRefreshToken());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RefreshToken findToken = tokenValidator.findRefreshToken(user.getUserEmail());

        if (findToken != null){
            findToken.setRefreshToken(JwtTokenUtils.generateRefreshToken());
            log.info("액세스 토큰 {}, 리프레쉬 토큰 {}", headers.get("Authorization"), headers.get("RefreshAuthorization"));
            return headers;
        }

        //리프레쉬 토큰을 저장
        //PK = userEmail
        RefreshToken refresh = new RefreshToken(user);

        refreshTokenRepository.save(refresh);

        log.info("액세스 토큰 {}, 리프레쉬 토큰 {}", headers.get("Authorization"), headers.get("RefreshAuthorization"));
        return headers;
    }

    //전체 유저 조회
    public List<AdminDto.ResponseDto> getAllUser(UserDetailsImpl userDetails) {
        validator.loginCheck(userDetails);
        AdminCheckValidator.adminAuthorization(userDetails.getUserLevel());

        List<User> users = userRepository.findAll();

        List<AdminDto.ResponseDto> responseDtos = new ArrayList<>();

        for (User user : users) {
            AdminDto.ResponseDto findDto = new AdminDto.ResponseDto(user);
        }

        log.info("전체 유저 조회 {}", responseDtos);

        return responseDtos;
    }

    //권한 변경
    @Transactional
    public ResponseEntity<String> updateLevel(Long userId, AdminDto.UpdateDto update, UserDetailsImpl userDetails) {

        AdminCheckValidator.adminAuthorization(userDetails.getUserLevel());

        User getUser = validator.findUserIdInfo(userId);

        getUser = new User(update.getUserLevel());

        userRepository.save(getUser);

        log.info("권한 변경 유저 {}", getUser);

        return ResponseEntity
                .status(200)
                .body("권한이 수정되었습니다.");
    }

    //유저 삭제
    @Transactional
    public ResponseEntity<String> deleteUser(Long userId, UserDetailsImpl userDetails) {

        AdminCheckValidator.adminAuthorization(userDetails.getUserLevel());

        User user = validator.findUserIdInfo(userId);

        userRepository.delete(user);

        log.info("확인 삭제된 유저 {}", user);

        return ResponseEntity
                .status(200)
                .body("유저 정보가 삭제 되었습니다.");

    }
}