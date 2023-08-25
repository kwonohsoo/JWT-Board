package com.example.demo.repository;

import com.example.demo.dto.ReplyRequestDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.QReply;
import com.example.demo.entity.Reply;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Repository
@Transactional(readOnly = true)
public class ReplyQueryRepository {

    private final MemberRepository memberRepository;
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final JPAQueryFactory queryFactory;

    // 댓글 작성
    @Transactional
    public Reply saveReply(ReplyRequestDto replyRequestDto) {

        Reply reply = Reply.builder()
                .content(replyRequestDto.getContent())
                .member(memberRepository.findById(replyRequestDto.getWriterSno()).orElse(null))
                .board(boardRepository.findById(replyRequestDto.getBno()).orElse(null))
                .build();

        replyRepository.save(reply);

        return reply;
    }

    // 댓글 목록 조회
    public List<Reply> findRepliesByBoard(Board board) {

        QReply reply = QReply.reply;

        return queryFactory
                .selectFrom(reply)
                .where(reply.board.eq(board))
                .fetch();
    }

    // 댓글 수정
    @Transactional
    public void updateReply(Long rno, ReplyRequestDto replyRequestDto) {

        QReply qReply = QReply.reply;

        queryFactory
                .update(qReply)
                .set(qReply.content, replyRequestDto.getContent())
                .where(qReply.rno.eq(rno))
                .execute();
    }

    // 댓글 삭제
    @Transactional
    public void deleteReply(Long rno) {

        QReply reply = QReply.reply;

        queryFactory
                .delete(reply)
                .where(reply.rno.eq(rno))
                .execute();
    }
}
