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
    DO_NOT_REQUEST_YOURSELF(HttpStatus.BAD_REQUEST, "본인을 친구로 요청할 수 없습니다."),
    ERR_ALREADY_FRIEND(HttpStatus.BAD_REQUEST, "이미 수락했거나 거절된 친구 요청입니다."),
    LIKE_COUNT_UNDERFLOW(HttpStatus.BAD_REQUEST, "좋아요 수는 0보다 작을 수 없습니다."),

    // 상태코드 401
    UNAUTHORIZED_PASSWORD(HttpStatus.UNAUTHORIZED, " 비밀번호를 확인해주세요."),

    // 상태코드 403
    NOT_USER_OF_COMMENT(HttpStatus.FORBIDDEN, " 댓글 작성자가 아닙니다."),
    FILE_SAVE_FAILURE(HttpStatus.FORBIDDEN, "파일 저장중 오류가 발생하였습니다."),
    ERR_FRIEND_REQUEST_UNAUTHORIZED_CANCELLATION(HttpStatus.FORBIDDEN, "본인이 보낸 친구 요청만 취소할 수 있습니다."),
    ERR_UNAUTHORIZED_FRIEND_REQUEST_ACCEPTANCE(HttpStatus.FORBIDDEN, "본인이 받은 친구 요청만 수락할 수 있습니다."),
    NOT_USER_OF_LIKE(HttpStatus.FORBIDDEN, " 댓글 작성자가 아닙니다."),
    NOT_USER_OF_BOARD(HttpStatus.FORBIDDEN, " 게시글 작성자가 아닙니다."),

    // 상태코드 404
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, " 회원이 존재하지 않습니다."),
    NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND, " 이메일을 확인해주세요."),
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, " 존재하지 않는 댓글입니다."),
    NOT_FOUND_CARD(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."),
    NOT_FOUND_TITLE(HttpStatus.NOT_FOUND, " 제목이 없습니다."),
    NOT_FOUND_FILE(HttpStatus.NOT_FOUND, " 파일이 없습니다."),
    NOT_FOUND_DICTIONARY(HttpStatus.NOT_FOUND, " 해당 식물이 없습니다."),
    DELETED_USER(HttpStatus.NOT_FOUND, " 탈퇴된 회원입니다."),
    NOT_FOUND_FREIND_REQUEST(HttpStatus.NOT_FOUND, "해당 친구 요청이 없습니다."),
    NOT_FOUND_FREIND(HttpStatus.NOT_FOUND, "해당 유저는 친구등록이 되어있지 않습니다"),
    NOT_FOUND_PLANT(HttpStatus.NOT_FOUND, "해당 식물은 없습니다."),
    NOT_FOUND_BOARD(HttpStatus.NOT_FOUND, "해당 게시글이 존재하지 않습니다."),
    NOT_FOUND_BOARD_LIKE(HttpStatus.NOT_FOUND, " 게시글에 해당 좋아요가 존재하지 않습니다."),
    NOT_FOUND_COMMENT_LIKE(HttpStatus.NOT_FOUND, " 댓글에 해당 좋아요가 존재하지 않습니다."),
    NOT_FOUND_PLANTDIARY(HttpStatus.NOT_FOUND, "식물다이어리가 존재하지 않습니다."),

    // 상태코드 409
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, " 중복된 이메일입니다."),
    ALREADY_REQUEST(HttpStatus.CONFLICT, "중복된 친구 요청입니다."),
    ERR_ALREADY_ACCEPTED_FRIEND(HttpStatus.CONFLICT, "이미 수락한 친구입니다."),
    DUPLICATE_LIKE(HttpStatus.CONFLICT, "이미 좋아요가 눌려있습니다.");



    private final HttpStatus httpStatus;
    private final String message;
}