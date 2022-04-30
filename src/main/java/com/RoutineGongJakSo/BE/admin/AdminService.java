package com.RoutineGongJakSo.BE.admin;

import com.RoutineGongJakSo.BE.model.User;
import com.RoutineGongJakSo.BE.repository.UserRepository;
import com.RoutineGongJakSo.BE.security.jwt.JwtTokenUtils;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final PasswordEncoder passwordEncoder;

    //관리자 로그인
    @Transactional
    public HttpHeaders login(AdminDto.RequestDto adminDto) {
        User user = userRepository.findByUserEmail(adminDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 userId 입니다."));

        if (!passwordEncoder.matches(adminDto.getPassword(), user.getUserPw())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        //접근권한 확인
        Validator.adminCheck(user);

        //Token -> Headers로 보내기
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtTokenUtils.generateAdminJwtToken(user.getUserName(), user.getUserEmail(), user.getUserLevel()));
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        System.out.println("Headers 토큰값 확인: " + headers.get("Authorization"));

        return headers;
    }

    //전체 유저 조회
    public List<AdminDto.ResponseDto> getAllUser() {

//        User find = userRepository.findByUserEmail(userDetails.getUserEmail())
//                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 userId 입니다."));
//
//        if (userDetails == null) {
//            throw new NullPointerException("관리자만 접근 할 수 있습니다.");
//        }
//
//        //접근권한 확인
//        Validator.adminCheck(find);

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
                    .build();
            responseDtos.add(findDto);
        }

        return responseDtos;
    }

    //권한 변경
    @Transactional
    public String updateLevel(Long userId, AdminDto.UpdateDto update) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException("존재하지 않는 유저입니다.")
        );
        user.setUserLevel(update.getUserLevel());

        return "권한이 수정 되었습니다.";
    }

    //유저 삭제
    @Transactional
    public String deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException("존재하지 않는 유저입니다.")
        );
        userRepository.delete(user);
        return "해당 유저 정보가 삭제되었습니다.";
    }
}
