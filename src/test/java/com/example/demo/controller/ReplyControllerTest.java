package com.example.demo.controller;

import com.example.demo.dto.ReplyRequestDto;
import com.example.demo.dto.ReplyResponseDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.Member;
import com.example.demo.entity.Reply;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.ReplyRepository;
import com.example.demo.service.ReplyService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@DisplayName("Reply Controller TEST")
class ReplyControllerTest {

    @InjectMocks
    private ReplyController replyController;

    @Mock
    private ReplyService replyService;

    @Mock
    private BoardRepository boardRepository;

    private List<Reply> replies;

    @BeforeEach
    void setUp() {
        Member member = Member.builder().sno(1L).name("이름").build();
        Board board = Board.builder().bno(1L).title("게시글 제목").content("게시글 내용").member(member).build();

        replies = new ArrayList<>();
        replies.add(Reply.builder().rno(1L).board(board).member(member).content("댓글1").build());
    }

    @Test
    @DisplayName("댓글 등록")
    void saveReply() {
        // given
        ReplyRequestDto replyRequestDto = new ReplyRequestDto();
        replyRequestDto.setWriterSno(1L);
        replyRequestDto.setBno(1L);
        replyRequestDto.setContent("댓글 등록");

        when(replyService.saveReply(any(ReplyRequestDto.class))).thenReturn(1L);

        // when
        ResponseEntity<?> response = replyController.saveReply(replyRequestDto);

        // then
        verify(replyService).saveReply(replyRequestDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, response.getBody());

        log.info(response.getStatusCode().toString());
        log.info(replyRequestDto.toString());
        log.info("--------------------------------------------------");
    }

    @Test
    @DisplayName("댓글 조회")
    void readReplies() {
        // given
        Long bno = 1L;
        Board board = new Board();

        when(boardRepository.findById(bno)).thenReturn(Optional.of(board));
        when(replyService.findRepliesByBoard(board)).thenReturn(replies);

        // when
        ResponseEntity<List<Reply>> responseEntity = replyController.readReplies(bno);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(replies, responseEntity.getBody());

        log.info("게시글 검색 결과:");
        log.info("게시글 번호: {}", replies.get(0).getBoard().getBno());
        log.info("댓글 번호: {}", replies.get(0).getRno());
        log.info("댓글 내용: {}", replies.get(0).getContent());
        log.info("--------------------------------------------------");
    }

    @Test
    @DisplayName("댓글 수정")
    void updateReply() {
        // given
        Long rno = 1L;
        ReplyRequestDto updateReply = new ReplyRequestDto();
        updateReply.setContent("댓글 수정 내용");

        // when
        ResponseEntity<String> response = replyController.updateReply(rno, updateReply);

        // then
        verify(replyService, times(1)).updateReply(rno, updateReply);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        log.info("댓글 수정 내용: " + updateReply);
        log.info(response.toString());
        log.info("--------------------------------------------------");
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteReply() {
        // given
        Long rno = 1L;

        // when
        ResponseEntity<String> response = replyController.deleteReply(rno);

        // then
        verify(replyService, times(1)).deleteReply(rno);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        log.info(response.toString());
        log.info("--------------------------------------------------");
    }
}