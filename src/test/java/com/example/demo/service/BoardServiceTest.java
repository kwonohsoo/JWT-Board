package com.example.demo.service;

import com.example.demo.config.QueryDslTestConfig;
import com.example.demo.dto.BoardDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.Member;
import com.example.demo.repository.BoardQueryRepository;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@Import(QueryDslTestConfig.class)
class BoardServiceTest {
    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BoardQueryRepository boardQueryRepository;

    private List<Board> boards;

    @BeforeEach
    void setUp() {
        boards = new ArrayList<>();
        boards.add(Board.builder().bno(1L).title("제목1").content("내용1").build()); // 테스트용 게시글 추가
        boards.add(Board.builder().bno(2L).title("제목2").content("내용2").build());
        boards.add(Board.builder().bno(3L).title("제목3").content("내용3").build());
        boards.add(Board.builder().bno(4L).title("제목4").content("내용4").build());
        boards.add(Board.builder().bno(5L).title("제목5").content("내용5").build());
        boards.add(Board.builder().bno(6L).title("제목6").content("내용6").build());
        boards.add(Board.builder().bno(7L).title("제목7").content("내용7").build());
        boards.add(Board.builder().bno(8L).title("제목8").content("내용8").build());
        boards.add(Board.builder().bno(9L).title("제목9").content("내용9").build());
        boards.add(Board.builder().bno(10L).title("제목10").content("내용10").build());
        boards.add(Board.builder().bno(11L).title("제목11").content("내용11").build());
    }

    @Test
    @DisplayName("게시글 등록")
    void 게시글_등록() {
        // Given
        Member testMember = new Member();
        testMember.setSno(1L);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));

        BoardDto boardDto = BoardDto.builder()
                .bno(1L)
                .title("테스트 제목")
                .content("테스트 내용")
                .views(0)
                .writerSno(1L)
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

    @Test
    @DisplayName("게시글 목록 조회 페이징")
    void 게시글_목록_조회_페이징_서비스_테스트() {
        // given
        Pageable pageable = PageRequest.of(0, 5); // Page 0, Page size 5
        Page<Board> boardPage = new PageImpl<>(boards, pageable, boards.size());
        when(boardQueryRepository.getAll(pageable)).thenReturn(boardPage);

        // when
        BoardResponseDto boardResponseDto = boardService.getAll(pageable);

        // then
        assertEquals(boardPage.getTotalPages(), boardResponseDto.getTotalPage());
        assertEquals(boardPage.getTotalElements(), boardResponseDto.getTotalCount());
        assertEquals(pageable.getPageNumber(), boardResponseDto.getPageNumber());
        assertEquals(pageable.getPageSize(), boardResponseDto.getPageSize());
        assertEquals(boards.size(), boardResponseDto.getBoardDataList().size());

        log.info(boardPage.toString());
        log.info(boardResponseDto.toString());
    }

    @Test
    @DisplayName("게시글 상세 조회")
    void 게시글_상세_조회() {
        // given
        Long bno = 1L;
        Board board = boards.get(0);
        when(boardRepository.findById(bno)).thenReturn(Optional.ofNullable(board));

        // when
        BoardDto result = boardService.findBoardByBno(bno);

        // then
        assertEquals(bno, result.getBno());
    }

    @Test
    @DisplayName("조회수 증가")
    @Transactional
    void 조회수_증가() {
        // given
        Long bno = 1L; // 증가시킬 게시물 번호

        // when
        long initViewCount = 0;
        long updatedViewCount = boardService.increaseViewCount(bno); // 조회수 증가 메소드 호출

        // then
        long expectedViewCount = initViewCount + 1; // 예상 증가된 조회수

        assertEquals(expectedViewCount, updatedViewCount); // 증가된 조회수와 예상 조회수 비교
    }
}