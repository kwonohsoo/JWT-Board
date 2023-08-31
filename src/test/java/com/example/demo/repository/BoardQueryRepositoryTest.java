package com.example.demo.repository;

import com.example.demo.dto.BoardDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardQueryRepositoryTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardQueryRepository boardQueryRepository;

    @Test
    void 게시글_등록() {
        // given
        String title = "test";
        String content = "test test";
        Long writerSno = 1L;

        when(memberRepository.findById(writerSno)).thenReturn(Optional.of(new Member()));

        BoardDto boardDto = BoardDto.builder()
                .title(title)
                .content(content)
                .views(1)
                .writerSno(writerSno)
                .bno(1L)
                .build();

        // when
        boardQueryRepository.saveBoard(boardDto);

        // then
        verify(boardRepository, times(1)).save(any(Board.class));
    }
}