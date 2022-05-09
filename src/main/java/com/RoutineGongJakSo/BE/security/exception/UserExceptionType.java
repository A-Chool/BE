package com.RoutineGongJakSo.BE.security.exception;


import org.springframework.http.HttpStatus;

public enum UserExceptionType implements BaseExceptionType {
    ALREADY_EXIST_USERNAME(600, HttpStatus.CONFLICT, "이미 존재하는 아이디입니다"),
    ALREADY_EXIST_NICKNAME(601, HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    WRONG_PASSWORD(602, HttpStatus.BAD_REQUEST, "비밀번호가 잘못되었습니다."),
    TRY_CHECKOUT(603, HttpStatus.BAD_REQUEST, "체크아웃을 먼저 해주세요."),
    NOT_FOUND_MEMBER(604, HttpStatus.NOT_FOUND, "회원 정보가 없습니다."),
    LOGIN_TOKEN_EXPIRE(605, HttpStatus.NOT_FOUND, "토큰이 존재하지 않습니다."),
    NOT_ONLINE(606, HttpStatus.NOT_FOUND, "로그인 된 사용자가 아닙니다."),
    LOW_LEVER(607, HttpStatus.BAD_REQUEST, "접근 권한이 없습니다."),
    YES_MEMBER(608, HttpStatus.BAD_REQUEST, "이미 만들어진 팀이 존재합니다."),
    NOT_COLUM(609, HttpStatus.BAD_REQUEST, "콜론 금지");

    private int errorCode;
    private HttpStatus httpStatus;
    private String errorMessage;

    //
    UserExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
}

