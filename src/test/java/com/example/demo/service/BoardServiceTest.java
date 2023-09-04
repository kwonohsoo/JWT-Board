package com.example.demo.service;

import com.example.demo.config.QueryDslTestConfig;
import com.example.demo.dto.BoardDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.Member;
import com.example.demo.repository.BoardQueryRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@Import(QueryDslTestConfig.class)
@DisplayName("Board Service TEST")
class BoardServiceTest {
    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardQueryRepository boardQueryRepository;

    private List<Board> boards;

    @BeforeEach
    void setUp() {
        boards = new ArrayList<>();
        Member member = new Member();

        boards.add(Board.builder().bno(1L).title("제목1").content("내용1").member(member).build()); // 테스트용 게시글 추가
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
    void savaBoard() {
        // given
        Long bno = 1L;
        BoardDto boardToCreate = new BoardDto();

        boardToCreate.setBno(bno);
        boardToCreate.setWriterSno(1L);
        boardToCreate.setTitle("제목");
        boardToCreate.setContent("내용");

        BoardDto boardDto = BoardDto.builder()
                .bno(bno)
                .title(boardToCreate.getTitle())
                .content(boardToCreate.getContent())
                .writerSno(boardToCreate.getWriterSno()).build();

        // when
        boardService.saveBoard(boardToCreate);

        // then
        verify(boardQueryRepository, times(1)).saveBoard(boardToCreate);

        assertEquals(boardToCreate.getTitle(), boardDto.getTitle());
        assertEquals(boardToCreate.getContent(), boardDto.getContent());
        assertEquals(boardToCreate.getWriterSno(), boardDto.getWriterSno());

        log.info(boardToCreate.toString());
        log.info("--------------------------------------------------");
    }

    @Test
    @DisplayName("게시판 조회 페이징")
    void getAllPage() {
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
        log.info("--------------------------------------------------");
    }

    @Test
    @DisplayName("게시글 상세 조회")
    void findBoardByBno() {
        // given
        Long bno = 1L;
        Board board = boards.get(0);

        when(boardQueryRepository.findBoardByBno(bno)).thenReturn(board);

        // when
        BoardDto result = boardService.findBoardByBno(bno);

        // then
        verify(boardQueryRepository, times(1)).findBoardByBno(bno);
        assertEquals(bno, result.getBno());

        log.info(result.toString());
        log.info("--------------------------------------------------");
    }

    @Test
    @DisplayName("조회수 증가")
    @Transactional
    void updateViewCount() {
        // given
        Long bno = 1L;

        when(boardQueryRepository.updateViewCount(bno)).thenReturn(1L);

        // when
        long updatedCount = boardService.increaseViewCount(bno);

        // then
        verify(boardQueryRepository, times(1)).updateViewCount(bno);

        assertEquals(1, updatedCount);

        log.info(String.valueOf(updatedCount));
        log.info("--------------------------------------------------");
    }

    @Test
    @DisplayName("게시글 수정")
    void updateBoard() {
        // given
        Long bno = 1L;
        BoardDto boardToUpdate = new BoardDto();

        String updateTitle = "수정된 제목";
        String updateContent = "수정된 내용";

        // when
        boardToUpdate.setBno(bno);
        boardToUpdate.setTitle(updateTitle);
        boardToUpdate.setContent(updateContent);

        boardService.updateBoard(bno, boardToUpdate);

        // then
        verify(boardQueryRepository, times(1)).updateBoard(bno, boardToUpdate);

        log.info(boardToUpdate.toString());
        log.info("--------------------------------------------------");
    }

    @Test
    @DisplayName("게시글 삭제")
    void deleteBoard() {
        // given
        Long bno = 1L;

        // when
        boardService.deleteBoard(bno);

        // then
        verify(boardQueryRepository, times(1)).deleteBoard(bno);

        log.info(bno.toString());
        log.info("--------------------------------------------------");
    }
}