package com.RoutineGongJakSo.BE.admin.admin;

import com.RoutineGongJakSo.BE.client.refreshToken.RefreshToken;
import com.RoutineGongJakSo.BE.client.refreshToken.RefreshTokenRepository;
import com.RoutineGongJakSo.BE.client.user.User;
import com.RoutineGongJakSo.BE.client.user.UserRepository;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.exception.UserException;
import com.RoutineGongJakSo.BE.security.exception.UserExceptionType;
import com.RoutineGongJakSo.BE.security.jwt.JwtTokenUtils;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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
    private final RefreshTokenRepository refreshTokenRepository;

    //관리자 로그인
    @Transactional
    public HttpHeaders login(AdminDto.RequestDto adminDto) {
        User user = userRepository.findByUserEmail(adminDto.getEmail())
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_MEMBER));

        if (!passwordEncoder.matches(adminDto.getPassword(), user.getUserPw())) {
            throw new UserException(UserExceptionType.WRONG_PASSWORD);
        }

        if (user.getUserLevel() < 5) {
            throw new UserException(UserExceptionType.LOW_LEVER);
        }

        //Token -> Headers로 보내기
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "BEARER " + JwtTokenUtils.generateAdminJwtToken(user.getUserName(), user.getUserEmail(), user.getUserLevel()));
        headers.add("RefreshAuthorization", "BEARER " + JwtTokenUtils.generateRefreshToken());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RefreshToken findToken = refreshTokenRepository.findByUserEmail(user.getUserEmail());

        if (findToken != null){
            findToken.setRefreshToken(JwtTokenUtils.generateRefreshToken());
            log.info("액세스 토큰 {}, 리프레쉬 토큰 {}", headers.get("Authorization"), headers.get("RefreshAuthorization"));
            return headers;
        }

        //리프레쉬 토큰을 저장
        //PK = userEmail
        RefreshToken refresh = RefreshToken.builder()
                .refreshToken(JwtTokenUtils.generateRefreshToken())
                .userEmail(user.getUserEmail())
                .build();

        refreshTokenRepository.save(refresh);

        log.info("액세스 토큰 {}, 리프레쉬 토큰 {}", headers.get("Authorization"), headers.get("RefreshAuthorization"));
        return headers;
    }

    //전체 유저 조회
    public List<AdminDto.ResponseDto> getAllUser(UserDetailsImpl userDetails) {

        //로그인 여부 확인, 접근권한 확인
        validator.adminCheck(userDetails);

        List<User> users = userRepository.findAll();

        List<AdminDto.ResponseDto> responseDtos = new ArrayList<>();

        for (User user : users) {
            AdminDto.ResponseDto findDto = AdminDto.ResponseDto.builder()
                    .userId(user.getUserId())
                    .userEmail(user.getUserEmail())
                    .userName(user.getUserName())
                    .phoneNumber(user.getPhoneNumber())
                    .userLevel(user.getUserLevel())
                    .kakaoId(user.getKakaoId())
                    .naverId(user.getNaverId())
                    .createdAt(user.getCreatedAt())
                    .build();
            responseDtos.add(findDto);
        }

        log.info("전체 유저 조회 {}", responseDtos);

        return responseDtos;
    }

    //권한 변경
    @Transactional
    public String updateLevel(Long userId, AdminDto.UpdateDto update, UserDetailsImpl userDetails) {

        //로그인 여부 확인, 접근권한 확인
        User user = validator.adminCheck(userDetails);

        user.setUserLevel(update.getUserLevel());

        log.info("권한 변경 유저 {}", user);

        return "권한이 수정 되었습니다.";
    }

    //유저 삭제
    @Transactional
    public String deleteUser(Long userId, UserDetailsImpl userDetails) {

        //로그인 여부 확인, 접근권한 확인
        validator.adminCheck(userDetails);
        //유저아이디로 유저정보 찾기
        User user = validator.findUserIdInfo(userId);

        userRepository.delete(user);

        log.info("확인 삭제된 유저 {}", user);

        return "해당 유저 정보가 삭제되었습니다.";
    }
}