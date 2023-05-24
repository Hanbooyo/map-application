package com.mapapplication.mapapplication.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    TEST(HttpStatus.INTERNAL_SERVER_ERROR, "001", "businessExceptionTest"),


    // 로그인
    INVALID_MEMBER_TYPE(HttpStatus.BAD_REQUEST, "M-001", "잘못된 회원 타입입니다.(memberType : KAKAO"),
    FAILED_LOGIN(HttpStatus.BAD_REQUEST, "에러코드","에러메세지"),
    ALREADY_REGISTERED_MEMBER(HttpStatus.BAD_REQUEST, "M-002", "이미 가입된 회원입니다."),
    MEMBER_NOT_EXIST(HttpStatus.BAD_REQUEST, "M-003", "존재하지 않는 회원입니다."),

    //
    TOKEN_EXPRIED(HttpStatus.UNAUTHORIZED, "A-001", "토큰이 만료되었습니다."),
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "A-002", "올바른 토큰이 아닙니다."),
    NOT_EXISTS_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "A-003", "Authorization Header가 비었습니다."),
    NOT_VALID_BEARER(HttpStatus.UNAUTHORIZED, "A-004", "인증 타입이 Bearer이 아닙니다"),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "A-005", "해당 refresh token은 존재하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A-006", "해당 refresh token은 만료되었습니다."),
    NOT_ACCESS_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "A-007", "해당 token은 Access token이 아닙니다."),
    FORBIDDEN_ADMIN(HttpStatus.UNAUTHORIZED, "A-008", "관리자 Role이 아닙니다."),


    // 장소

    ;

    ErrorCode(HttpStatus httpStatus, String errorcode, String message) {
        this.httpStatus = httpStatus;
        this.errorcode = errorcode;
        this.message = message;
    }

    private HttpStatus httpStatus;
    private String errorcode;
    private String message;


}
