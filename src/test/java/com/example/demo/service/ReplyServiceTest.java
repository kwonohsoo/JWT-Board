package com.example.demo.service;

import com.example.demo.dto.ReplyRequestDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.Member;
import com.example.demo.entity.Reply;
import com.example.demo.repository.ReplyQueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@DisplayName("Reply Service TEST")
class ReplyServiceTest {

    @InjectMocks
    private ReplyService replyService;

    @Mock
    private ReplyQueryRepository replyQueryRepository;

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
        replyRequestDto.setBno(1L);
        replyRequestDto.setWriterSno(1L);
        replyRequestDto.setContent("댓글 등록");

        Reply savaReply = new Reply();
        savaReply.setRno(1L);
        when(replyQueryRepository.saveReply(replyRequestDto)).thenReturn(savaReply);

        // when
        Long savedRno = replyService.saveReply(replyRequestDto);
        assertEquals(1L, savedRno);

        // then
        verify(replyQueryRepository, times(1)).saveReply(replyRequestDto);

        log.info(replyRequestDto.toString());
    }

    @Test
    @DisplayName("댓글 조회")
    void findRepliesByBoard() {
        // given
        Board board = Board.builder().build();

        when(replyQueryRepository.findRepliesByBoard(board)).thenReturn(replies);

        // when
        List<Reply> replyList = replyService.findRepliesByBoard(board);

        // then
        assertEquals(replies.size(), replyList.size());
        assertEquals(replies.get(0).getContent(), replyList.get(0).getContent());

        verify(replyQueryRepository, times(1)).findRepliesByBoard(board);

        log.info("댓글 조회 결과: " + replyList);
        log.info("--------------------------------------------------");
    }

    @Test
    @DisplayName("댓글 수정")
    void updateReply() {
        // given
        Long rno = 1L;
        String updatedContent = "댓글 수정 내용";

        ReplyRequestDto replyRequestDto = new ReplyRequestDto();
        replyRequestDto.setContent(updatedContent);

        // when
        replyService.updateReply(rno, replyRequestDto);

        // then
        verify(replyQueryRepository, times(1)).updateReply(rno, replyRequestDto);

        log.info("댓글 수정 내용: " + updatedContent);
        log.info("--------------------------------------------------");
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteReply() {
        // given
        Long rno = 1L;

        // when
        replyService.deleteReply(rno);

        // then
        verify(replyQueryRepository, times(1)).deleteReply(rno);

        log.info(rno.toString());
        log.info("--------------------------------------------------");
    }
}