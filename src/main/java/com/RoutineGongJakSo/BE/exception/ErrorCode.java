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
    DISPLAY_WEEK_ID(HttpStatus.BAD_REQUEST, "400_3", "이 주차는 현재 display 중이며, 지울 수 없습니다."),
    TOO_LONG_WEEK_NAME(HttpStatus.BAD_REQUEST, "400_4", "주차는 필수 입력값입니다."),

    ALREADY_EXIST_TEAM_NAME(HttpStatus.BAD_REQUEST, "400_5", "중복된 팀 이름이 존재합니다."),

    // 404 Not Found
    NOT_FOUND_WEEK_ID(HttpStatus.NOT_FOUND, "404_1", "주차 아이디가 존재하지 않습니다."),
    NOT_FOUND_USER_ID(HttpStatus.NOT_FOUND, "404_1", "유저 아이디가 존재하지 않습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;

}