package com.example.demo.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 200 : 요청 성공
     */
    SUCCESS(true, HttpStatus.OK.value(), "요청에 성공하였습니다."),


    /**
     * 400 : Request 오류, Response 오류
     */
    // Common
    REQUEST_ERROR(false, HttpStatus.BAD_REQUEST.value(), "입력값을 확인해주세요."),
    EMPTY_JWT(false, HttpStatus.UNAUTHORIZED.value(), "JWT를 입력해주세요."),
    INVALID_JWT(false, HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,HttpStatus.FORBIDDEN.value(),"권한이 없는 유저의 접근입니다."),
    RESPONSE_ERROR(false, HttpStatus.NOT_FOUND.value(), "값을 불러오는데 실패하였습니다."),

    // users
    USERS_EMPTY_USER_ID(false, HttpStatus.BAD_REQUEST.value(), "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_PHONENUM(false, HttpStatus.BAD_REQUEST.value(), "전화번호를 입력해주세요."),
    POST_USERS_INVALID_PHONENUM(false, HttpStatus.BAD_REQUEST.value(), "전화번호 형식을 확인해주세요."),
    POST_USERS_EXISTS_PHONENUM(false,HttpStatus.BAD_REQUEST.value(),"중복된 전화번호입니다."),

    POST_USERS_EMPTY_NICKNAME(false, HttpStatus.BAD_REQUEST.value(), "닉네임을 입력해주세요."),
    POST_USERS_EXISTS_NICKNAME(false,HttpStatus.BAD_REQUEST.value(),"중복된 닉네임입니다."),
    FAILED_TO_LOGIN(false,HttpStatus.NOT_FOUND.value(),"없는 전화번호입니다."),

    // [POST] users/kakao
    POST_USERS_EMPTY_EMAIL(false, HttpStatus.BAD_REQUEST.value(), "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, HttpStatus.BAD_REQUEST.value(), "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false, HttpStatus.BAD_REQUEST.value(), "중복돤 이메일입니다."),

    // boards
    PRODUCTS_EMPTY_BOARD_ID(false, HttpStatus.BAD_REQUEST.value(), "게시물 아이디 값을 확인해주세요."),

    // [POST] /boards
    POST_BOARDS_EMPTY_TITLE(false, HttpStatus.BAD_REQUEST.value(), "제목을 입력해주세요."),
    POST_BOARDS_EMPTY_CONTENT(false, HttpStatus.BAD_REQUEST.value(), "내용을 입력해주세요."),
    POST_BOARDS_EMPTY_IMAGE(false, HttpStatus.BAD_REQUEST.value(), "사진을 1장 이상 첨부해주세요."),

    // reviews
    REVIEWS_EMPTY_REVIEW_ID(false, HttpStatus.BAD_REQUEST.value(), "리뷰 아이디 값을 확인해주세요."),

    // comments
    COMMENT_EMPTY_COMMENT_ID(false, HttpStatus.BAD_REQUEST.value(), "댓글 아이디 값을 확인해주세요."),

    // [POST] /living/:boardId/comments
    POST_COMMENT_EMPTY_CONTENT(false, HttpStatus.BAD_REQUEST.value(), "내용을 입력해주세요."),

    // login/kakao
    KAKAO_CODE_EMPTY(false, HttpStatus.BAD_REQUEST.value(), "인가 코드를 입력해주세요"),



    /**
     * 50 : Database, Server 오류
     */
    DATABASE_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userId}
    MODIFY_FAIL_NICKNAME(false,HttpStatus.INTERNAL_SERVER_ERROR.value(),"유저네임 수정 실패"),
    MODIFY_FAIL_PROFILEIMG(false,HttpStatus.INTERNAL_SERVER_ERROR.value(),"프로필 이미지 수정 실패"),
    PASSWORD_ENCRYPTION_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "비밀번호 복호화에 실패하였습니다."),
    /**
     *
     */
    LIKE_BOARD_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(),"이미 좋아요를 누른 게시물입니다."),
    UNLIKE_BOARD_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(),"좋아요를 취소할 수 없습니다."),
    NOT_EXISTS_BOARD_ERROR(false,HttpStatus.INTERNAL_SERVER_ERROR.value(),"존재하지 않는 게시물입니다.");


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
