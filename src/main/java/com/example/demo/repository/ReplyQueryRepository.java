package com.example.demo.repository;

import com.example.demo.dto.ReplyDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.QReply;
import com.example.demo.entity.Reply;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.demo.entity.QReply.reply;

@RequiredArgsConstructor
@Repository
@Transactional(readOnly = true)
public class ReplyQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    // 댓글 작성
    @Transactional
    public ReplyDto saveReply(ReplyDto replyDto) {
        Reply reply = Reply.builder()
                        .content(replyDto.getContent()).build();
        em.persist(reply);
        return replyDto;
    }

    // 댓글 목록 조회
    public List<Reply> findRepliesByBoard(Board board) {
        QReply reply = QReply.reply;

        return queryFactory
                .selectFrom(reply)
                .where(reply.bno.eq(board))
                .fetch();
    }

    // 댓글 수정
    @Transactional
    public void updateReply(Long rno, ReplyDto replyDto) {
        QReply qReply = QReply.reply;
        queryFactory
                .update(qReply)
                .set(qReply.content, replyDto.getContent())
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
