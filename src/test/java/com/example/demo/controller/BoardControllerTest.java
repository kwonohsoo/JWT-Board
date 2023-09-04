package com.example.demo.controller;

import com.example.demo.dto.BoardDto;
import com.example.demo.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith({MockitoExtension.class})
@DisplayName("Board Controller TEST")
class BoardControllerTest {

    @Mock
    private BoardService boardService;

    @InjectMocks
    private BoardController boardController;

    private List<BoardDto> boards;

    @BeforeEach
    void setUp() {
        boards = new ArrayList<>();

        boards.add(BoardDto.builder().bno(1L).title("제목1").content("내용1").build());
        boards.add(BoardDto.builder().bno(2L).title("제목2").content("내용2").build());
        boards.add(BoardDto.builder().bno(3L).title("제목3").content("내용3").build());
        boards.add(BoardDto.builder().bno(4L).title("제목4").content("내용4").build());
        boards.add(BoardDto.builder().bno(5L).title("제목5").content("내용5").build());
        boards.add(BoardDto.builder().bno(6L).title("제목6").content("내용6").build());
        boards.add(BoardDto.builder().bno(7L).title("제목7").content("내용7").build());
        boards.add(BoardDto.builder().bno(8L).title("제목8").content("내용8").build());
        boards.add(BoardDto.builder().bno(9L).title("제목9").content("내용9").build());
        boards.add(BoardDto.builder().bno(10L).title("제목10").content("내용10").build());
    }

    @Test
    @DisplayName("게시글 작성")
    void savaBoard() {
        // given
        BoardDto requestDto = new BoardDto();
        BoardDto savedBoardDto = new BoardDto();
        savedBoardDto.setBno(1L);
        savedBoardDto.setTitle("제목");
        savedBoardDto.setContent("내용");
        savedBoardDto.setWriterSno(1L);

        when(boardService.saveBoard(any(BoardDto.class))).thenReturn(savedBoardDto);

        // when
        ResponseEntity<?> response = boardController.createBoard(requestDto);

        //then
        verify(boardService).saveBoard(requestDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedBoardDto, response.getBody());

        log.info(response.toString());
        log.info("--------------------------------------------------");
    }

    @Test
    @DisplayName("게시판 조회 페이징")
    void getAllPage() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("게시글 상세 조회")
    void findBoardByBno() {
        // given
        Long bno = 1L;
        BoardDto boardDto = new BoardDto();
        boardDto.setTitle("제목");
        boardDto.setContent("내용");

        when(boardService.findBoardByBno(bno)).thenReturn(boardDto);

        BoardDto result = boardController.findBoardByBno(bno).getBody();

        // when
        ResponseEntity<BoardDto> responseEntity = boardController.findBoardByBno(bno);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(result, responseEntity.getBody());

        log.info(responseEntity.toString());
        log.info("--------------------------------------------------");
    }

    @Test
    @DisplayName("조회수 증가")
    public void updateViewCount() {
        // given
        Long bno = 1L;
        long updatedViewCount = 5L;

        when(boardService.increaseViewCount(bno)).thenReturn(updatedViewCount);

        // when
        ResponseEntity<String> responseEntity = boardController.increaseViewCount(bno);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("조회수 : " + updatedViewCount, responseEntity.getBody());

        log.info(responseEntity.toString());
        log.info("--------------------------------------------------");
    }

    @Test
    @DisplayName("게시글 검색")
    void searchBoard() {
        // given
        String keyword = "제목1";

        when(boardController.searchBoard(keyword)).thenReturn(boards);

        // when
        List<BoardDto> result = boardController.searchBoard(keyword);

        // then
        assertEquals(10, result.size());
        assertEquals("제목1", result.get(0).getTitle());
        verify(boardService, times(1)).searchBoard(keyword);
        assertEquals(boards, result);

        log.info("게시글 검색 결과:");
        log.info("BNO: {}", result.get(0).getBno());
        log.info("Title: {}", result.get(0).getTitle());
        log.info("Content: {}", result.get(0).getContent());
        log.info("Views: {}", result.get(0).getViews());
        log.info("--------------------------------------------------");
    }

    @Test
    @DisplayName("게시글 수정")
    void updateBoard() {
        // given
        Long bno = 1L;
        BoardDto updatedBoardDto = new BoardDto();

        updatedBoardDto.setTitle("새로운 제목");
        updatedBoardDto.setContent("새로운 내용");

        // when
        ResponseEntity<String> responseEntity = boardController.updateBoard(bno, updatedBoardDto);

        // then
        verify(boardService, times(1)).updateBoard(bno, updatedBoardDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        log.info(updatedBoardDto.toString());
        log.info(responseEntity.toString());
        log.info("--------------------------------------------------");
    }

    @Test
    @DisplayName("게시글 삭제")
    void deleteBoard() {
        // given
        Long bno = 1L;

        // when
        ResponseEntity<String> responseEntity = boardController.deleteBoard(bno);

        // then
        verify(boardService, times(1)).deleteBoard(bno);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        log.info(responseEntity.toString());
        log.info("--------------------------------------------------");
    }
}