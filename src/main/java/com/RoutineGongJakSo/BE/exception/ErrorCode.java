package com.RoutineGongJakSo.BE.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 400 Bad Request
    ALREADY_EXIST_WEEK_NAME(HttpStatus.BAD_REQUEST, "400_1", "중복된 주차가 존재합니다."),
    BLANK_WEEK_NAME(HttpStatus.BAD_REQUEST, "400_2", "주차는 필수 입력값입니다."),
    BLANK_USER_NAME(HttpStatus.BAD_REQUEST, "400_1", "아이디는 필수 입력값입니다."),
    INVALID_PATTERN_USER_NAME(HttpStatus.BAD_REQUEST, "400_2", "아이디를 이메일 형식으로 입력해주세요."),
    BLANK_USER_NICKNAME(HttpStatus.BAD_REQUEST, "400_3", "닉네임은 필수 입력값입니다."),
    INVALID_PATTERN_USER_NICKNAME(HttpStatus.BAD_REQUEST, "400_4", "닉네임을 2자~8자로 입력해주세요."),
    BLANK_USER_PW(HttpStatus.BAD_REQUEST, "400_5", "비밀번호는 필수 입력값입니다."),
    INVALID_PATTERN_USER_PW(HttpStatus.BAD_REQUEST, "400_6", "비밀번호는 영문 대소문자,숫자,특수문자를 사용하여 8~20자로 입력해주세요."),
    BLANK_USER_PW_CHECK(HttpStatus.BAD_REQUEST, "400_7", "비밀번호 확인은 필수 입력값입니다."),
    NOT_EQUAL_USER_PW_CHECK(HttpStatus.BAD_REQUEST, "400_8", "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    NOT_EQUAL_USER_PW(HttpStatus.BAD_REQUEST, "400_11", "비밀번호를 확인해주세요."),
    DUPLICATED_USER_NAME(HttpStatus.BAD_REQUEST, "400_9", "유저아이디가 이미 존재합니다."),
    DUPLICATED_USER_NICKNAME(HttpStatus.BAD_REQUEST, "400_10", "유저닉네임이 이미 존재합니다."),
    BLANK_USER_MBTI(HttpStatus.BAD_REQUEST, "400_11", "MBTI 전부 체크해주세요. null이 있습니다."),

    // 404 Not Found
    NOT_FOUND_USER_ID(HttpStatus.NOT_FOUND, "404_1", "유저 아이디가 존재하지 않습니다."),
    NOT_FOUND_LOCATION_ID(HttpStatus.NOT_FOUND, "404_2", "지역 코드가 존재하지 않습니다."),
    NOT_FOUND_CATEGORY_ID(HttpStatus.NOT_FOUND, "404_2", "카테고리 코드가 존재하지 않습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;

}