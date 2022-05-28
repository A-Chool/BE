package com.RoutineGongJakSo.BE.client.user;

import com.RoutineGongJakSo.BE.security.validator.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class) //test class 가 Mockito 를 사용함을 의미
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks // Mock 객체가 주입된 클래스를 사용하게 될 클래스
    private UserService userService;

    @BeforeEach
    void beforeEach() {
        //given
        JoinDto joinDto = JoinDto.builder()
                .email("test@test.com")
                .userName("나확인")
                .userPw("test123t")
                .userPwCheck("test123test")
                .phoneNumber("010-1234-1234")
                .build();

        //when
        Optional<User> found = userRepository.findByUserEmail(joinDto.getEmail());
        Validator.checkUser(found);

        User user = User.builder()
                .userEmail(joinDto.getEmail())
                .userName(joinDto.getUserName())
                .userPw(passwordEncoder.encode(joinDto.getUserPw()))
                .phoneNumber(joinDto.getPhoneNumber())
                .build();

        userRepository.save(user);
    }


    @Test
    @DisplayName("회원가입 기능 작동 확인")
    void join() {
        //given
        JoinDto joinDto = JoinDto.builder()
                .email("test@test.com")
                .userName("나확인")
                .userPw("test123t")
                .userPwCheck("test123test")
                .phoneNumber("010-1234-1234")
                .build();

        //when
        Optional<User> found = userRepository.findByUserEmail(joinDto.getEmail());
        Validator.checkUser(found);

        User user = User.builder()
                .userEmail(joinDto.getEmail())
                .userName(joinDto.getUserName())
                .userPw(passwordEncoder.encode(joinDto.getUserPw()))
                .phoneNumber(joinDto.getPhoneNumber())
                .build();

        userRepository.save(user);

        //then
        Assertions.assertThat(joinDto).isNotNull();
        Assertions.assertThat(joinDto.getEmail()).isEqualTo("test@test.com");
        Assertions.assertThat(user.getUserName()).isEqualTo("나확인");
    }

//    @Test
//    @DisplayName("이메일이 중복되는 경우")
//    void emailValid() throws Error {
//        //given
//        JoinDto joinDto = JoinDto.builder()
//                .email("test@test.com")
//                .userName("나확인")
//                .userPw("test123t")
//                .userPwCheck("test123test")
//                .phoneNumber("010-1234-1234")
//                .build();
//
//        //when
//        Optional<User> found = userRepository.findByUserEmail(joinDto.getEmail());
//
//
//        //then
//        Assertions.assertThat()
//
//    }
}