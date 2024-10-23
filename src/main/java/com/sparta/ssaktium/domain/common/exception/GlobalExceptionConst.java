package com.sparta.ssaktium.domain.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalExceptionConst {

    // 상태코드 400
    DUPLICATE_PASSWORD(HttpStatus.BAD_REQUEST, " 새 비밀번호는 이전에 사용한 비밀번호와 같을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, " 새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다."),
    INVALID_ROLE(HttpStatus.BAD_REQUEST, " 올바른 권한이 아닙니다."),
    INVALID_FILE_FORMAT(HttpStatus.BAD_REQUEST,"파일 형식이 잘못되었습니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "파일 크기가 잘못되었습니다."),
    LIKE_COUNT_UNDERFLOW(HttpStatus.BAD_REQUEST, "좋아요 수는 0보다 작을 수 없습니다."),

    // 상태코드 401
    UNAUTHORIZED_PASSWORD(HttpStatus.UNAUTHORIZED, " 비밀번호를 확인해주세요."),

    // 상태코드 403
    NOT_USER_OF_COMMENT(HttpStatus.FORBIDDEN, " 댓글 작성자가 아닙니다."),
    FILE_SAVE_FAILURE(HttpStatus.FORBIDDEN, "파일 저장중 오류가 발생하였습니다."),
    NOT_USER_OF_LIKE(HttpStatus.FORBIDDEN, " 댓글 작성자가 아닙니다."),

    // 상태코드 404git
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, " 회원이 존재하지 않습니다."),
    NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND, " 이메일을 확인해주세요."),
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, " 존재하지 않는 댓글입니다."),
    NOT_FOUND_CARD(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."),
    NOT_FOUND_TITLE(HttpStatus.NOT_FOUND, " 제목이 없습니다."),
    NOT_FOUND_FILE(HttpStatus.NOT_FOUND, " 파일이 없습니다."),
    DELETED_USER(HttpStatus.NOT_FOUND, " 탈퇴된 회원입니다."),
    NOT_FOUND_BOARD_LIKE(HttpStatus.NOT_FOUND, " 게시글에 해당 좋아요가 존재하지 않습니다."),

    // 상태코드 409
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, " 중복된 이메일입니다."),
    DUPLICATE_LIKE(HttpStatus.CONFLICT, "이미 좋아요가 눌려있습니다.");



    private final HttpStatus httpStatus;
    private final String message;
}