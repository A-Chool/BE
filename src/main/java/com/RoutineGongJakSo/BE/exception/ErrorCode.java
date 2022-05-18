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
    BAD_FORM_TYPE(HttpStatus.BAD_REQUEST, "400_50", "이미지 형식이 올바르지 않습니다."),
    LIAR_USER_IMAGE(HttpStatus.BAD_REQUEST, "400_51", "업로드된 이미지가 존재하지 않습니다."),

    // 404 Not Found
    NOT_FOUND_WEEK_ID(HttpStatus.NOT_FOUND, "404_1", "주차 아이디가 존재하지 않습니다."),
    NOT_FOUND_USER_ID(HttpStatus.NOT_FOUND, "404_1", "유저 아이디가 존재하지 않습니다."),
    NOT_FOUND_LOCATION_ID(HttpStatus.NOT_FOUND, "404_2", "지역 코드가 존재하지 않습니다."),
    NOT_FOUND_CATEGORY_ID(HttpStatus.NOT_FOUND, "404_2", "카테고리 코드가 존재하지 않습니다."),

    // 500 Sever  Error
    FAIL_FILE_UPLODA(HttpStatus.INTERNAL_SERVER_ERROR, "505_50", "파일 업로드에 실패 했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;

}