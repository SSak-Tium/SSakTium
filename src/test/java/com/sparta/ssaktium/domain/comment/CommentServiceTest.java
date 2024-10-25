package com.sparta.ssaktium.domain.comment;


import com.sparta.ssaktium.domain.boards.entity.Board;
import com.sparta.ssaktium.domain.boards.repository.BoardRepository;
import com.sparta.ssaktium.domain.comments.dto.request.CommentRequestDto;
import com.sparta.ssaktium.domain.comments.repository.CommentRepository;
import com.sparta.ssaktium.domain.comments.service.CommentService;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class) // Mockito 사용 설정
class CommentServiceTest {

    // 실제 테스트할 서비스 객체
    @InjectMocks
    private CommentService commentService;

    // 의존 객체들 Mocking
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private UserRepository userRepository;


}