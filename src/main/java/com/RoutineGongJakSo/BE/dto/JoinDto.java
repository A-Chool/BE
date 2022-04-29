package com.RoutineGongJakSo.BE.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class JoinDto {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "닉네임는 필수 입력 값입니다.")
    @Pattern(regexp = "^([a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣]).{1,10}$", message = "닉네임은 한글, 영문, 숫자만 가능하며 2-10자리 가능합니다.")
    private String userName;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9]{6,12}$", message = "숫자와 문자를 포함한 6-12자리 입력해주세요.")
//    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{10,}$|^(?=.*\\d)(?=.*[$@$!%*#?&])[\\d$@$!%*#?&]{10,}$|^(?=.*[$@$!%*#?&])(?=.*[A-Za-z])[A-Za-z$@$!%*#?&]{10,}$|^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{10,}$", message = "10글자 이상, 영문/숫자/특수문자(공백 제외)/2개 이상의 조합을 사용해주세요.")
    private String userPw;

    @NotBlank(message = "비밀번호 확인은 필수 입력 값입니다.")
    private String userPwCheck;

    @NotBlank(message = "핸드폰 번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", message = "올바른 형식의 전화번호가 아닙니다.")
    private String phoneNumber;
}
