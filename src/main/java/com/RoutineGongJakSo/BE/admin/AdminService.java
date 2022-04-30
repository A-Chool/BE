package com.RoutineGongJakSo.BE.admin;

import com.RoutineGongJakSo.BE.model.User;
import com.RoutineGongJakSo.BE.repository.UserRepository;
import com.RoutineGongJakSo.BE.security.jwt.JwtTokenUtils;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final PasswordEncoder passwordEncoder;

    //관리자 로그인
    @Transactional
    public Map<String, Object> login(AdminDto.RequestDto adminDto) {
        User user = userRepository.findByUserEmail(adminDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 userId 입니다."));

        if (!passwordEncoder.matches(adminDto.getPassword(), user.getUserPw())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        //접근권한 확인
        Validator.adminCheck(user);

        Map<String, Object> result = new HashMap<>();

        result.put("token", jwtTokenUtils.generateAdminJwtToken(user.getUserName(), user.getUserEmail(), user.getUserLevel()));
        result.put("userId", user.getUserEmail());
        result.put("username가", user.getUserName());
        result.put("userLevel", user.getUserLevel());

        System.out.println("맵 안에 토큰값 확인: " + result.get("token"));

        return result;
    }

    //전체 유저 조회
    public List<User> getAllUser() {
        List<User> user = userRepository.findAll();
        return user;
    }

    //권한 변경
    @Transactional
    public String updateLevel(Long userId, AdminDto.Update update) {
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
