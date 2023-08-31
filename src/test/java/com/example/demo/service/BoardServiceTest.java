package com.example.demo.service;

import com.example.demo.dto.BoardDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.Member;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.MemberRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private BoardService boardService;

    @Test
    @DisplayName("게시글 등록")
    void testSaveBoard() {
        // Given
        Member testMember = new Member();
        testMember.setSno(2L);

        when(memberRepository.findById(2L)).thenReturn(Optional.of(testMember));

        BoardDto boardDto = BoardDto.builder()
                .bno(1L)
                .title("테스트 제목")
                .content("테스트 내용")
                .views(0)
                .writerSno(2L)
                .build();

        Board savedBoard = Board.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .views(boardDto.getViews())
                .member(testMember)
                .build();
        when(boardRepository.save(any(Board.class))).thenReturn(savedBoard);

        // When
        BoardDto savedBoardDto = boardService.saveBoard(boardDto);

        // Then
        assertEquals(boardDto.getTitle(), savedBoardDto.getTitle());
        assertEquals(boardDto.getContent(), savedBoardDto.getContent());
        assertEquals(boardDto.getViews(), savedBoardDto.getViews());
        assertEquals(boardDto.getWriterSno(), savedBoardDto.getWriterSno());
    }
}