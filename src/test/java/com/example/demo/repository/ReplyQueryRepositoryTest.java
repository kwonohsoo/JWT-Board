package com.example.demo.repository;

import com.example.demo.dto.ReplyRequestDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.Member;
import com.example.demo.entity.Reply;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
@DisplayName("Reply Repository TEST")
class ReplyQueryRepositoryTest {

    @InjectMocks
    private ReplyQueryRepository replyQueryRepository;

    @Mock
    private ReplyRepository replyRepository;

    @Mock
    BoardRepository boardRepository;

    @Mock
    private MemberRepository memberRepository;

    private List<Reply> replies;

    @BeforeEach
    void setUp() {
        Member member = Member.builder().sno(1L).name("이름").build();
        Board board = Board.builder().bno(1L).title("게시글 제목").content("게시글 내용").member(member).build();

        replies = new ArrayList<>();
        replies.add(Reply.builder().rno(1L).board(board).member(member).content("댓글1").build());
    }

    @Test
    @DisplayName("댓글 작성")
    void saveReply() {
        // given
        ReplyRequestDto replyRequestDto = new ReplyRequestDto();
        replyRequestDto.setContent("댓글 내용");
        replyRequestDto.setWriterSno(1L);
        replyRequestDto.setBno(1L);

        Member member = new Member();
        when(memberRepository.findById(replyRequestDto.getWriterSno())).thenReturn(java.util.Optional.of(member));

        List<Reply> replies = new ArrayList<>();

        Board board = new Board(member, "게시글 제목", "게시글 내용", 0, replies);
        when(boardRepository.findById(replyRequestDto.getBno())).thenReturn(java.util.Optional.of(board));

        // when
        Reply savedReply = replyQueryRepository.saveReply(replyRequestDto);

        // then
        verify(memberRepository, times(1)).findById(replyRequestDto.getWriterSno());
        verify(boardRepository, times(1)).findById(replyRequestDto.getBno());
        verify(replyRepository, times(1)).save(any(Reply.class));

        assertNotNull(savedReply);

        log.info(replyRequestDto.toString());
        log.info("--------------------------------------------------");
    }

    @Test
    @DisplayName("댓글 목록 조회")
    void findRepliesByBoard() {
        // given
        Long rno = 1L;
        Reply reply = replies.get(0);
        when(replyRepository.findById(rno)).thenReturn(Optional.of(reply));

        // when
        Optional<Reply> optionalReply = replyRepository.findById(rno);

        // then
        assertTrue(optionalReply.isPresent());
        Reply result = optionalReply.get();
        assertEquals(reply.getRno(), result.getRno());
        assertEquals(reply.getBoard().getBno(), result.getBoard().getBno());
        assertEquals(reply.getMember().getSno(), result.getMember().getSno());
        assertEquals(reply.getContent(), result.getContent());

        log.info("게시글 상세 정보:");
        log.info("댓글 번호: " + result.getRno());
        log.info("게시글 번호: " + result.getBoard().getBno());
        log.info("회원 번호: " + result.getMember().getSno());
        log.info("댓글: " + result.getContent());
        log.info("--------------------------------------------------");
    }

    @Test
    @DisplayName("댓글 수정")
    void updateReply() {
        // given
        Long rno = 1L;
        String updateContent = "수정된 댓글";

        ReplyRequestDto replyRequestDto = new ReplyRequestDto();
        replyRequestDto.setContent(updateContent);
        replyRequestDto.setBno(1L);
        replyRequestDto.setWriterSno(1L);

        // when
        replyRepository.update(rno, replyRequestDto);

        // then
        verify(replyRepository, times(1)).update(rno, replyRequestDto);

        log.info("댓글 수정 내용: " + replyRequestDto.getContent());
        log.info("--------------------------------------------------");
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteReply() {
        // given
        Long rno = 1L;

        // when
        replyRepository.deleteById(rno);


        // then
        verify(replyRepository, times(1)).deleteById(rno);

        log.info("게시글 삭제 완료: RNO " + rno);
        log.info("--------------------------------------------------");
    }
}